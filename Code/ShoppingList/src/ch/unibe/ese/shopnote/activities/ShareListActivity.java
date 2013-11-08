package ch.unibe.ese.shopnote.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.ShoppingList;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.ShareListRequest;
import ch.unibe.ese.shopnote.sidelist.NavigationDrawer;
import ch.unibe.ese.shopnote.R;

@SuppressLint("NewApi")
public class ShareListActivity extends BaseActivity {

	private ListManager manager;
	private FriendsManager friendsManager;
	private SyncManager syncManager;
	private ArrayAdapter<Friend> friendsAdapter;
	private ArrayAdapter<Friend> autocompleteAdapter;
	private ShoppingList list;
	private DrawerLayout drawMenu;
	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_list);
		// Show the Up button in the action bar.
		getActionBar();

		manager = getListManager();
		friendsManager = getFriendsManager();
		friendsAdapter = new ArrayAdapter<Friend>(this,
				R.layout.shopping_list_item, new ArrayList<Friend>());

		syncManager = getSyncManager();

		// Create drawer menu
		NavigationDrawer nDrawer = new NavigationDrawer();
		drawMenu = nDrawer.constructNavigationDrawer(drawMenu, this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			long listIndex = extras.getLong(EXTRAS_LIST_ID);
			list = manager.getShoppingList(listIndex);
			setTitle(this.getString(R.string.share_list_title) + " "
					+ list.getName());
		}

		// create autocompletion
		AutoCompleteTextView textName = createAutocomplete();

		// autocompletion click listener
		textName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				Friend friend = (Friend) autocompleteAdapter.getItem(position);
				friendsAdapter.add(friend);
				updateFriendsList();
				
				friendsManager.addFriendToList(list, friend);

				setTextViewText(R.id.editTextName, "");
			}
		});

	}

	/**
	 * creates or updates the autocreation textfield
	 * 
	 * @return
	 */
	private AutoCompleteTextView createAutocomplete() {
		AutoCompleteTextView textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
		autocompleteAdapter = new ArrayAdapter<Friend>(this,
				android.R.layout.simple_list_item_1,
				friendsManager.getFriendsList());
		textName.setAdapter(autocompleteAdapter);

		return textName;
	}

	/** Called when the user touches the create button */
	public void addFriend(View view) {
		String name = getTextViewText(R.id.editTextName);

		Intent intent = new Intent(this, CreateFriendActivity.class);
		intent.putExtra(EXTRAS_FRIEND_NAME, name);
		startActivityForResult(intent, 0);

		setTextViewText(R.id.editTextName, "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share_list, menu);

		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);
		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareIntent(createShareIntent());

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Navigate back to the list
			finish();
			return true;
		case R.id.menu_item_share:
			createShareIntent();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onResume() {
		super.onResume();
		updateFriendsList();
	}

	/**
	 * Get the result from the CreateFriendsActivity (when a new friend is
	 * added) and add it to the list
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		long friendId = -1;

		if (resultCode == RESULT_OK) {
			Bundle korb = data.getExtras();
			friendId = korb.getLong(EXTRAS_FRIEND_ID);
		}

		if (friendId != -1) {
			Friend friend = friendsManager.getFriend(friendId);
			friendsAdapter.add(friend);
			// update lists
			updateFriendsList();
			createAutocomplete();
		}
	}

	/**
	 * Updates the listView, which shows all friends
	 */
	public void updateFriendsList() {
		ListView listView = (ListView) findViewById(R.id.FriendView);
		listView.setAdapter(friendsAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		drawMenu.closeDrawers();
		// Save friends here
		if (friendsAdapter.getCount() > 0) {
			for (int i = 0; i < friendsAdapter.getCount(); i++) {
				// TODO paste your real number here (instead of 1234)
				ShareListRequest slrequest = new ShareListRequest("" + 796897,
						"" + friendsAdapter.getItem(i).getPhoneNr(),
						list.getId());
				this.syncManager.addRequest(slrequest);
			}
			this.syncManager.synchronise();
		}
	}

	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, listToString());
		return shareIntent;

	}

	private String listToString() {
		List<Item> items = manager.getItemsFor(list);
		StringBuilder sb = new StringBuilder();
		sb.append(list).append("\n");
		for (Item item : items) {
			sb.append(item).append("\n");
		}

		return sb.toString();
	}
}
