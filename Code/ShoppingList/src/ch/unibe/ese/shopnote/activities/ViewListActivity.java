package ch.unibe.ese.shopnote.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.adapters.ItemAdapter;
import ch.unibe.ese.shopnote.adapters.ItemAutocompleteAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Comparators;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ItemException;
import ch.unibe.ese.shopnote.core.ItemRecipeAdapter;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.Recipe;
import ch.unibe.ese.shopnote.core.ShoppingList;
import ch.unibe.ese.shopnote.core.Utility;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.listchange.ItemRequest;

/**
 *	Displays a single shopping list including the (bought) items
 */
public class ViewListActivity extends BaseActivity {

	private ListManager manager;
	private SyncManager syncmanager;
	private ArrayAdapter<Item> itemAdapter;
	private ArrayAdapter<Item> itemBoughtAdapter;
	private ItemAutocompleteAdapter sqliteAdapter;
	private ArrayList<Item>	itemsList;
	private ArrayList<Item>	itemsBoughtList;
	private ShoppingList list;
	private ListView listView;
	private ListView listViewBought;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_list);
       
		//set chosen color theme
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.RelativeLayoutViewList);
		RelativeLayout rlDrawer = (RelativeLayout) findViewById(R.id.drawer_Linearlayout);
		updateTheme(lo, getActionBar(), rlDrawer);
		LinearLayout lladdItem = (LinearLayout) findViewById(R.id.addItem);
		updateThemeTextBox(lladdItem);
		
		manager = getListManager();
		syncmanager = getSyncManager();

		// create drawer menu
		createDrawerMenu();
		createDrawerToggle(); // to change the title
		drawerToggle.setDrawerIndicatorEnabled(false);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			long listIndex = extras.getLong(EXTRAS_LIST_ID);
			list = manager.getShoppingList(listIndex);
			setTitle(list.getName());
		}

		refresh();

		// Autocompletion
		AutoCompleteTextView itemName = (AutoCompleteTextView) findViewById(R.id.editTextName);
		sqliteAdapter = new ItemAutocompleteAdapter(this, android.R.layout.simple_list_item_1, manager);
		itemName.setAdapter(sqliteAdapter);
		updateThemeTextBox(itemName);	
		
		itemName.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View arg1, int position,
	                long id) {
	        	try {
		        	ItemRecipeAdapter entry = (ItemRecipeAdapter) sqliteAdapter.getItem(position);
		        	if (entry.isItem()) {
		        		Item item = manager.getItem(entry.getId());
		        		manager.addItemToList(item, list);
		        		addItemRequestIfShared(item, false);
		        	} else {
		        		Recipe recipe = manager.getRecipeAt(entry.getId());
		        		for (Item item : recipe.getItemList()) {
							manager.addItemToList(item, list);
							addItemRequestIfShared(item, false);
							itemAdapter.add(item);
						}
		        	}
		        	updateAdapters();
		        	
		        	EditText addItem = (EditText) findViewById(R.id.editTextName);
		        	addItem.setText("");
	        	} catch (ItemException e){
	        		showToast(getString(R.string.error_duplicate_item));
	        	}
	        }
	    });

		// Done button
		itemName.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView view, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					addItem(view);
					updateAdapters();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Updates the adapters
	 */
	public void updateAdapters() {
		// Get list items
		List<Item> items = manager.getItemsFor(list);
		separateBoughtItems(items);

		listView = updateItemAdapter();
		listViewBought = updateItemBoughtAdapter();
		addListeners(listView, listViewBought);
		toggleShoppingCart();
		
		// Bugfix, allows to put a ListView in a ScrollView with other objects
		Utility.setListViewHeightBasedOnChildren(listView);
		Utility.setListViewHeightBasedOnChildren(listViewBought);
	}

	private void separateBoughtItems(List<Item> items) {
		itemsBoughtList = new ArrayList<Item>();
		itemsList = new ArrayList<Item>();

		for (Item item : items) {
			if (item.isBought())
				itemsBoughtList.add(item);
			else
				itemsList.add(item);
		}
	}

	private ListView updateItemAdapter() {
		itemAdapter = new ItemAdapter(this, R.layout.shopping_list_item,
				itemsList);
		ListView listView = (ListView) findViewById(R.id.ItemView);
		listView.setAdapter(itemAdapter);
		updateThemeListView(listView);
		return listView;
	}

	private ListView updateItemBoughtAdapter() {
		ListView listViewBought = (ListView) findViewById(R.id.ItemBoughtView);
		itemBoughtAdapter = new ItemAdapter(this,
				R.layout.shopping_list_item, itemsBoughtList);
		listViewBought.setAdapter(itemBoughtAdapter);
		updateThemeListView(listViewBought);
		return listViewBought;
	}

	// TODO: rewrite this method-code, its not that beautiful...
	private void addListeners(ListView listView, ListView listViewBought) {
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
				updateAdapters();
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
				manager.updateItemInList(item, list);
				addItemRequestIfShared(item, true);

				itemAdapter.remove(item);
				itemBoughtAdapter.add(item);
				updateAdapters();
			}
		});

		// Add long click Listener to bought items
		listViewBought
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Item selectedItem = itemBoughtAdapter.getItem(position);
						ViewListActivity.this
								.startActionMode(new ShoppingListActionMode(
										ViewListActivity.this.manager,
										selectedItem, list,
										ViewListActivity.this.itemAdapter,
										ViewListActivity.this));
						updateAdapters();
						return true;
					}
				});

		// Add click Listener to bought items
		listViewBought.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// bought items
				Item item = itemBoughtAdapter.getItem(position);
				item.setBought(false);
				manager.updateItemInList(item, list);
				addItemRequestIfShared(item, false);
				itemBoughtAdapter.remove(item);
				itemAdapter.add(item);
				updateAdapters();
			}
		});
	}

	/** Called when the user touches the add item button */
	public void addItemEdit(View view) {
		Intent intent = new Intent(this, CreateItemActivity.class);
		EditText textName = (EditText) findViewById(R.id.editTextName);
		String name = textName.getText().toString();
		intent.putExtra(EXTRAS_ITEM_NAME, name);
		intent.putExtra(EXTRAS_LIST_ID, list.getId());
		this.startActivityForResult(intent, 1);

		// remove text from field
		textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
		textName.setText("");
	}

	/** Called when the user touches the ok button */
	public void addItem(View view) {
		EditText textName = (EditText) findViewById(R.id.editTextName);
		String name = textName.getText().toString();
		if (name.trim().length() == 0) {
			Toast.makeText(this, this.getString(R.string.error_name),
					Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			if (name.charAt(0) == '/') {
				List<Recipe> recipeList = manager.getRecipes();
				String recipeName = name.toLowerCase();
	
				for (Recipe recipe : recipeList) {
					if (recipe.getName().toLowerCase().equals(recipeName)) {
						for (Item item : recipe.getItemList()) {
							item.setBought(false);
							manager.addItemToList(item, list);
							addItemRequestIfShared(item, false);
							itemAdapter.add(item);
						}
					}
				}
	
			} else {
				Item item = manager.getItem(name);
				if (item == null)
					item = new Item(name);
				boolean added = manager.addItemToList(item, list);
				addItemRequestIfShared(item, false);
	
				if (added) {	// refresh view
					itemAdapter.add(item);
					itemAdapter.sort(Comparators.ITEM_COMPARATOR);
				}
			}
	
			// remove text from field
			textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
			textName.setText("");
			updateAdapters();
		} catch (ItemException e){
			showToast(getString(R.string.error_duplicate_item));
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
		long listIndex = list.getId();
		
		switch (item.getItemId()) {  
			case android.R.id.home:
				finish();
				return true;
			
			// Handle presses on the action bar items
			case R.id.action_share:
				Intent intentShare = new Intent(this, ShareActivity.class);
				intentShare.putExtra(EXTRAS_LIST_ID, listIndex);
				this.startActivity(intentShare);
				return true;

			// Handle presses on overflow menu items
			case R.id.action_edit_list:
				Intent intentEdit = new Intent(this, CreateListActivity.class);
				intentEdit.putExtra(EXTRAS_LIST_ID, listIndex);
				this.startActivity(intentEdit);
				return true;
			case R.id.action_archive:
				// toggle archive state
				list.setArchived(!list.isArchived());
				manager.saveShoppingList(list);
				finish();
				return true;
			case R.id.action_delete:
				manager.removeShoppingList(list);
				finish();
				return true;
			case R.id.action_refresh:
				synchronize();
				Toast.makeText(this, this.getString(R.string.synchronizing),
						Toast.LENGTH_SHORT).show();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK)
			updateAdapters();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	/**
	 *	Hide the shopping cart if it is empty
	 */
	private void toggleShoppingCart() {
		// hide shopping cart image if no items bought
		ImageView imageView = (ImageView) findViewById(R.id.imageItemsBought);
		TextView textView = (TextView) findViewById(R.id.textItemsBought);
		if (!itemBoughtAdapter.isEmpty()) {
			imageView.setVisibility(View.VISIBLE);
			textView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.INVISIBLE);
			textView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void addItemRequestIfShared(Item item, boolean isBought) {
		if (list.isShared()){
			ItemRequest irequest = new ItemRequest(getMyPhoneNumber(), list.getId(), item.copy());
			irequest.setBought(isBought);
			syncmanager.addRequest(irequest);
			syncmanager.synchronise(this);
		}
	}
	
	@Override
	public void refresh() {
		this.updateAdapters();
		// reset change count notification
		list.setChangesCount(0);
		manager.saveShoppingList(list);
	}
}