package ch.unibe.ese.activities;

import java.util.List;
import java.util.concurrent.ExecutionException;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.share.RequestListener;
import ch.unibe.ese.share.RequestSender;
import ch.unibe.ese.share.SyncManager;
import ch.unibe.ese.share.requests.FriendRequest;
import ch.unibe.ese.share.requests.RegisterRequest;
import ch.unibe.ese.share.requests.Request;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.sidelist.NavigationDrawer;

public class HomeActivity extends BaseActivity {

	private ListManager listmanager;
	private SyncManager syncmanager;
	private ArrayAdapter<ShoppingList> shoppingListAdapter;
	private Activity homeActivity = this;

	private DrawerLayout drawMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// create slide list
		NavigationDrawer nDrawer = new NavigationDrawer();
		drawMenu = nDrawer.constructNavigationDrawer(drawMenu, this);

		listmanager = getListManager();
		syncmanager = new SyncManager();

		// Get List from manager
		List<ShoppingList> shoppingLists = listmanager.getShoppingLists();

		shoppingListAdapter = new ArrayAdapter<ShoppingList>(this,
				R.layout.shopping_list_item, shoppingLists);

		ListView listView = (ListView) findViewById(R.id.ShoppingListView);
		listView.setAdapter(shoppingListAdapter);

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
				intent.putExtra("selectedList", position);
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
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_refresh:
			// Test Requests added to the queue
			TelephonyManager tMgr =(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = tMgr.getLine1Number();
			Request request = new RegisterRequest(phoneNumber);
			syncmanager.addRequest(request);
			Request request2 = new FriendRequest(phoneNumber);
			syncmanager.addRequest(request2);
			
			// This is the only line, which is not testing :)
			syncmanager.synchronise(this);

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
}
