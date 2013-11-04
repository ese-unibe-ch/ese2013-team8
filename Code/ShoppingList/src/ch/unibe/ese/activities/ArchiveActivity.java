package ch.unibe.ese.activities;

import java.util.ArrayList;
import java.util.List;

import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.shoppinglist.R.layout;
import ch.unibe.ese.shoppinglist.R.menu;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class ArchiveActivity extends BaseActivity {
	
	private ListManager manager;
	private ArrayAdapter<ShoppingList> listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		// Show the Up button in the action bar.
		setupActionBar();
		
		manager = getListManager();
		
		updateAdapter();
	}
	
	/**
	 * Updates all adapters 
	 */
	public void updateAdapter() {
		// display items
		List<ShoppingList> shoppingLists = manager.getShoppingLists();
		List<ShoppingList> shoppingListsArchived = new ArrayList<ShoppingList>();
		
		// separate archived lists
		for (ShoppingList list: shoppingLists)
			if (list.isArchived())
				shoppingListsArchived.add(list);
		
		listAdapter = new ArrayAdapter<ShoppingList>(this, R.layout.shopping_list_item, shoppingListsArchived);
		ListView listView = (ListView) findViewById(R.id.ListView);
		listView.setAdapter(listAdapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.archive, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		}
		return super.onOptionsItemSelected(item);
	}

}
