package ch.unibe.ese.shopnote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.FriendRequest;
import ch.unibe.ese.shopnote.R;

/**
 * Creates a frame to create new friends or edit them if the intent has a extra
 * 
 * @author ESE Team 8
 * 
 */
public class CreateFriendActivity extends BaseActivity {
	private FriendsManager friendsManager;
	private Friend friend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_friend);
		getActionBar().hide();

		friendsManager = getFriendsManager();

		// check if edit friend
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			editFriend(extras);
		}
	}

	/**
	 * Allows to edit friend by adding the data of the friend to the textviews
	 * 
	 * @param extras
	 */
	private void editFriend(Bundle extras) {

		// get friend
		long friendId = extras.getLong(EXTRAS_FRIEND_ID);
		this.friend = friendsManager.getFriend(friendId);

		if (friend != null) {
			// set name of friend
			setTextViewText(R.id.edit_friend_name, friend.getName());

			// set phoneNr but uneditable
			EditText friendNr = (EditText) findViewById(R.id.edit_friend_phone_number);
			friendNr.setText("" + friend.getPhoneNr());
			friendNr.setEnabled(false);
		} else {
			String name = extras.getString(EXTRAS_FRIEND_NAME);
			setTextViewText(R.id.edit_friend_name, name);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_friends, menu);
		return true;
	}

	/**
	 * Reads the input and tries to creates a friend with it or edits an old one
	 * 
	 * @param View
	 *            v
	 */
	public void addEntryToList(View w) {
		try {
			String name = getTextViewText(R.id.edit_friend_name);
			int nr = Integer.parseInt(getTextViewText(R.id.edit_friend_phone_number));

			SyncManager syncmanager = this.getSyncManager();
			// Ask server if friend has the app
			FriendRequest frequest = new FriendRequest("" + nr);
			syncmanager.addRequest(frequest);
			syncmanager.synchronise();

			if (friend == null)
				addNewFriend(name, nr);
			else {
				friend.setName(name);
				friend.setPhoneNr(nr);
				friendsManager.update(friend);
				finish();
			}

		} catch (Exception e) {
			Toast.makeText(this, this.getString(R.string.error_enter),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void addNewFriend(String name, int nr) {
		Friend friend = new Friend(nr, name);
		int processStatus = friendsManager.addFriend(friend);
		if (processStatus == 0) {
			finishTheActivity(friend.getId());
		} else
			printFailure(processStatus);
	}

	/**
	 * prints the failure created by adding a friend
	 * 
	 * @param failureNumber
	 */
	private void printFailure(int failure) {
		switch (failure) {
		case 1:
			Toast.makeText(this, this.getString(R.string.error_friend_already),
					Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(this, this.getString(R.string.error_name),
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	public void goBack(View view) {
		finish();
	}

	/**
	 * finishs the program with no result when called from ManageFriendsActivity
	 * or with result when called by ShareListActivity
	 */
	private void finishTheActivity(long id) {
		if (getIntent().getExtras() != null) {
			Intent intent = new Intent();
			intent.putExtra(EXTRAS_FRIEND_ID, id);
			setResult(RESULT_OK, intent);
		}
		finish();
	}
}
