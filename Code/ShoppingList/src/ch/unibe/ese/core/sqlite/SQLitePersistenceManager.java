package ch.unibe.ese.core.sqlite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.PersistenceManager;
import ch.unibe.ese.core.ShoppingList;

/**
 * This class provides function to save
 * all lists / items / shops to a SQLite database
 * @author Stephan
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
		this.updateHelper = new SQLiteUpdateHelper(this.database, this.readHelper);
	}

	/**
	 * Everything for lists
	 */
	
	@Override
	public List<ShoppingList> readLists() throws IOException {
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
	public void save(ShoppingList list) throws IOException {
		// Convert the list to a ContentValue
		// Automatically creates a new shop in the database if it doesn't exist
		ContentValues values = updateHelper.toValue(list);
		// If this is a new list
		if (readHelper.getListId(list.getName()) == -1) {
			database.insert(SQLiteHelper.TABLE_LISTS, null, values);
		} else { // Else if it is an old list
			database.update(SQLiteHelper.TABLE_LISTS, values,
					SQLiteHelper.COLUMN_LIST_ID + "=" + readHelper.getListId(list.getName()), null);
		}
	}

	@Override
	public void remove(ShoppingList list) throws IOException {
		if (readHelper.getListId(list.getName()) == -1) {
			throw new IOException();
		} else {
			database.delete(SQLiteHelper.TABLE_LISTS,
					SQLiteHelper.COLUMN_LIST_ID + "=" + readHelper.getListId(list.getName()), null);
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
	
	@Override
	public void save(Item item, ShoppingList list) {
		updateHelper.addItemIfNotExistent(item);
		ContentValues values = updateHelper.toValue(item, list);
		if(readHelper.isInList(item, list)) {
			// The following code needs to be added if we want to add quantities
			// It just updates the entry in the database with the newest values in "values"
			//database.update(SQLiteHelper.TABLE_ITEMTOLIST, values, SQLiteHelper.COLUMN_ITEM_ID+"=? AND "+SQLiteHelper.COLUMN_LIST_ID+"=?", 
			//		new String[] {""+readHelper.getItemId(item.getName()), ""+readHelper.getListId(list.getName())} );
		} else {
			database.insert(SQLiteHelper.TABLE_ITEMTOLIST, null, values);
		}
	}
	
	@Override
	public void remove(Item item, ShoppingList list) throws IOException {
		if(readHelper.isInList(item, list)) {
			database.delete(SQLiteHelper.TABLE_ITEMTOLIST, SQLiteHelper.COLUMN_ITEM_ID+"=? AND "+SQLiteHelper.COLUMN_LIST_ID+"=?", 
					new String[] {""+readHelper.getItemId(item.getName()), ""+readHelper.getListId(list.getName())} );
		} else {
			throw new IOException();
		}
	}
}
