package ch.unibe.ese.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.share.SyncManager;
import ch.unibe.ese.share.requests.FriendRequest;
import ch.unibe.ese.share.requests.RegisterRequest;
import ch.unibe.ese.share.requests.Request;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.sidelist.NavigationDrawer;

public class HomeActivity extends BaseActivity {

	private ListManager listmanager;
	private SyncManager syncmanager;
	private List<ShoppingList> shoppingLists;
	private List<ShoppingList> shoppingListsNotArchived;
	private ArrayAdapter<ShoppingList> shoppingListAdapter;
	private Activity homeActivity = this;
	private DrawerLayout drawMenu;
	private ActionBarDrawerToggle drawerToggle;

	@SuppressLint("NewApi") // TODO: check "problem"
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// Create drawer menu
		NavigationDrawer nDrawer = new NavigationDrawer();
		drawMenu = nDrawer.constructNavigationDrawer(drawMenu, this);
		createDrawerToggle();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //create Managers for local lists and synch lists
		listmanager = getListManager();
		syncmanager = getSyncManager();

		updateAdapter();

	}
	
	private void createDrawerToggle() {
		drawerToggle = new ActionBarDrawerToggle(
                this,                  	/* host Activity */
                drawMenu,         		/* DrawerLayout object */
                R.drawable.ic_drawer,  	/* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  	/* "open drawer" description */
                R.string.drawer_close  	/* "close drawer" description */
                ) {

			// At the moment the following two methods are useless, but maybe 
			// we will want to add stuff later
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.app_name);
            }
        };
        
        // Set the drawer toggle as the DrawerListener
        drawMenu.setDrawerListener(drawerToggle);
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

	private void updateAdapter() {
		// Get List from manager
		shoppingLists = listmanager.getShoppingLists();
		shoppingListsNotArchived = new ArrayList<ShoppingList>();
		
		// separate archived lists
		for (ShoppingList list: shoppingLists)
			if (!list.isArchived())
				shoppingListsNotArchived.add(list);

		shoppingListAdapter = new ArrayAdapter<ShoppingList>(this,
				R.layout.shopping_list_item, shoppingListsNotArchived);

		ListView listView = (ListView) findViewById(R.id.ShoppingListView);
		listView.setAdapter(shoppingListAdapter);

		addListener(listView);
	}

	private void addListener(ListView listView) {
		// Add long click Listener
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			// TODO: Merge those 2 Listeners together, because we dont need two
			// seperate listeners...
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ShoppingList selectedList = shoppingListAdapter.getItem(arg2);
				HomeActivity.this.startActionMode(new ShoppingListActionMode(
						HomeActivity.this.listmanager, selectedList,
						HomeActivity.this.shoppingListAdapter,
						HomeActivity.this));
				return true;
			}
		});

		// Add click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(homeActivity, ViewListActivity.class);
				
				// get position in listmanager
				ShoppingList list = shoppingListsNotArchived.get(position);
				int listPosition = listmanager.getShoppingLists().indexOf(list);
				
				intent.putExtra("selectedList", listPosition);
				homeActivity.startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item))
        		return true;
                
		// Handle presses on the action bar items
		switch (item.getItemId()) {

			case R.id.action_refresh:
				// Test Requests added to the queue
				TelephonyManager tMgr =(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
				String phoneNumber = tMgr.getLine1Number();
				Request request2 = new FriendRequest(phoneNumber);
				syncmanager.addRequest(request2);
				Request request = new RegisterRequest(phoneNumber);
				syncmanager.addRequest(request);
				
				// This is the only line, which is not testing :)
				syncmanager.synchronise();
	
				return true;
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
	}
	
	public void onResume(){
    	super.onResume();
    	updateAdapter();
    }

}
