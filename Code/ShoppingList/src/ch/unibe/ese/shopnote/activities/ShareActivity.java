package ch.unibe.ese.shopnote.activities;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.adapters.FriendsListAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.Recipe;
import ch.unibe.ese.shopnote.core.ShoppingList;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.GetSharedFriendsRequest;
import ch.unibe.ese.shopnote.share.requests.ShareListRequest;

/**
 *	Allows to share/synchronize lists/recipes with friends or send them as text message
 */
@SuppressLint("NewApi")
public class ShareActivity extends BaseActivity {

	private ListManager listManager;
	private FriendsManager friendsManager;
	private SyncManager syncManager;
	private ArrayAdapter<Friend> autocompleteAdapter;
	private FriendsListAdapter adapter;
	private ShoppingList list;
	private Recipe recipe;
	private boolean isRecipe;
	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_list);
      
		// set chosen color theme
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.RelativeLayoutShareList);
		RelativeLayout rlDrawer = (RelativeLayout) findViewById(R.id.drawer_Linearlayout);
		updateTheme(lo, getActionBar(), rlDrawer);
		LinearLayout lladdItem = (LinearLayout) findViewById(R.id.addFriend);
		updateThemeTextBox(lladdItem);
		
		// show the up button in the action bar.
		getActionBar();

		// get managers
		listManager = getListManager();
		friendsManager = getFriendsManager();
		syncManager = getSyncManager();
		
		// verify phone number
		getMyPhoneNumber();

		// create drawer menu
		createDrawerMenu();
		createDrawerToggle(); // to change the title
		drawerToggle.setDrawerIndicatorEnabled(false);

		// get extras
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isRecipe = extras.getBoolean(EXTRAS_IS_RECIPE);
			// Get list
			if (!isRecipe) {
				long listIndex = extras.getLong(EXTRAS_LIST_ID);
				list = listManager.getShoppingList(listIndex);
				setTitle(this.getString(R.string.share_list_title) + " "
						+ list.getName());
			}
			// Get recipe
			else {
				long recipeIndex = extras.getLong(EXTRAS_RECIPE_ID);
				recipe = listManager.getRecipeAt(recipeIndex);
				setTitle(this.getString(R.string.share_list_title) + " "
						+ this.getString(R.string.view_recipe_title) + " " + recipe.toString());
			}
		}

		// get Shared Friends
		getSharedFriends();
		
		// create autocompletion
		createAutocomplete();
	}

	/**
	 *	Creates or updates the autocreation textfield
	 */
	private void createAutocomplete() {
		AutoCompleteTextView textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
		autocompleteAdapter = new ArrayAdapter<Friend>(this,
				android.R.layout.simple_list_item_1,
				friendsManager.getFriendsWithApp());
		textName.setAdapter(autocompleteAdapter);
		updateThemeTextBox(textName);

		// autocompletion click listener
		textName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				Friend friend = (Friend) autocompleteAdapter.getItem(position);
				// share list
				if (!isRecipe) {
					friendsManager.addFriendToList(list, friend);
					updateFriendsList();
					ShareListRequest slrequest = new ShareListRequest(getMyPhoneNumber(),
							friend.getPhoneNr(), list.getId(), list.getName());
					syncManager.addRequest(slrequest);			
				}
				// share recipe
				else {
					friendsManager.addFriendToRecipe(recipe, friend);
					updateFriendsList();
					// TODO: ShareRecipeRequest		
				}
				setTextViewText(R.id.editTextName, "");
			}
		});
	}

	/** Called when the user touches the create button */
	public void addFriend(View view) {
		Intent intent = new Intent(this, CreateFriendActivity.class);
		startActivityForResult(intent, 1);

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
			// Try to sync with server
			syncManager.synchronise(this);
			
			// Set the list on shared if there's an entry in the list
			if (!isRecipe) {
				list.setShared(!adapter.isEmpty());
				listManager.saveShoppingList(list);
			}
			
			// Set the recipe on shared
			else {
				recipe.setShared(!adapter.isEmpty());
				listManager.saveRecipe(recipe);
			}
				
			// Navigate back to the list/recipe
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
	 * Updates the listView, which shows all friends
	 */
	public void updateFriendsList() {
		ListView listView = (ListView) findViewById(R.id.FriendView);
		if (!isRecipe) {
			adapter = new FriendsListAdapter(this,
					android.R.layout.simple_list_item_1,
					friendsManager.getSharedFriends(list));
		}
		else {
			adapter = new FriendsListAdapter(this,
					android.R.layout.simple_list_item_1,
					friendsManager.getSharedFriends(recipe));
		}
		listView.setAdapter(adapter);
		updateThemeListView(listView);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Friend friend = adapter.getItem(position);	
				if (!isRecipe) {
					ShareActivity.this.startActionMode(new ShareActionMode(
									ShareActivity.this.friendsManager, friend,
									list, ShareActivity.this));
				}
				else {
					ShareActivity.this.startActionMode(new ShareActionMode(
							ShareActivity.this.friendsManager, friend,
							recipe, ShareActivity.this));
				}

				setTextViewText(R.id.editTextName, "");
				return true;
			}
		});
	}

	private void getSharedFriends() {
		if(isRecipe) {
			if(recipe.isShared()) {
				// Get friends here
			}
		} else if(list.isShared()) {
				GetSharedFriendsRequest gsfRequest = new GetSharedFriendsRequest(getMyPhoneNumber(), list.getId());
				syncManager.addRequest(gsfRequest);
				syncManager.synchronise(this);
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
		if (!isRecipe)
			shareIntent.putExtra(Intent.EXTRA_TEXT, listToString());
		else
			shareIntent.putExtra(Intent.EXTRA_TEXT, recipeToString());
		return shareIntent;

	}

	private String listToString() {
		List<Item> items = listManager.getItemsFor(list);
		StringBuilder sb = new StringBuilder();
		sb.append(list).append("\n");
		for (Item item : items) {
			sb.append("- ").append(item).append("\n");
		}

		return sb.toString();
	}
	
	private String recipeToString() {
		List<Item> items = recipe.getItemList();
		StringBuilder sb = new StringBuilder();
		sb.append(this.getString(R.string.view_recipe_title) + " ").append(recipe).append(":").append("\n");
		for (Item item : items) {
			sb.append("- ").append(item).append("\n");
		}

		return sb.toString();
	}
	
	@Override
	public void refresh() {
		this.updateFriendsList();
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
			final Friend friend = friendsManager.getFriend(friendId);
			final SynchHandler handler = new SynchHandler(this);
			if(!friend.hasTheApp())
				Toast.makeText(getApplicationContext(), R.string.checkIfFriendHasApp, Toast.LENGTH_SHORT ).show();
				
			new Thread(new Runnable() {
				public void run() {	
					for(int i=0; i<5; i++)
						if(!friend.hasTheApp())
							waitForMillisecs(1000);
					if(friend.hasTheApp()) {
						if (!isRecipe) {
							friendsManager.addFriendToList(list, friend);
							ShareListRequest slrequest = new ShareListRequest(getMyPhoneNumber(),
									friend.getPhoneNr(), list.getId(), list.getName());
							syncManager.addRequest(slrequest);
						}
						else {
							friendsManager.addFriendToRecipe(recipe, friend);
							// TODO: ShareRecipeRequest
						}
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				}

				private void waitForMillisecs(int number) {
					try {
						Thread.sleep(number);
					} catch (InterruptedException e) {
						System.err.println("Thread sleep interrupted");
					}
				}
			}).start();
			
				
		}
		
		// update lists
		updateFriendsList();
		createAutocomplete();
	}
}
