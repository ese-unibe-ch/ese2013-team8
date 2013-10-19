package ch.unibe.ese.core.sqlite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import ch.unibe.ese.core.PersistenceManager;
import ch.unibe.ese.core.ShoppingList;

public class SQLitePersistenceManager implements PersistenceManager {

	private final Context context;
	// Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = { SQLiteHelper.COLUMN_LIST_ID,
			SQLiteHelper.COLUMN_LIST_NAME };

	public void close() {
		dbHelper.close();
	}

	public SQLitePersistenceManager(Context applicationContext) {
		if (applicationContext == null)
			throw new IllegalArgumentException("null is not allowed");
		this.context = applicationContext;
		this.dbHelper = new SQLiteHelper(this.context);
		this.database = dbHelper.getWritableDatabase();
	}

	@Override
	public List<ShoppingList> read() throws IOException {
		List<ShoppingList> lists = new ArrayList<ShoppingList>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_LISTS, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ShoppingList list = cursorToShoppingList(cursor);
			lists.add(list);
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
	}

	private ShoppingList cursorToShoppingList(Cursor cursor) {
		ShoppingList list = new ShoppingList(" ");
		list.setId(cursor.getInt(0));
		list.setName(cursor.getString(1));
		return list;
	}

	@Override
	public void save(ShoppingList list) throws IOException {
		// Get all rows with the id of this list
		Cursor cursor = database.query(SQLiteHelper.TABLE_LISTS,
				allColumns, SQLiteHelper.COLUMN_LIST_ID + "="
						+ list.getId(), null, null, null, null);

		// If this is a new list
		System.out.println(cursor.getCount());
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_LIST_NAME, list.getName());
			database.insert(SQLiteHelper.TABLE_LISTS, null, values);
		// Else if it is an old list
		} else if (cursor.getCount() == 1) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_LIST_NAME, list.getName());
			database.update(SQLiteHelper.TABLE_LISTS, values, SQLiteHelper.COLUMN_LIST_ID + "=" + list.getId(), null);
		// If there is more than one list with this id
		} else {
			throw new IOException();
		}
	}
	
	@Override
	public void remove(ShoppingList list) throws IOException {
		// Get all rows with the id of this list
		Cursor cursor = database.query(SQLiteHelper.TABLE_LISTS,
				allColumns, SQLiteHelper.COLUMN_LIST_ID + "="
						+ list.getId(), null, null, null, null);
		
		if (cursor.getCount() == 1) {
			database.delete(SQLiteHelper.TABLE_LISTS, SQLiteHelper.COLUMN_LIST_ID + "=" + list.getId(), null);
		} else {
			throw new IOException();
		}
	}

}
