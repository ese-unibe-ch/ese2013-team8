package ch.unibe.ese.core.sqlite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		database.execSQL("insert into " + SQLiteHelper.TABLE_LISTS + "values (1,hello)");
	}

	@Override
	public List<ShoppingList> read() throws IOException {
		List<ShoppingList> lists = new ArrayList<ShoppingList>();
		
		Cursor cursor = database.query(SQLiteHelper.TABLE_LISTS,
			        allColumns, null, null, null, null, null);
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
		ShoppingList list = new ShoppingList("");
		list.setId(cursor.getInt(0));
		list.setName(cursor.getString(1));
		return list;
	}

	@Override
	public void save(List<ShoppingList> lists) throws IOException {
		// TODO Auto-generated method stub

	}

}
