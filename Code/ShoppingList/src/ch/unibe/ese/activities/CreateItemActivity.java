package ch.unibe.ese.activities;

import java.math.BigDecimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.Shop;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.core.sqlite.SQLiteItemAdapter;
import ch.unibe.ese.core.sqlite.SQLiteShopAdapter;
import ch.unibe.ese.shoppinglist.R;

public class CreateItemActivity extends BaseActivity {

	private ListManager manager;
	private ShoppingList list;
	private Item item;
	private int listIndex;
	private TextView textViewTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_item);
		// Show the Up button in the action bar.
		getActionBar().hide();
		manager = getListManager();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// get item name
			String name = extras.getString("Item");
			
			AutoCompleteTextView textName = (AutoCompleteTextView) findViewById(R.id.editTextName);
			SQLiteItemAdapter sqliteItemAdapter = new SQLiteItemAdapter(this,
					android.R.layout.simple_list_item_1);
			textName.setAdapter(sqliteItemAdapter);
			textName.setText(name);
			// get list
			listIndex = extras.getInt("selectedList");
			list = manager.getShoppingLists().get(listIndex);
			
			// Set autocompletion adapter
			AutoCompleteTextView textShop = (AutoCompleteTextView) findViewById(R.id.editTextShop);
			SQLiteShopAdapter sqliteShopAdapter = new SQLiteShopAdapter(this,
					android.R.layout.simple_list_item_1);
			textShop.setAdapter(sqliteShopAdapter);
			
			// edit item
			if (extras.getBoolean("editItem")) {
				long itemId = extras.getLong("selectedItem");
				for (Item it : manager.getItemsFor(list)) {
					if (it.getId() == itemId) {
						item = it;
						break;
					}
				}
				textViewTitle = (TextView) findViewById(R.id.textViewTitle);
				textViewTitle.setText(this.getString(R.string.edit_item_title));
				setItem();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_item, menu);
		return true;
	}

	private void setItem() {
		// set name
		EditText textName = (EditText) findViewById(R.id.editTextName);
		textName.setText(item.getName());

		// set shop (the one from the list has higher priority
		// Fail with commented code:
		// I Add shop to one list
		// I Add Milk to this list
		// I create new list with other shop
		// I Add Milk to this list too
		// Milk gets assigned the shop of the older list => THERE'S SOMETHING WRONG (maybe the wrong list was passed to the CreateItemActivity?)
//		if (list.getShop() != null) {
//			TextView textShop = (TextView) findViewById(R.id.editTextShop);
//			textShop.setText(list.getShop().toString());
//			textShop.setEnabled(false);
//		} else 
		if (item.getShop() != null) {
			TextView textShop = (TextView) findViewById(R.id.editTextShop);
			textShop.setText(item.getShop().toString());
		}

		if (item.getPrice() != null) {
			EditText textPrice = (EditText) findViewById(R.id.editTextPrice);
			textPrice.setText(item.getPrice().toString());
		}
	}

	/** Called when the user touches the abort button */
	public void goBack(View view) {
		finish();
	}

	/** Called when the user touches the save button */
	public void saveItem(View view) {
		// get name
		EditText textName = (EditText) findViewById(R.id.editTextName);
		String name = textName.getText().toString();
		if (name == null || name.trim().isEmpty()) {
			Toast.makeText(this, this.getString(R.string.error_name), Toast.LENGTH_SHORT).show();
			return;
		}

		if (item == null)
			item = new Item(name);
		else
			item.setName(name);

		// get shop
		EditText textShop = (EditText) findViewById(R.id.editTextShop);
		String shopName = textShop.getText().toString();
		if (!shopName.isEmpty()) {
			Shop shop = new Shop(shopName);
			item.setShop(shop);
		}

		// get price
		EditText textPrice = (EditText) findViewById(R.id.editTextPrice);
		String priceString = textPrice.getText().toString();
		if (priceString != null && !priceString.isEmpty()) {
			BigDecimal price = new BigDecimal(priceString);
			item.setPrice(price);
		}

		// save the item
		manager.addItemToList(item, list);

		// go back to the list
		Intent intent = new Intent(this, ViewListActivity.class);
		intent.putExtra("selectedList", listIndex);
		this.startActivity(intent);
	}
}
