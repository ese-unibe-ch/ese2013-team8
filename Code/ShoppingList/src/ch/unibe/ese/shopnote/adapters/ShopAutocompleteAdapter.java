package ch.unibe.ese.shopnote.adapters;

import ch.unibe.ese.shopnote.core.sqlite.SQLiteHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

/**
 * AutocompleteAdapter for shops
 *
 */

public class ShopAutocompleteAdapter extends ArrayAdapter<String> {

	private Context context;
	
	public ShopAutocompleteAdapter(Context context, int layout) {
		super(context, layout);
		initializeArray();
	}
	
	private void initializeArray() {
		SQLiteDatabase database = SQLiteHelper.instance.getReadableDatabase();
		Cursor cursor = database.query(SQLiteHelper.TABLE_SHOPS, new String[]{SQLiteHelper.COLUMN_SHOP_NAME}, null, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			this.add(cursor.getString(0));
			cursor.moveToNext();
		}
	}

}
