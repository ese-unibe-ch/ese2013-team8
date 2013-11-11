package ch.unibe.ese.shopnote.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.adapters.FriendsListAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.drawer.NavigationDrawer;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.FriendRequest;

/**
 * Creates a frame which enlists all friends and the possibility to manage them
 * @author ESE Team 8
 *
 */
public class ManageFriendsActivity extends BaseActivity {
	
	private static final int PICK_CONTACT = 2;
	private FriendsManager friendsManager;
	private SyncManager syncManager;
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
		syncManager = getSyncManager();
		
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
	 * Updates the Viewlist, which shows all friends and adds itemclicklistener
	 */
	public void updateFriendsList(){
		friendsAdapter = new FriendsListAdapter(this,
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
//			Intent intent = new Intent(this, CreateFriendActivity.class);
//			this.startActivity(intent);
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
			
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
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK && requestCode == PICK_CONTACT) 
				addChosenFriend(data);
	}

	/**
	 * Extracts the Contact from data and adds it to the db
	 * @param data
	 */
	private void addChosenFriend(Intent data) {
		Uri contactData = data.getData();
		Cursor cursor = managedQuery(contactData, null, null, null, null);
	     
		 if (cursor.moveToFirst()) {
			  String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
			  String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
	
			  if (hasPhone.equalsIgnoreCase("1")) {
				  Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
						  ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
				  phones.moveToFirst();
				  String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				  String cName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				  
				Friend friend = new Friend(cNumber, cName);
				friendsManager.addFriend(friend);
			  }	
	     }
	}

	@Override
	public void refresh() {
		updateFriendsList();
	}
}
