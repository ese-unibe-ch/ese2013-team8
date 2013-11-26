package ch.unibe.ese.shopnote.core.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.PersistenceManager;
import ch.unibe.ese.shopnote.core.Recipe;
import ch.unibe.ese.shopnote.core.ShoppingList;

/**
 * This class provides function to save all lists / items / shops / recipes to a SQLite
 * database
 * 
 */

public class SQLitePersistenceManager implements PersistenceManager {

	private final Context context;
	// Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private SQLiteUpdateHelper updateHelper;
	private SQLiteReadHelper readHelper;

	public void close() {
		dbHelper.close();
	}

	public SQLitePersistenceManager(Context applicationContext) {
		if (applicationContext == null)
			throw new IllegalArgumentException("null is not allowed");

		this.context = applicationContext;
		this.dbHelper = new SQLiteHelper(this.context);
		this.database = dbHelper.getWritableDatabase();
		this.readHelper = new SQLiteReadHelper(this.database);
		this.updateHelper = new SQLiteUpdateHelper(this.database,
				this.readHelper);
	}

	/**
	 * Everything for lists
	 */

	@Override
	public List<ShoppingList> getLists() {
		List<ShoppingList> lists = new ArrayList<ShoppingList>();

		Cursor cursor = readHelper.getListCursor();
		while (!cursor.isAfterLast()) {
			ShoppingList list = readHelper.cursorToShoppingList(cursor);
			lists.add(list);
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
	}

	@Override
	public void save(ShoppingList list) {
		// Convert the list to a ContentValue
		// Automatically creates a new shop in the database if it doesn't exist
		ContentValues values = updateHelper.toValue(list);
		// If this is a new list
		if (list.getId() == null) {
			long id = database.insert(SQLiteHelper.TABLE_LISTS, null, values);
			list.setId(id);
		} else { // Else if it is an old list
			database.update(SQLiteHelper.TABLE_LISTS, values,
					SQLiteHelper.COLUMN_LIST_ID + " = ?", new String[] { ""
							+ list.getId() });
		}
	}

	@Override
	public void remove(ShoppingList list) {
		Long listId = list.getId();
		if (listId != null) {
			database.delete(SQLiteHelper.TABLE_ITEMTOLIST,
					SQLiteHelper.COLUMN_LIST_ID + " = ?", new String[] { ""
							+ listId });
			database.delete(SQLiteHelper.TABLE_LISTS,
					SQLiteHelper.COLUMN_LIST_ID + "= ?", new String[] { ""
							+ listId });
		}
	}

	/**
	 * Everything for Items
	 */

	@Override
	public List<Item> getItems(ShoppingList list) {
		List<Item> itemList = new ArrayList<Item>();
		Cursor cursor = readHelper.getItemCursor(list);
		while (!cursor.isAfterLast()) {
			Item item = readHelper.cursorToItem(cursor);
			itemList.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return itemList;
	}

	public List<Item> getAllItems() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		Cursor cursor = readHelper.getItemTableCursor();
		while (!cursor.isAfterLast()) {
			Item item = readHelper.cursorToItemLite(cursor);
			if (item != null)
				itemList.add(item);
			cursor.moveToNext();
		}
		cursor.close();

		return itemList;
	}

	@Override
	public void save(Item item, ShoppingList list) {
		// Add the item to the Items Table
		updateHelper.addItemIfNotExistent(item);
		// Add the item to the ItemtoList Table
		ContentValues values = updateHelper.toValue(item, list);
		if (readHelper.isInList(item, list)) {
			Long listId = list.getId();
			database.update(SQLiteHelper.TABLE_ITEMTOLIST, values,
					SQLiteHelper.COLUMN_ITEM_ID + "= ? AND "
							+ SQLiteHelper.COLUMN_LIST_ID + "=?", new String[] {
							"" + item.getId(), "" + listId });
		} else {
			database.insert(SQLiteHelper.TABLE_ITEMTOLIST, null, values);
		}

	}

	@Override
	public void remove(Item item, ShoppingList list) {
		Long listId = list.getId();
		if (readHelper.isInList(item, list)) {
			database.delete(SQLiteHelper.TABLE_ITEMTOLIST,
					SQLiteHelper.COLUMN_ITEM_ID + "=? AND "
							+ SQLiteHelper.COLUMN_LIST_ID + "=?", new String[] {
							"" + item.getId(), "" + listId });
		}
	}

	@Override
	public void save(Item item) {
		ContentValues values = updateHelper.toValue(item);
		long id = 0;
		if (readHelper.isInList(item)) {
			id = item.getId();
			database.update(SQLiteHelper.TABLE_ITEMS, values,
					SQLiteHelper.COLUMN_ITEM_ID + "=? ",
					new String[] { "" + id });
		} else {
			id = database.insert(SQLiteHelper.TABLE_ITEMS, null, values);
			item.setId(id);
		}
	}

	/**
	 * Returns item with the id
	 * 
	 * @param itemId
	 * @return item with the itemId, if not found, null
	 */
	public Item getItem(long itemId) {
		// TODO: rewrite code when problem is solved with shop and date in item
		// db
		List<Item> itemList = getAllItems();
		for (Item item : itemList)
			if (item.getId() == itemId)
				return item;
		return null;
	}

	@Override
	public void remove(Item item) {
		if (readHelper.isInList(item)) {
			database.delete(SQLiteHelper.TABLE_ITEMTOLIST,
					SQLiteHelper.COLUMN_ITEM_ID + "=? ", new String[] { ""
							+ item.getId() });

			database.delete(SQLiteHelper.TABLE_ITEMS,
					SQLiteHelper.COLUMN_ITEM_ID + "=? ", new String[] { ""
							+ item.getId() });
		}
	}

	/**
	 * Everything for friends
	 */

	@Override
	public ArrayList<Friend> getFriends() {
		ArrayList<Friend> list = new ArrayList<Friend>();

		Cursor cursor = readHelper.getFriendCursor();
		while (!cursor.isAfterLast()) {
			Friend friend = readHelper.cursorToFriend(cursor);
			list.add(friend);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	@Override
	public long save(Friend friend) {
		// Convert the friend to a ContentValue
		ContentValues values = updateHelper.toValue(friend);
		// If this is a new friend
		if (friend.getId() == null && !readHelper.isInList(friend)) {
			long id = database.insert(SQLiteHelper.TABLE_FRIENDS, null, values);
			friend.setId(id);
		} else if(friend.getId() == null) {
			long id = readHelper.getFriendId(friend.getPhoneNr());
			friend.setId(id);
		} else { // Else if it is an old friend
			database.update(
					SQLiteHelper.TABLE_FRIENDS,
					values,
					SQLiteHelper.COLUMN_FRIEND_ID + "="
							+ friend.getId(), null);
		}
		return friend.getId();
	}
	
	
	@Override
	public void removeFriend(Friend friend) {
		database.delete(SQLiteHelper.TABLE_FRIENDS,
				SQLiteHelper.COLUMN_FRIEND_ID + "=? ", new String[] { ""
						+ friend.getId() });
		database.delete(SQLiteHelper.TABLE_FRIENDSTOLIST, 
				SQLiteHelper.COLUMN_FRIEND_ID + "=?", new String[] { "" 
						+friend.getId() });
	}
	
	public List<Friend> getSharedFriends(ShoppingList list) {
		ArrayList<Friend> sharedFriends = new ArrayList<Friend>();
		
		Cursor cursor = readHelper.getSharedFriendsCursor();
		while (!cursor.isAfterLast()) {
			if(cursor.getLong(0) == list.getId()) {
				long friendId = cursor.getLong(1);
				Friend friend = readHelper.getFriend(friendId);
				if(friend != null)
					sharedFriends.add(friend);
			}	
			cursor.moveToNext();
		}
		cursor.close();
		return sharedFriends;
		
	}

	@Override
	public void save(ShoppingList list, Friend friend) {
		ContentValues values = updateHelper.toValue(list, friend);
		if (!readHelper.isInList(list, friend)) {
			database.insert(SQLiteHelper.TABLE_FRIENDSTOLIST, null, values);
		}
	}
	
	@Override
	public void remove(ShoppingList list, Friend friend) {
		if (readHelper.isInList(list, friend))
			database.delete(SQLiteHelper.TABLE_FRIENDSTOLIST, 
				SQLiteHelper.COLUMN_LIST_ID + "=? AND " + 
				SQLiteHelper.COLUMN_FRIEND_ID + "=?", 
				new String[] { "" + list.getId(), "" + friend.getId() });
	}



	/**
	 * Everything for recipes
	 */
	@Override
	public List<Recipe> readRecipes() {
		List<Recipe> listOfRecipes = new ArrayList<Recipe>();

		Cursor cursor = readHelper.getRecipeCursor();
		while (!cursor.isAfterLast()) {
			Recipe recipe = readHelper.cursorToRecipe(cursor);
			listOfRecipes.add(recipe);
			cursor.moveToNext();
		}
		cursor.close();

		addItemsToRecipes(listOfRecipes);
		return listOfRecipes;
	}

	private void addItemsToRecipes(List<Recipe> listOfRecipes) {
		Cursor cursor = readHelper.getItemToRecipeCursor();

		// TODO: rewrite code, its just a fix. At the moment it would take a lot
		// of resources for big lists
		while (!cursor.isAfterLast()) {
			Item item = getItem(cursor.getLong(1));

			if (item != null)
				for (Recipe recipe : listOfRecipes) {
					if (recipe.getId() == cursor.getLong(0))
						recipe.addItem(item);
				}
			cursor.moveToNext();
		}
	}

	public void save(Recipe recipe) {
		// Convert the Recipe to a ContentValue
		ContentValues values = updateHelper.toValue(recipe);
		// If this is a new recipe
		if (!readHelper.isInList(recipe)) {
			long id = database.insert(SQLiteHelper.TABLE_RECIPES, null, values);
			recipe.setId(id);
		} else { // Else if it is an old recipe
			database.update(SQLiteHelper.TABLE_RECIPES, values,
					SQLiteHelper.COLUMN_RECIPE_ID + "=" + recipe.getId(), null);
		}

		List<Item> ingredients = recipe.getItemList();
		if (ingredients != null && !ingredients.isEmpty())
			saveIngredients(recipe, ingredients);
	}

	private void saveIngredients(Recipe recipe, List<Item> ingredients) {
		for (Item item : ingredients) {
			ContentValues values = updateHelper.toValue(recipe, item);
			if (!readHelper.isInList(item, recipe))
				database.insert(SQLiteHelper.TABLE_ITEMTORECIPE, null, values);
		}
	}

	@Override
	public void remove(Recipe recipe) {
		long recipeNr = readHelper.getRecipeId(recipe.getName());
		if (recipeNr != -1) {
			database.delete(SQLiteHelper.TABLE_RECIPES,
					SQLiteHelper.COLUMN_RECIPE_ID + "=?", new String[] { ""
							+ recipeNr });
			database.delete(SQLiteHelper.TABLE_ITEMTORECIPE,
					SQLiteHelper.COLUMN_RECIPE_ID + "=? ", new String[] { ""
							+ recipeNr });
		}

	}
}
