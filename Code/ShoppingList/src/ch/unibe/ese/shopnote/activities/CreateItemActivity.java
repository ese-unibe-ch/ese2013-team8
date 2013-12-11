package ch.unibe.ese.shopnote.activities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.adapters.ItemAutocompleteAdapter;
import ch.unibe.ese.shopnote.adapters.ShopAutocompleteAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.ItemException;
import ch.unibe.ese.shopnote.core.ItemUnit;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.entities.ShoppingListItem;
import ch.unibe.ese.shopnote.core.entities.Recipe;
import ch.unibe.ese.shopnote.core.entities.Shop;
import ch.unibe.ese.shopnote.core.entities.ShoppingList;

/**
 * 	Creates a frame to create new items or edit them if the intent has an extra
 */
public class CreateItemActivity extends BaseActivity {

	private ListManager manager;
	private ShoppingList list;
	private Recipe recipe;
	private ShoppingListItem item;
	private boolean editItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_item);
        
		//set chosen color theme
		updateColor();

		
		// Show the Up button in the action bar.
		getActionBar().hide();
		manager = getListManager();

		setTextViews();
		openKeyboard();
	}

	private void updateColor() {
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.relativeLayoutCreateItem);
		updateThemeTextBox(lo);
		View someView = findViewById(R.id.RelativeLayoutCreateItemComplete);
		updateTheme(someView, getActionBar());
		LinearLayout llbuttons = (LinearLayout) findViewById(R.id.buttonBar);
		LinearLayout buttonsShadow = (LinearLayout) findViewById(R.id.buttonShadow);
		LinearLayout buttonsDivider = (LinearLayout) findViewById(R.id.buttonDivider);
		updateSaveAbordButtons(llbuttons, buttonsShadow, buttonsDivider);
	}

	/**
	 * Set the values of the textview fields if editing an item
	 */
	private void setTextViews() {
		// Set autocompletion adapter for shop
		AutoCompleteTextView textShop = (AutoCompleteTextView) findViewById(R.id.editTextShop);
		ShopAutocompleteAdapter sqliteShopAdapter = new ShopAutocompleteAdapter(this,
				android.R.layout.simple_list_item_1);
		textShop.setAdapter(sqliteShopAdapter);
		
		// Set the adapter for the UnitSpinner
		Spinner spinnerUnit = (Spinner) findViewById(R.id.editSpinnerUnits);
		String[] labels = getResources().getStringArray(R.array.item_units);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerUnit.setAdapter(adapter);
		
		Bundle extras = getIntent().getExtras();
		String name = "";

		if (extras != null) {
			//itemListView = extras.getBoolean(BaseActivity.EXTRAS_ITEM_EDIT); // to edit itemlistview
			// Create new item from ViewListActivity
			name = extras.getString(EXTRAS_ITEM_NAME);
			if ((name != null) && !name.equals("")) {
				setTextViewText(R.id.editTextName, name);
				// move focus to next field if name is set
				textShop.requestFocus();
				openKeyboard();
			}
			long listIndex = extras.getLong(EXTRAS_LIST_ID);
			if (listIndex != 0)
				list = manager.getShoppingList(listIndex);
			long recipeIndex = extras.getLong(EXTRAS_RECIPE_ID);
			if (recipeIndex != 0)
				recipe = manager.getRecipeAt(recipeIndex);

			// prepare for edit item when necessary
			if (extras.getBoolean(EXTRAS_ITEM_EDIT)) {
				long itemId = extras.getLong(EXTRAS_ITEM_ID);
				editItem = true;
				List<ShoppingListItem> items;
				if(list != null)
					items = manager.getItemsFor(list);
				else if (recipe != null)
					items = recipe.getItemList();
				else 
					items = manager.getAllItems();
				for (ShoppingListItem it :items) {
					if (it.getId() == itemId) {
						item = it;
						break;
					}
				}
				
				setTextViewText(R.id.textViewTitle, this.getString(R.string.edit_item_title));
				editItem();
			}
		}

		// Autocomplete only if adding a new item
		if (!editItem) {
			// create autocomplete adapter for name
			AutoCompleteTextView textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
			ItemAutocompleteAdapter sqliteItemAdapter = new ItemAutocompleteAdapter(this,
					android.R.layout.simple_list_item_1, manager, "", true);
			textName.setAdapter(sqliteItemAdapter);
			textName.setText(name);
		}
		
		// Set autocompletion adapter for shop
		textShop = (AutoCompleteTextView) findViewById(R.id.editTextShop);
		sqliteShopAdapter = new ShopAutocompleteAdapter(this,
				android.R.layout.simple_list_item_1);
		textShop.setAdapter(sqliteShopAdapter);
	}
	
	/**
	 * enter item data in edittext fields if editing an item
	 */
	private void editItem() {
		// set name
		setTextViewText(R.id.editTextName, item.getName());

		if (item.getShop() != null)
			setTextViewText(R.id.editTextShop, item.getShop().toString());
		
		if (item.getPrice() != null)
			setTextViewText(R.id.editTextPrice, item.getPrice().toString());
		
		if(item.getQuantity()!= null)
			setTextViewText(R.id.editTextQuantity, item.getQuantity().toString());
		
		if (item.getUnit() != null) {
			int position = item.getUnit().ordinal();
			((Spinner) findViewById(R.id.editSpinnerUnits)).setSelection(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_item, menu);
		return true;
	}

	/** Called when the user touches the abort button */
	public void goBack(View view) {
		super.finish();
	}

	/** Called when the user touches the save button */
	public void saveItem(View view) {
		// get name and change it if necessary
		String name = getTextViewText(R.id.editTextName);
		if (name == null || name.trim().isEmpty()) {
			Toast.makeText(this, this.getString(R.string.error_name),
					Toast.LENGTH_SHORT).show();
			return;
		}

		// get price and set it if necessary
		String priceString = getTextViewText(R.id.editTextPrice);
		BigDecimal price = null;
		if (!priceString.isEmpty() && !priceString.equals(".")) {
			price = new BigDecimal(priceString);
			price = price.setScale(2, RoundingMode.HALF_UP);
		}
		// get quantity and item
		String quantity = getTextViewText(R.id.editTextQuantity);
		int unitPosition = ((Spinner) findViewById(R.id.editSpinnerUnits)).getSelectedItemPosition();
		BigDecimal quant = (quantity.isEmpty() || quantity.equals(".")) ? null : new BigDecimal(quantity);
		ItemUnit unit = (unitPosition < 0 || quant == null) ? null : ItemUnit.values()[unitPosition];

		if (item == null)
			item = new ShoppingListItem(name);
		else
			item.setName(name);
		
		// get shop and set it if necessary
		String shopName = getTextViewText(R.id.editTextShop);
		if (!shopName.isEmpty()) {
			Shop shop = new Shop(shopName);
			item.setShop(shop);
		}
		item.setPrice(price);
		item.setQuantity(quant, unit);	

		try {
			// save the item
			if (list != null) {
				if (editItem)
					manager.updateItemInList(item, list);
				else
					manager.addItemToList(item, list);
			} else if (recipe != null) {
				recipe.addItem(item);
				manager.saveRecipe(recipe);
			} else {
				// save item if called in itemlist
				manager.save(item);
			}
	
			// go back to the list
			finish();
		} catch (ItemException e){
			showToast(getString(R.string.error_duplicate_item));
		}
	}

	public void finish() {
		Intent data = new Intent();
		if (item != null)
			data.putExtra(EXTRAS_ITEM_ID, item.getId());
		setResult(Activity.RESULT_OK, data);
		super.finish();
	}
}
