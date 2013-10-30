package ch.unibe.ese.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.FriendsManager;
import ch.unibe.ese.shoppinglist.R;

public class CreateFriendsActivity extends BaseActivity {
	FriendsManager friendsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_friends);
		
		friendsManager = getFriendsManager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_friends, menu);
		return true;
	}
	
	/**
	 * Reads the input and tries to creates a friend with it
	 * @param View v
	 */
	public void addEntryToList(View w) {
		try{
			EditText friendName = (EditText) findViewById(R.id.edit_friend_name);
			String name = friendName.getText().toString();

			EditText friendNr = (EditText) findViewById(R.id.edit_friend_phone_number);
			int nr = Integer.parseInt(friendNr.getText().toString());
	
			int processStatus = friendsManager.addFriend(nr, name);
			if (processStatus == 0){
				friendName.setText("");
				friendNr.setText("");
				
				finish();
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
	
	public void goBack(View view) {
		finish();
	}

}
