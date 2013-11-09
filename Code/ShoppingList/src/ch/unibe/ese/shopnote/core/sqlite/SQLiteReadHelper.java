package ch.unibe.ese.shopnote.core.sqlite;

import java.math.BigDecimal;
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.Recipe;
import ch.unibe.ese.shopnote.core.ShoppingList;

/**
 * This Class provides usefull functions for reading the database
 * 
 * @author Stephan
 * 
 */
public class SQLiteReadHelper {

	private SQLiteDatabase database;
	private static final String SELECT_ALL_ITEM_DATA = "SELECT " //
			+ SQLiteHelper.TABLE_ITEMS + "." + SQLiteHelper.COLUMN_ITEM_ID + ","
			+ SQLiteHelper.TABLE_ITEMS + "." + SQLiteHelper.COLUMN_ITEM_NAME + ","
			+ SQLiteHelper.TABLE_ITEMTOLIST + "." + SQLiteHelper.COLUMN_ITEMBOUGHT + ","
			+ SQLiteHelper.TABLE_ITEMTOLIST + "." + SQLiteHelper.COLUMN_LISTPRICE + ","
			+ SQLiteHelper.TABLE_ITEMTOLIST + "." + SQLiteHelper.COLUMN_ITEM_QUANTITY
			+ " FROM "
			+ SQLiteHelper.TABLE_ITEMS + ","//
			+ SQLiteHelper.TABLE_ITEMTOLIST
			+ " WHERE "
			+ SQLiteHelper.TABLE_ITEMS + "." + SQLiteHelper.COLUMN_ITEM_ID
			+ " = " + SQLiteHelper.TABLE_ITEMTOLIST + "."+ SQLiteHelper.COLUMN_ITEM_ID;
	
	private static final String SELECT_ITEM_DATA = SELECT_ALL_ITEM_DATA
			+ " AND "
			+ SQLiteHelper.TABLE_ITEMTOLIST + "."
			+ SQLiteHelper.COLUMN_LIST_ID
			+ " = ?";;
			
	private static final String SELECT_RECIPE_DATA = "SELECT "
			+ SQLiteHelper.TABLE_RECIPES + 		"." + SQLiteHelper.COLUMN_RECIPE_ID + ","
			+ SQLiteHelper.TABLE_RECIPES + 		"." + SQLiteHelper.COLUMN_RECIPE_NAME + ","
			+ SQLiteHelper.TABLE_ITEMTORECIPE + "." + SQLiteHelper.COLUMN_ITEM_ID 
			+ " FROM " 
			+ SQLiteHelper.TABLE_RECIPES + ","
			+ SQLiteHelper.TABLE_ITEMTORECIPE 
			+ " WHERE "
			+ SQLiteHelper.TABLE_ITEMTORECIPE + "." + SQLiteHelper.COLUMN_RECIPE_ID
			+ " = " 
			+ SQLiteHelper.TABLE_RECIPES + 		"." + SQLiteHelper.COLUMN_RECIPE_ID;
	

	public SQLiteReadHelper(SQLiteDatabase database) {
		this.database = database;
	}
	

	/**
	 * Get a cursor on TABLE_LISTS
	 * 
	 * @return
	 */
	public Cursor getListCursor() {
		Cursor cursor = getQueryCursor(SQLiteHelper.TABLE_LISTS,
				SQLiteHelper.LISTS_COLUMNS, null);
		cursor.moveToFirst();
		return cursor;
	}

	/**
	 * Get a cursor on TABLE_ITEMTOLIST for a specific listID
	 * @param list
	 * @return
	 */
	public Cursor getItemCursor(ShoppingList list) {
		Long listId = list.getId();

		Cursor cursor = database.rawQuery(SELECT_ITEM_DATA, new String[] { ""
				+ listId });
		cursor.moveToFirst();
		return cursor;
	}
	
	/**
	 * Get a cursor on TABLE_ITEM for all items with all abilities
	 * @return
	 */
	public Cursor getItemCursor(){
		Cursor cursor = database.rawQuery(SELECT_ALL_ITEM_DATA, null );
		cursor.moveToFirst();
		return cursor;
	}
	
	/**
	 * Get a cursor on TABLE_ITEM for all items with just name ability 
	 * @return
	 */
	public Cursor getItemTableCursor() {
		Cursor cursor = getQueryCursor(SQLiteHelper.TABLE_ITEMS, SQLiteHelper.ITEMS_COLUMNS , null);
		cursor.moveToFirst();
		return cursor;
	}

	/**
	 * Get a cursor on TABLE_FRIENDS
	 * @return
	 */
	public Cursor getFriendCursor() {
		Cursor cursor = getQueryCursor(SQLiteHelper.TABLE_FRIENDS,
				SQLiteHelper.FRIENDS_COLUMNS, null);
		cursor.moveToFirst();
		return cursor;
	}
	
	/**
	 * Get the cursor to a complete recipe db
	 * @return
	 */
	public Cursor getRecipeCursor() {
		Cursor cursor = getQueryCursor(SQLiteHelper.TABLE_RECIPES,
				SQLiteHelper.RECIPE_COLUMNS, null);
		cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor getItemToRecipeCursor() {
		Cursor cursor = getQueryCursor(SQLiteHelper.TABLE_ITEMTORECIPE,
				SQLiteHelper.ITEMTORECIPE_COLUMNS, null);
		cursor.moveToFirst();
		return cursor;
	}

	/**
	 * Shortcut for database querys (to save all the nulls)
	 * @param table
	 * @param columns
	 * @param where
	 * @return
	 */
	public Cursor getQueryCursor(String table, String[] columns, String where) {
		Cursor cursor = database.query(table, columns, where, null, null, null,
				null);
		return cursor;
	}

	/**
	 * Converts a cursor (at right position) to a shopping list
	 * 
	 * @param cursor
	 * @return
	 */
	public ShoppingList cursorToShoppingList(Cursor cursor) {
		ShoppingList list = new ShoppingList(cursor.getString(1));
		list.setId(cursor.getLong(0));
		list.setArchived(cursor.getInt(2) == 1);
		if (cursor.getLong(3) > 0)
			list.setDueDate(new Date(cursor.getLong(3)));
		
		list.setShop(getShopName(cursor.getInt(4)));
		return list;
	}

	public Item cursorToItem(Cursor cursor) {
		Item item = new Item(cursor.getString(1));
		item.setId(cursor.getInt(0));
		item.setBought(cursor.getInt(2) == 1);
		String price = cursor.getString(3);
		if (price != null && !price.isEmpty())
			item.setPrice(new BigDecimal(price));
		item.setQuantity(cursor.getString(4));
		return item;
	}
	
	public Item cursorToItemLite(Cursor cursor) {
		Item item = new Item(cursor.getString(1));
		item.setId(cursor.getInt(0));
		return item;
	}

	public Friend cursorToFriend(Cursor cursor) {
		Friend friend = new Friend(cursor.getString(1), cursor.getString(2));
		friend.setId(cursor.getLong(0));
		return friend;
	}
	
	public Recipe cursorToRecipe(Cursor cursor) {
		Recipe recipe = new Recipe(cursor.getString(1));
		recipe.setId(cursor.getLong(0));
		return recipe;
	}

	/**
	 * Get Shop name with shop ID
	 * 
	 * @param shopId
	 * @return shopname
	 */
	public String getShopName(int shopId) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_SHOPS,
				SQLiteHelper.SHOPS_COLUMNS, SQLiteHelper.COLUMN_SHOP_ID + "=?",
				new String[] { "" + shopId }, null, null, null, null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return cursor.getString(1);
		} else {
			return null;
		}
	}

	/**
	 * Get Shop ID with shop name or -1 if the shop does not exist.
	 * 
	 * @param shopName
	 * @return
	 */
	public int getShopId(String shopName) {
		if (shopName == null)
			return -1;
		Cursor cursor = database.query(SQLiteHelper.TABLE_SHOPS,
				SQLiteHelper.SHOPS_COLUMNS, SQLiteHelper.COLUMN_SHOP_NAME
						+ "=?", new String[] { shopName }, null, null, null,
				null);
		System.err.println("count: " + cursor.getCount());
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return cursor.getInt(0);
		} else {
			return -1;
		}
	}

	/**
	 * Get List Name with list ID
	 * 
	 * @param listId
	 * @return
	 */
	public String getListName(int listId) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_LISTS,
				SQLiteHelper.LISTS_COLUMNS, SQLiteHelper.COLUMN_LIST_ID + "=?",
				new String[] { "" + listId }, null, null, null, null);
		if (cursor.getCount() == 1) {
			return cursor.getString(1);
		} else {
			return null;
		}
	}

	/**
	 * Get Item Id with Item Name
	 * 
	 * @param itemName
	 * @return
	 */
	public long getItemId(String itemName) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_ITEMS,
				SQLiteHelper.ITEMS_COLUMNS, SQLiteHelper.COLUMN_ITEM_NAME
						+ "=?", new String[] { itemName }, null, null, null,
				null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return cursor.getLong(0);
		} else {
			return -1;
		}
	}

	/**
	 * Get friend name with item ID
	 * 
	 * @param itemId
	 * @return
	 */
	public String getFriendName(String friendNr) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_FRIENDS,
				SQLiteHelper.FRIENDS_COLUMNS, SQLiteHelper.COLUMN_FRIEND_PHONENR
						+ "=?", new String[] {friendNr }, null, null,
				null, null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return cursor.getString(1);
		} else {
			return null;
		}
	}

	/**
	 * Get the name of the recipe with the id
	 * @param id
	 * @return name of recipe or when no such id in list null
	 */
	public String getRecipeName(Long id) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_RECIPES,
				SQLiteHelper.RECIPE_COLUMNS, SQLiteHelper.COLUMN_RECIPE_ID
						+ "=?", new String[] { "" + id }, null, null,
				null, null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return cursor.getString(1);
		}  
		
		return null;
	}
	
	/**
	 * Get the id of the recipe with the name
	 * @param name
	 * @return id of recipe or when not found -1
	 */
	public long getRecipeId(String name) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_RECIPES,
				SQLiteHelper.RECIPE_COLUMNS, SQLiteHelper.COLUMN_RECIPE_NAME
						+ "=?", new String[] { "" + name }, null, null,
				null, null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			return cursor.getLong(0);
		}  
		
		return -1;
	}

	/**
	 * Check if an item is already in a list (database level)
	 * 
	 * @param item
	 * @param list
	 * @return true if item is in list
	 */
	public boolean isInList(Item item, ShoppingList list) {
		long itemId = item.getId() == null ? -1 : item.getId();
		Long listId = list.getId();
		Cursor cursor = database.query(SQLiteHelper.TABLE_ITEMTOLIST,
				SQLiteHelper.ITEMTOLIST_COLUMNS, SQLiteHelper.COLUMN_ITEM_ID
						+ "=? AND " + SQLiteHelper.COLUMN_LIST_ID + "=?",
				new String[] { "" + itemId, "" + listId }, null, null, null,
				null);
		return (cursor.getCount() >= 1);
	}

	/**
	 * Check if an item is already in db (database level)
	 * @param item
	 * @return true if item is in db
	 */
	public boolean isInList(Item item) {
		if(item.getId() == null) return false;
		long itemId = item.getId();
		Cursor cursor = database.query(SQLiteHelper.TABLE_ITEMS,
				SQLiteHelper.ITEMS_COLUMNS, SQLiteHelper.COLUMN_ITEM_ID + "=? ", 
				new String[] { "" + itemId }, null, null, null, null);
		return cursor.getColumnCount() >= 1;
	}

	/**
	 * Check if an item is already in a recipe (database level)
	 * @param item
	 * @param id
	 * @return true if item is in recipe
	 */
	public boolean isInList(Item item, Recipe recipe) {
		if(recipe.getId() == null || item.getId() == null) return false;
		
		Cursor cursor = database.query(SQLiteHelper.TABLE_ITEMTORECIPE,
				SQLiteHelper.ITEMTORECIPE_COLUMNS, SQLiteHelper.COLUMN_ITEM_ID
						+ "=? AND " + SQLiteHelper.COLUMN_RECIPE_ID + "=?",
				new String[] { "" + item.getId(), "" + recipe.getId() }, null, null, null, null);
		
		if(cursor.getCount() == 1)
			return true;
		if(cursor.getCount() > 1)
			throw new IllegalStateException();
		else
			return false;
	}

	/**
	 * Checks if the db contains the entry of the recipe already
	 * @param recipe
	 * @return true if already in db, otherwise false
	 */
	public boolean isInList(Recipe recipe) {
		if(recipe.getId() == null) return false;
		if(getRecipeName(recipe.getId()) == null) return false;
		
		return true;
	}

	/**
	 * Checks if the db contains the entry of a friend which is connected to a list
	 * @param list
	 * @param friend
	 * @return true if entry exists, otherwise false
	 */
	public boolean isInList(ShoppingList list, Friend friend) {
		Cursor cursor = database.query(SQLiteHelper.TABLE_FRIENDSTOLIST,
				SQLiteHelper.FRIENDSTOLIST_COLUMNS, SQLiteHelper.COLUMN_FRIEND_ID
				+ "=? AND " + SQLiteHelper.COLUMN_LIST_ID + "=?",
				new String[] { "" + friend.getId(), "" + list.getId() },null, null, null, null);
		
		if(cursor.getCount() == 1)
			return true;
		
		return false;
	}
}
