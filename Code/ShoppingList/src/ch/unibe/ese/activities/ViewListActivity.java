package ch.unibe.ese.activities;

import java.util.ArrayList;
import java.util.List;

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
import ch.unibe.ese.core.sqlite.SQLiteItemAdapter;
import ch.unibe.ese.shoppinglist.R;

public class ViewListActivity extends BaseActivity {

	private ListManager manager;
	private ArrayAdapter<Item> itemAdapter;
	private ArrayAdapter<Item> itemBoughtAdapter;
	private Activity viewListActivity = this;
	private ShoppingList list;
	private int listIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_list);
		// Show the Up button in the action bar.
		setupActionBar();

		manager = getListManager();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			listIndex = extras.getInt("selectedList");
			list = manager.getShoppingLists().get(listIndex);
			setTitle(list.getName());
		}

		// Get list items
		List<Item> items = manager.getItemsFor(list);

		itemAdapter = new ArrayAdapter<Item>(this, R.layout.shopping_list_item,
				new ArrayList<Item>());
		itemBoughtAdapter = new ArrayAdapter<Item>(viewListActivity,
				R.layout.shopping_list_item, new ArrayList<Item>());
		

		ListView listView = (ListView) findViewById(R.id.ItemView);
		listView.setAdapter(itemAdapter);
		
		ListView listViewBought = (ListView) findViewById(R.id.ItemBoughtView);
		listViewBought.setAdapter(itemBoughtAdapter);

		for (Item item : items) {
			if (item.isBought())
				itemBoughtAdapter.add(item);
			else
				itemAdapter.add(item);
		}

		// Add long click Listener
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Item selectedItem = itemAdapter.getItem(arg2);
				ViewListActivity.this
						.startActionMode(new ShoppingListActionMode(
								ViewListActivity.this.manager, selectedItem,
								list, ViewListActivity.this.itemAdapter,
								ViewListActivity.this));
				return true;
			}
		});

		// Add click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// bought items
				Item item = itemAdapter.getItem(position);
				item.setBought(true);
				manager.addItemToList(item, list);
				itemAdapter.remove(item);
				
				itemBoughtAdapter.add(item);
				// TODO: add striketrough to bought items
				// TODO: set scrollbar on whole activity, not on

			}
		});

		// Autocompletion
		AutoCompleteTextView textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
		SQLiteItemAdapter sqliteAdapter = new SQLiteItemAdapter(this,
				android.R.layout.simple_list_item_1);
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
			Toast.makeText(this, this.getString(R.string.error_name), Toast.LENGTH_SHORT).show();
		} else {
			Item item = new Item(name);
			manager.addItemToList(item, list);

			// refresh view
			itemAdapter.add(item);

			// remove text from field
			textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
			textName.setText("");

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
    	int listIndex = manager.getShoppingLists().indexOf(list);
    	
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
			
		// Handle presses on the action bar items
		case R.id.action_share:
			Intent intentShare = new Intent(this, ShareListActivity.class);
        	intentShare.putExtra("selectedList", listIndex);
            this.startActivity(intentShare);
			return true;
		
		// Handle presses on overflow menu items
		case R.id.action_edit_list:
        	Intent intentEdit = new Intent(this, CreateListActivity.class);
        	intentEdit.putExtra("selectedList", listIndex);
            this.startActivity(intentEdit);
			return true;
		case R.id.action_archive:
			// TODO: add archive function
			Toast.makeText(this, this.getString(R.string.error_missing), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_delete:
			manager.removeShoppingList(list);
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_refresh:
			// TODO: add synchronize function
			Toast.makeText(this, this.getString(R.string.error_missing), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_settings:
			Intent optionsIntent = new Intent(this, OptionsActivity.class);
			this.startActivity(optionsIntent);
			return true;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
}
