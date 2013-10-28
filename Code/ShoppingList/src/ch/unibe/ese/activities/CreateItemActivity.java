package ch.unibe.ese.activities;

import java.math.BigDecimal;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.Shop;
import ch.unibe.ese.core.ShoppingList;
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
			EditText textName = (EditText) findViewById(R.id.editTextName);
			textName.setText(name);
			// get list
			listIndex = extras.getInt("selectedList");
			list = manager.getShoppingLists().get(listIndex);
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
				textViewTitle.setText("Edit item:");
				editItem();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_item, menu);
		return true;
	}

	private void editItem() {
		// set name
		EditText textName = (EditText) findViewById(R.id.editTextName);
		textName.setText(item.getName());

		// set shop
		if (item.getShop() != null) {
			EditText textShop = (EditText) findViewById(R.id.editTextShop);
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
		boolean check = true;
		try {
			if (item == null)
				item = new Item(name);
			else
				item.setName(name);
		} catch (IllegalArgumentException e) {
			Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT)
					.show();
			check = false;
		}

		if (check) {
			// get shop
			EditText textShop = (EditText) findViewById(R.id.editTextShop);
			String shopName = textShop.getText().toString();
			if (shopName.trim().length() != 0) {
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
			try {
				manager.addItemToList(item, list);

			} catch (IllegalStateException e) {
				Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT)
						.show();
			}

			// go back to the list
			Intent intent = new Intent(this, ViewListActivity.class);
			intent.putExtra("selectedList", listIndex);
			this.startActivity(intent);
		}
	}
}
