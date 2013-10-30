package ch.unibe.ese.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Friend;
import ch.unibe.ese.core.FriendsManager;
import ch.unibe.ese.shoppinglist.R;

public class ManageFriendsActivity extends BaseActivity {
	FriendsManager friendsManager;

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

		updateFriendsList();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	/**
	 * Updates the Viewlist, which shows all friends
	 */
	public void updateFriendsList(){
		ArrayAdapter<Friend> friendsAdapter = new ArrayAdapter<Friend>(this,
				R.layout.shopping_list_item, friendsManager.getFriendsList());

		ListView listView = (ListView) findViewById(R.id.friends_list);
		listView.setAdapter(friendsAdapter);	
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
