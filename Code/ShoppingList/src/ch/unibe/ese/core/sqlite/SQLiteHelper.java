package ch.unibe.ese.core.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	
	public static SQLiteHelper instance;
	// Table definitions
	// Save all Shopping lists with an unique ID
	public static final String TABLE_LISTS = "shoppinglists";
	public static final String COLUMN_LIST_ID = "listid";
	public static final String COLUMN_LIST_NAME = "listname";
	public static final String COLUMN_LIST_DUEDATE = "duedate";
	// Save all Items with an unique name and ID
	public static final String TABLE_ITEMS = "items";
	public static final String COLUMN_ITEM_ID = "itemid";
	public static final String COLUMN_ITEM_NAME = "itemname";
	// Save all shops with an unique name and ID
	public static final String TABLE_SHOPS = "shops";
	public static final String COLUMN_SHOP_ID = "shopid";
	public static final String COLUMN_SHOP_NAME = "shopname";
	// Link items to lists
	public static final String TABLE_ITEMTOLIST = "itemtolist";
	public static final String COLUMN_LISTPRICE = "listprice";
	public static final String COLUMN_ITEMBOUGHT = "itembought";

	private static final String DATABASE_NAME = "shoppinglist.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE_TABLE_LISTS = 
			// create table for lists
			"create table " + TABLE_LISTS + "(" //
			+ COLUMN_LIST_ID + " integer primary key autoincrement, " //
			+ COLUMN_LIST_NAME + " text not null, " //
			+ COLUMN_LIST_DUEDATE + " integer, " //
			+ COLUMN_SHOP_ID + " integer" //
			+ ");";
			// Create table for items
	private static final String DATABASE_CREATE_TABLE_ITEMS = 
			"create table " + TABLE_ITEMS + "("
			+ COLUMN_ITEM_ID + " integer primary key autoincrement, "
			+ COLUMN_ITEM_NAME + " varchar(30) "
			+ ");";
			// link items to lists
	private static final String DATABASE_CREATE_TABLE_ITEMTOLIST =
			"create table " + TABLE_ITEMTOLIST + "("
			+ COLUMN_ITEM_ID + " integer NOT NULL, "
			+ COLUMN_LIST_ID + " integer NOT NULL, "
			+ COLUMN_LISTPRICE + " float, "
			+ COLUMN_ITEMBOUGHT + " integer, "
			+ "primary key (" + COLUMN_ITEM_ID + ", " + COLUMN_LIST_ID + ")"
			+ ");";
			// Create table for Shops
	private static final String DATABASE_CREATE_TABLE_SHOPS =
			"create table " + TABLE_SHOPS + "("
			+ COLUMN_SHOP_ID + " integer primary key autoincrement, "
			+ COLUMN_SHOP_NAME + " varchar(30) "
			+ ");";
	
	
	public static String[] LISTS_COLUMNS = { SQLiteHelper.COLUMN_LIST_ID,
			SQLiteHelper.COLUMN_LIST_NAME, SQLiteHelper.COLUMN_LIST_DUEDATE,
			SQLiteHelper.COLUMN_SHOP_ID };
	public static String[] SHOPS_COLUMNS = { SQLiteHelper.COLUMN_SHOP_ID,
			SQLiteHelper.COLUMN_SHOP_NAME };
	public static String[] ITEMS_COLUMNS = { SQLiteHelper.COLUMN_ITEM_ID,
			SQLiteHelper.COLUMN_ITEM_NAME };
	public static String[] ITEMTOLIST_COLUMNS = {
			SQLiteHelper.COLUMN_ITEM_ID, SQLiteHelper.COLUMN_LIST_ID,
			SQLiteHelper.COLUMN_LISTPRICE };

	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SQLiteHelper.instance = this;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TABLE_LISTS);
		database.execSQL(DATABASE_CREATE_TABLE_ITEMS);
		database.execSQL(DATABASE_CREATE_TABLE_ITEMTOLIST);
		database.execSQL(DATABASE_CREATE_TABLE_SHOPS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
		onCreate(db);
	}

}
