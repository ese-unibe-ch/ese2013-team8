package ch.unibe.ese.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Friend;
import ch.unibe.ese.core.FriendsManager;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.sidelist.NavigationDrawer;

/**
 * Creates a frame which enlists all friends and the possibility to manage them
 * @author ESE Team 8
 *
 */

public class ManageFriendsActivity extends BaseActivity {
	
	private FriendsManager friendsManager;
	private ArrayAdapter<Friend> friendsAdapter;
	private DrawerLayout drawMenu;

	@Override
	/**
	 * initializes the friendslist and loads all friends.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_friends);
		// Show the Up button in the action bar.
		setupActionBar();

		friendsManager = getFriendsManager();
		
		// Create drawer menu
		NavigationDrawer nDrawer = new NavigationDrawer();
		drawMenu = nDrawer.constructNavigationDrawer(drawMenu, this);

		updateFriendsList();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	/**
	 * Fix to close the drawer menu on back button press
	 */
	@Override       
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
	}
	
	/**
	 * Updates the Viewlist, which shows all friends and adds itemclicklistener
	 */
	public void updateFriendsList(){
		friendsAdapter = new ArrayAdapter<Friend>(this,
				R.layout.shopping_list_item, friendsManager.getFriendsList());

		ListView listView = (ListView) findViewById(R.id.friends_list);
		listView.setAdapter(friendsAdapter);
		
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
		updateFriendsList();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_friends, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_new:
			Intent intent = new Intent(this, CreateFriendActivity.class);
			this.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
