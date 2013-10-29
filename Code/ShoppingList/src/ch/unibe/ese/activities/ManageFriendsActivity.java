package ch.unibe.ese.activities;

import android.os.Bundle;
import android.view.Menu;
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

		friendsManager = getFriendsManager();

		updateFriendsList();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_friends, menu);
		return true;
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

	/**
	 * Reads the input and tries to creates a friend with it
	 * @param View v
	 */
	public void addEntryToList(View w) {
		try{
			EditText friendName = (EditText) findViewById(R.id.friends_name_edit);
			String name = friendName.getText().toString();

			EditText friendNr = (EditText) findViewById(R.id.friends_nr_edit);
			int nr = Integer.parseInt(friendNr.getText().toString());
	
			int processStatus = friendsManager.addFriend(nr, name);
			if (processStatus == 0){
				friendName.setText("");
				friendNr.setText("");
	
				updateFriendsList();
			} else 
				printFailure(processStatus);
		} catch(Exception e){
			Toast.makeText(this, this.getString(R.string.error_enter), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * prints the failure created by adding a friend
	 * @param failureNumber
	 */
	private void printFailure(int failure){
		switch(failure){
		case 1: 
			Toast.makeText(this, this.getString(R.string.error_friend_already), Toast.LENGTH_SHORT).show();
			break;
			
		case 2: 
			Toast.makeText(this, this.getString(R.string.error_name), Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
