package ch.unibe.ese.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ch.unibe.ese.core.Friend;
import ch.unibe.ese.core.FriendsManager;
import ch.unibe.ese.shoppinglist.R;

public class ManageFriendsActivity extends Activity {
	FriendsManager friendsManager;
	List<Friend> friendsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_friends);
		
		friendsManager = new FriendsManager();
		friendsList = friendsManager.getFriendsList();
		
		ArrayAdapter<Friend> friendsAdapter = new ArrayAdapter<Friend>(this, 
		        R.layout.shopping_list_item, friendsList );
		
		ListView listView = (ListView) findViewById(R.id.friends_list);
		listView.setAdapter(friendsAdapter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_friends, menu);
		return true;
	}
	
	public void save(View w){
		EditText friendName = (EditText) findViewById(R.id.friends_name_edit);
		String name = friendName.getText().toString();
		
		EditText friendNr = (EditText) findViewById(R.id.friends_nr_edit);
		int nr = Integer.parseInt(friendNr.getText().toString());
		
		if(!friendsManager.addFriend(nr, name))
			Toast.makeText(this, "Error occured, friend not found", Toast.LENGTH_SHORT)
			.show();
		
		friendName.setText("");
		friendNr.setText("");
		
		//TODO: durch schoenere Variante ersetzen
		friendsList = friendsManager.getFriendsList();
		ArrayAdapter<Friend> friendsAdapter = new ArrayAdapter<Friend>(this, 
		        R.layout.shopping_list_item, friendsList );
		
		ListView listView = (ListView) findViewById(R.id.friends_list);
		listView.setAdapter(friendsAdapter);
		
	}

}
