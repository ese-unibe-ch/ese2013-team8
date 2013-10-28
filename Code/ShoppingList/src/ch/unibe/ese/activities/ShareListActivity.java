package ch.unibe.ese.activities;

import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.shoppinglist.R.layout;
import ch.unibe.ese.shoppinglist.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ShareListActivity extends BaseActivity {

	private ListManager manager;
	private ShoppingList list;
	private int listIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_list);
		// Show the Up button in the action bar.
		getActionBar();
		
		manager = getListManager();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			listIndex = extras.getInt("selectedList");
			list = manager.getShoppingLists().get(listIndex);
			setTitle("Share " + list.getName());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Navigate back to the list
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
