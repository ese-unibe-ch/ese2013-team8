package ch.unibe.ese.shopnote.activities;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.adapters.FriendsListAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.FriendRequest;
import ch.unibe.ese.shopnote.share.requests.RegisterRequest;

/**
 *	Creates a frame which enlists all friends and the possibility to manage them
 */
public class ManageFriendsActivity extends BaseActivity {
	
	private FriendsManager friendsManager;
	private SyncManager syncManager;
	private ArrayAdapter<Friend> friendsAdapter;

	@Override
	/**
	 * initializes the friendslist and loads all friends.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_friends);
        
		//set chosen color theme
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.RelativeLayoutManageFriends);
		RelativeLayout rlDrawer = (RelativeLayout) findViewById(R.id.drawer_Linearlayout);
		updateTheme(lo, getActionBar(), rlDrawer);
		
		// Show the Up button in the action bar.
		setupActionBar();

		friendsManager = getFriendsManager();
		syncManager = getSyncManager();
		
		// verify phone number
		getMyPhoneNumber();
		
		// Create drawer menu
		createDrawerMenu();
		createDrawerToggle();
		
		updateFriendsList();
		
		setTitle(this.getString(R.string.title_activity_manage_friends));
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	/**
	 * Updates the Viewlist, which shows all friends and adds itemclicklistener
	 */
	public void updateFriendsList(){
		List<Friend> friendsList = new ArrayList<Friend>(friendsManager.getFriendsList());
		friendsAdapter = new FriendsListAdapter(this,
				R.layout.shopping_list_item, friendsList);

		ListView listView = (ListView) findViewById(R.id.friends_list);
		listView.setAdapter(friendsAdapter);
		updateThemeListView(listView);
		
		// hide welcome message
		RelativeLayout welcome = (RelativeLayout) findViewById(R.id.manage_friends_welcome);
		if (friendsAdapter.isEmpty())
			welcome.setVisibility(View.VISIBLE);
		else
			welcome.setVisibility(View.GONE);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Friend selectedFriend = friendsAdapter.getItem(position);
				ManageFriendsActivity.this.startActionMode(new FriendListActionMode(
						friendsManager, friendsAdapter, selectedFriend, ManageFriendsActivity.this));
				return true;
			}
		});
	}
	
	protected void onResume() {
		super.onResume();
		for(Friend f: friendsManager.getFriendsList()) {
			FriendRequest fr = new FriendRequest(f);
			syncManager.addRequest(fr);
		}
		syncManager.synchronise(this);
		updateFriendsList();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_friends, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Pass the event to ActionBarDrawerToggle, if it returns
	    // true, then it has handled the app icon touch event
	    if (drawerToggle.onOptionsItemSelected(item))
	    	return true;
	    
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_new:
			Intent intent = new Intent(this, CreateFriendActivity.class);
			this.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
    	drawMenu.closeDrawers();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		updateFriendsList();
	}

	@Override
	public void refresh() {
		updateFriendsList();
	}
}
