package ch.unibe.ese.activities;

import java.util.ArrayList;

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
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Friend;
import ch.unibe.ese.core.FriendsManager;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.share.SyncManager;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.sidelist.NavigationDrawer;

public class ShareListActivity extends BaseActivity {

	private ListManager manager;
	private FriendsManager friendsManager;
	private SyncManager syncManager;
	private ArrayAdapter<Friend> friendsAdapter;
	private ArrayAdapter<Friend> autocompleteAdapter;
	private ShoppingList list;
	private DrawerLayout drawMenu;
	
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
		
		// Create drawer menu
		NavigationDrawer nDrawer = new NavigationDrawer();
		drawMenu = nDrawer.constructNavigationDrawer(drawMenu, this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			long listIndex = extras.getLong("selectedList");
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
		intent.putExtra("Friend", name);
		startActivityForResult(intent, 0);

		setTextViewText(R.id.editTextName, "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Navigate back to the list
			finish();
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
		int phoneNr = -1;

		if (resultCode == RESULT_OK) {
			Bundle korb = data.getExtras();
			phoneNr = korb.getInt("phoneNr");
		}

		if (phoneNr != -1) {
			Friend friend = friendsManager.getFriendFromNr(phoneNr);
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
	}
	
	@Override
	public void onBackPressed() {
		this.syncManager.synchronise();
	}
}
