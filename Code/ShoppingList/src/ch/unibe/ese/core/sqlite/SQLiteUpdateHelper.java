package ch.unibe.ese.core.sqlite;

import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ShoppingList;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class provides useful functions for updating the database
 * 
 * @author Stephan
 * 
 */
public class SQLiteUpdateHelper {

	private SQLiteDatabase database;
	private SQLiteReadHelper readHelper;

	public SQLiteUpdateHelper(SQLiteDatabase database,
			SQLiteReadHelper readHelper) {
		this.database = database;
		this.readHelper = readHelper;
	}

	/**
	 * Converts a list to a ContentValue, which can be inserted into the
	 * database
	 * 
	 * @param list
	 * @return
	 */
	public ContentValues toValue(ShoppingList list) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_LIST_NAME, list.getName());
		values.put(SQLiteHelper.COLUMN_LIST_DUEDATE,
				list.getDueDate() != null ? list.getDueDate().getTime() : null);
		this.addShopIfNotExistent(list.getShop());
		values.put(SQLiteHelper.COLUMN_SHOP_ID,
				readHelper.getShopId(list.getShop()));
		return values;
	}

	/**
	 * Converts an item to a Contentvalue (TABLE_ITEMS)
	 * 
	 * @param item
	 * @return
	 */
	public ContentValues toValue(Item item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_ITEM_NAME, item.getName());
		return values;
	}

	/**
	 * Converts an item and a list to a Contentvalue (TABLE_ITEMTOLIST)
	 * 
	 * @param item
	 * @param list
	 * @return
	 */
	public ContentValues toValue(Item item, ShoppingList list) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_ITEM_ID, item.getId());
		values.put(SQLiteHelper.COLUMN_LIST_ID,
				readHelper.getListId(list.getName()));
		values.put(SQLiteHelper.COLUMN_ITEMBOUGHT, item.isBought() ? 1 : 0);
		String price = item.getPrice()!= null?item.getPrice().toString():null;
		values.put(SQLiteHelper.COLUMN_LISTPRICE, price);
		return values;
	}

	/**
	 * Adds a shop to the database if it doesn't exist yet
	 * 
	 * @param name
	 */
	private void addShopIfNotExistent(String name) {
		if (readHelper.getShopId(name) == -1) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_SHOP_NAME, name);
			database.insert(SQLiteHelper.TABLE_SHOPS, null, values);
		}
	}

	/**
	 * Adds an item to the database if it doesn't exist yet
	 * 
	 * @param item
	 */
	public void addItemIfNotExistent(Item item) {
		long id = readHelper.getItemId(item.getName());
		if (id == -1) {
			ContentValues values = this.toValue(item);
			id = database.insert(SQLiteHelper.TABLE_ITEMS, null, values);
		}
		if (item.getId() == null)
			item.setId(id);
	}
}
