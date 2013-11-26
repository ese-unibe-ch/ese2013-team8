package ch.unibe.ese.shopnote.activities;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import ch.unibe.ese.shopnote.adapters.ItemListAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.R;

/**
 * 	Displays all Items in the database
 */
public class ItemListActivity extends BaseActivity {
	
	private ListManager manager;
	private List<Item> itemList;
	private ArrayAdapter<Item> itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		//set chosen color theme
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.RelativeLayoutItemList);
		updateTheme(lo);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		//get manager and all items 
		manager = getListManager();
		
		// Create drawer menu
		createDrawerMenu();
		createDrawerToggle();
	
		updateAdapter();

		setTitle(this.getString(R.string.title_activity_item_list));
	}
	
	/**
	 * Updates all adapters 
	 */
	public void updateAdapter() {
		// display items
		itemList = manager.getAllItems();
		itemAdapter = new ItemListAdapter(this, R.layout.shopping_list_item, itemList);
		ListView listView = (ListView) findViewById(R.id.ItemView);
		listView.setAdapter(itemAdapter);
		
		setItemLongClick(listView);
	}

	private void setItemLongClick(ListView listView) {
		//sets clicklistener
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Item selectedItem = itemAdapter.getItem(arg2);
				ItemListActivity.this.startActionMode(new ShoppingListActionMode(
						ItemListActivity.this.manager, selectedItem, ItemListActivity.this.itemAdapter, ItemListActivity.this));
				return true;
			}
		});
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
		getMenuInflater().inflate(R.menu.item_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item))
        	return true;
        
		switch (item.getItemId()) {		
			case R.id.action_new:
				Intent intent = new Intent(this, CreateItemActivity.class);
				this.startActivityForResult(intent, 1);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Updates the Adapters to get the newest list when returning to activity
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) 
				updateAdapter(); 
	}
	
	public void onResume(){
    	super.onResume();
    	updateAdapter();
    }
}
