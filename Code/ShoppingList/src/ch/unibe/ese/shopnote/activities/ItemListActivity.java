package ch.unibe.ese.shopnote.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.adapters.ItemAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ListManager;

/**
 * 	Displays all Items in the database
 */
public class ItemListActivity extends BaseActivity {
	
	private ListManager manager;
	private List<Item> itemList;
	private ArrayAdapter<Item> itemAdapter;
	private Activity activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		//set chosen color theme
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.RelativeLayoutItemList);
		RelativeLayout rlDrawer = (RelativeLayout) findViewById(R.id.drawer_Linearlayout);
		updateTheme(lo, getActionBar(), rlDrawer);
		
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
		itemAdapter = new ItemAdapter(this, R.layout.shopping_list_item, itemList);
		ListView listView = (ListView) findViewById(R.id.ItemView);
		listView.setAdapter(itemAdapter);
		updateThemeListView(listView);
		
		setItemLongClick(listView);
		
		// hide welcome message
		RelativeLayout welcome = (RelativeLayout) findViewById(R.id.item_list_welcome);
		if (itemAdapter.isEmpty())
			welcome.setVisibility(View.VISIBLE);
		else
			welcome.setVisibility(View.GONE);
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
		
		// Add click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Item item = itemAdapter.getItem(position);
				Intent intent = new Intent(activity, CreateItemActivity.class);
				intent.putExtra(BaseActivity.EXTRAS_ITEM_ID, item.getId());
				intent.putExtra(BaseActivity.EXTRAS_ITEM_EDIT, true);
				activity.startActivityForResult(intent, 1);
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
