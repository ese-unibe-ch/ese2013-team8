package ch.unibe.ese.activities;

import java.util.ArrayList;
import java.util.List;

import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.sidelist.NavigationDrawer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;

public class ArchiveActivity extends BaseActivity {
	
	private ListManager manager;
	private ArrayAdapter<ShoppingList> listAdapter;
	List<ShoppingList> shoppingLists;
	List<ShoppingList> shoppingListsArchived;
	private Activity archiveActivity = this;
	private DrawerLayout drawMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		// Show the Up button in the action bar.
		setupActionBar();
		
		manager = getListManager();
		
		// Create drawer menu
		NavigationDrawer nDrawer = new NavigationDrawer();
		drawMenu = nDrawer.constructNavigationDrawer(drawMenu, this);
		
		updateAdapter();
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
		
		listAdapter = new ArrayAdapter<ShoppingList>(this, R.layout.shopping_list_item, shoppingListsArchived);
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
				int listPosition = manager.getShoppingLists().indexOf(list);
				
				intent.putExtra("selectedList", listPosition);
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
