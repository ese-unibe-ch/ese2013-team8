package ch.unibe.ese.shoppinglist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.unibe.ese.core.JsonPersistenceManager;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends Activity {

	private ListManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		manager = new ListManager(new JsonPersistenceManager(getApplicationContext()));
		
		// Test: (shouldn't be here)
		// Add item to list
		manager.addShoppingList(new ShoppingList("Groceries"));
		manager.addShoppingList(new ShoppingList("Electronics"));
		manager.addShoppingList(new ShoppingList("Stuff"));
		// End of Test
		
		// Get List from manager
		List<ShoppingList> shoppingLists = manager.getShoppingLists();
		
		ArrayAdapter<ShoppingList> shoppingListAdapter = new ArrayAdapter<ShoppingList>(this, 
		        android.R.layout.simple_list_item_1, shoppingLists);
		
		ListView listView = (ListView) findViewById(R.id.ShoppingListView);
		listView.setAdapter(shoppingListAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		manager.persist();
	}
}
