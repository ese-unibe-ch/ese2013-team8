package ch.unibe.ese.shoppinglist;

import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.ese.core.JsonPersistenceManager;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.core.SlideMenu;

public class HomeActivity extends Activity {

	private ListManager manager;
	private ArrayAdapter<ShoppingList> shoppingListAdapter;
	private SlideMenu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Put content into the slideMenu and add it to the view
		menu = new SlideMenu(getApplicationContext(), R.layout.activity_home);
		setContentView(menu);
		
		manager = new ListManager(new JsonPersistenceManager(getApplicationContext()));
		
		// Get List from manager
		List<ShoppingList> shoppingLists = manager.getShoppingLists();
		
		shoppingListAdapter = new ArrayAdapter<ShoppingList>(this, 
		        R.layout.shopping_list_item, shoppingLists);
		
		ListView listView = (ListView) menu.getContent().findViewById(R.id.ShoppingListView);
		listView.setAdapter(shoppingListAdapter);
		
		// Add long click Listener
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ShoppingList selectedList = shoppingListAdapter.getItem(arg2);
				HomeActivity.this.startActionMode(
						new ShoppingListActionMode(HomeActivity.this.manager,selectedList, HomeActivity.this.shoppingListAdapter, HomeActivity.this)
						);
				return false;
			}
		});	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_new:
	        	Intent intent = new Intent(this, CreateListActivity.class);
	            this.startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		manager.persist();
	}
	
	public void onBackPressed() {
		if(menu.menuOpen())
			menu.toggleMenu();
		else
			super.onBackPressed();
	}
}
