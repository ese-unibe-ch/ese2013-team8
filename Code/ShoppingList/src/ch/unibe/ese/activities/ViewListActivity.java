package ch.unibe.ese.activities;

import java.util.ArrayList;
import java.util.List;

import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.core.sqlite.SQLiteItemAdapter;
import ch.unibe.ese.shoppinglist.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.shoppinglist.R;

public class ViewListActivity extends BaseActivity {

	private ListManager manager;
	private ArrayAdapter<Item> itemAdapter;
	private ArrayAdapter<Item> itemBoughtAdapter;
	private Activity viewListActivity = this;
	private ShoppingList list;
	private int listIndex;
	private boolean longClick = false;
	List<Item> items;
	List<Item> itemsBought;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_list);
		// Show the Up button in the action bar.
		setupActionBar();
		
		manager = getListManager();
		items = new ArrayList<Item>();
		itemsBought = new ArrayList<Item>();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			listIndex = extras.getInt("selectedList");
			list = manager.getShoppingLists().get(listIndex);
			setTitle(list.getName());
		}
		
		// Get list items	
		items = manager.getItemsFor(list);
		
		if (items != null) {
			itemAdapter = new ArrayAdapter<Item>(this, 
			        R.layout.shopping_list_item, items);
			
			ListView listView = (ListView) findViewById(R.id.ItemView);
			listView.setAdapter(itemAdapter);
		
		// Add long click Listener
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				longClick = true; // make sure that the list doesn't open on long click
				Item selectedItem = itemAdapter.getItem(arg2);
				ViewListActivity.this.startActionMode(
						new ShoppingListActionMode(ViewListActivity.this.manager, selectedItem, list, ViewListActivity.this.itemAdapter, ViewListActivity.this)
						);
				return false;
			}
		});
		
		// Add click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!longClick) {
					// bought items
					itemsBought.add(items.remove(position));
					itemBoughtAdapter = new ArrayAdapter<Item>(viewListActivity, 
					        R.layout.shopping_list_item, itemsBought);
					// TODO: add striketrough to bought items
					// TODO: set scrollbar on whole activity, not on listViews
					
					ListView listViewBought = (ListView) findViewById(R.id.ItemBoughtView);
					listViewBought.setAdapter(itemBoughtAdapter);
				}
				longClick = false;
			}
		});
			
		}
		
		// Autocompletion
		AutoCompleteTextView textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
		SQLiteItemAdapter sqliteAdapter = new SQLiteItemAdapter(this, android.R.layout.simple_list_item_1);
		textName.setAdapter(sqliteAdapter);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	/** Called when the user touches the add item button */
	public void addItemEdit(View view) {
	  	Intent intent = new Intent(this, CreateItemActivity.class);
	  	EditText textName = (EditText) findViewById(R.id.editTextName);
		String name = textName.getText().toString();
    	intent.putExtra("Item", name);
    	intent.putExtra("selectedList", listIndex);
        this.startActivity(intent);
	}
	
	/** Called when the user touches the ok button */
	public void addItem(View view) {
	  	
		EditText textName = (EditText) findViewById(R.id.editTextName);
		String name = textName.getText().toString();
		if (name.trim().length() == 0) {
			Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT)
				.show();
		}
		else {
			try {
				Item item = new Item(name);
				manager.addItemToList(item, list);
				
				// refresh view
				items = manager.getItemsFor(list);
				itemAdapter = new ArrayAdapter<Item>(this, 
				        R.layout.shopping_list_item, items);
				ListView listView = (ListView) findViewById(R.id.ItemView);
				listView.setAdapter(itemAdapter);
				
				// remove text from field
				textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
				textName.setText("");
	
			} catch (IllegalStateException e) {
				Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT)
					.show();
			}
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_list, menu);
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

	@Override
	protected void onPause() {
		super.onPause();
	}
}
