package ch.unibe.ese.core.sqlite;

import ch.unibe.ese.core.Friend;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.Recipe;
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
		values.put(SQLiteHelper.COLUMN_LIST_ARCHIVED, list.isArchived() ? 1 : 0);
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
		values.put(SQLiteHelper.COLUMN_LIST_ID, list.getId());
		values.put(SQLiteHelper.COLUMN_ITEMBOUGHT, item.isBought() ? 1 : 0);
		String price = item.getPrice() != null ? item.getPrice().toString()
				: null;
		values.put(SQLiteHelper.COLUMN_LISTPRICE, price);
		values.put(SQLiteHelper.COLUMN_ITEM_QUANTITY, item.getQuantity());
		return values;
	}

	/**
	 * Converts an friend into a Contentvalue (TABLE_FRIENDS)
	 * 
	 * @param friends
	 * @return
	 */
	public ContentValues toValue(Friend friend) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_FRIEND_NAME, friend.getName());
		values.put(SQLiteHelper.COLUMN_FRIEND_PHONENR, friend.getPhoneNr());
		return values;
	}
	
	/**
	 * Converts an recipe into a ContentValue (TABLE_RECIPE)
	 * @param recipe
	 * @return
	 */
	public ContentValues toValue(Recipe recipe) {
		ContentValues values = new ContentValues();
//		if(recipe.getId() != null)
//			values.put(SQLiteHelper.COLUMN_RECIPE_ID, recipe.getId());
		values.put(SQLiteHelper.COLUMN_RECIPE_NAME, recipe.getName());
		return values;
	}
	
	public ContentValues toValue(Recipe recipe, Item item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_RECIPE_ID, recipe.getId());
		values.put(SQLiteHelper.COLUMN_ITEM_ID, item.getId());
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
