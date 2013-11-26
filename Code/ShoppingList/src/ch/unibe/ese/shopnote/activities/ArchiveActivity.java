package ch.unibe.ese.shopnote.activities;

import java.util.ArrayList;
import java.util.List;
import ch.unibe.ese.shopnote.adapters.ShoppingListAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.ShoppingList;
import ch.unibe.ese.shopnote.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 *	Displays the archived shopping lists
 */
public class ArchiveActivity extends BaseActivity {
	
	private ListManager manager;
	private ShoppingListAdapter listAdapter;
	private List<ShoppingList> shoppingLists;
	private List<ShoppingList> shoppingListsArchived;
	private Activity archiveActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
      
		//set chosen color theme
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.RelativeLayoutArchive);
		updateTheme(lo);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		manager = getListManager();
		
		// Create drawer menu
		createDrawerMenu();
		createDrawerToggle();
		
		updateAdapter();
		
		setTitle(this.getString(R.string.title_activity_archive));
	}
	
	/**
	 * Updates all adapters 
	 */
	public void updateAdapter() {
		// display items
		shoppingLists = manager.getShoppingLists();
		shoppingListsArchived = new ArrayList<ShoppingList>();
		
		// separate archived lists
		for (ShoppingList list: shoppingLists)
			if (list.isArchived())
				shoppingListsArchived.add(list);
		
		listAdapter = new ShoppingListAdapter(this, R.layout.shopping_list_item, shoppingListsArchived);
		
		// calculate boughtItems/totalItems count
		calculateItemCount(shoppingListsArchived, listAdapter);
		
		ListView listView = (ListView) findViewById(R.id.ListView);	
		listView.setAdapter(listAdapter);
		
		addListener(listView);
	}
	
	private void addListener(ListView listView) {
		// Add long click Listener
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			// TODO: Merge those 2 Listeners together, because we don't need two
			// seperate listeners...
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ShoppingList selectedList = listAdapter.getItem(arg2);
				ArchiveActivity.this.startActionMode(new ShoppingListActionMode(
						ArchiveActivity.this.manager, selectedList,
						ArchiveActivity.this.listAdapter,
						ArchiveActivity.this));
				return true;
			}
		});

		// Add click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(archiveActivity, ViewListActivity.class);
				
				// get position in listmanager
				ShoppingList list = shoppingListsArchived.get(position);
				
				intent.putExtra(EXTRAS_LIST_ID, list.getId());
				archiveActivity.startActivity(intent);
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
		getMenuInflater().inflate(R.menu.archive, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Pass the event to ActionBarDrawerToggle, if it returns
	    // true, then it has handled the app icon touch event
	    if (drawerToggle.onOptionsItemSelected(item))
	    	return true;
	    
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
    	drawMenu.closeDrawers();
	}
}
