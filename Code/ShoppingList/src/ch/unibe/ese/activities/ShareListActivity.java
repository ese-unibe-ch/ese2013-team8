package ch.unibe.ese.activities;

import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Friend;
import ch.unibe.ese.core.FriendsManager;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.core.sqlite.SQLiteItemAdapter;
import ch.unibe.ese.shoppinglist.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ShareListActivity extends BaseActivity {

	private ListManager manager;
	private FriendsManager friendsManager;
	private ArrayAdapter<Friend> friendsAdapter;
	private ShoppingList list;
	private int listIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_list);
		// Show the Up button in the action bar.
		getActionBar();
		
		manager = getListManager();
		friendsManager = getFriendsManager();
		

		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			listIndex = extras.getInt("selectedList");
			list = manager.getShoppingLists().get(listIndex);
			setTitle(this.getString(R.string.share_list_title) + " " + list.getName());
		}
		
		// Autocompletion
		AutoCompleteTextView textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
		ArrayAdapter<Friend> adapter = new ArrayAdapter<Friend>(this,
				android.R.layout.simple_list_item_1, friendsManager.getFriendsList());
		textName.setAdapter(adapter);
		
		// Autocompletion click listener
		textName.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View arg1, int position,
	                long id) {
	        	// TODO: fix bugs
	        	Friend friend = friendsManager.getFriendsList().get(position);
	        	friendsAdapter.add(friend);
	        }
	    });

	}
	
	/** Called when the user touches the create button */
	public void addFriend(View view) {
		EditText textName = (EditText) findViewById(R.id.editTextName);
		String name = textName.getText().toString();
		
		Intent intent = new Intent(this, CreateFriendActivity.class);
		intent.putExtra("Friend", name);
		this.startActivity(intent);
		
		textName.setText("");
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
	 * Updates the listView, which shows all friends
	 */
	public void updateFriendsList(){
    	friendsAdapter = new ArrayAdapter<Friend>(this, 
    			R.layout.shopping_list_item, friendsManager.getFriendsList());

		ListView listView = (ListView) findViewById(R.id.FriendView);
		listView.setAdapter(friendsAdapter);	
	}
}
