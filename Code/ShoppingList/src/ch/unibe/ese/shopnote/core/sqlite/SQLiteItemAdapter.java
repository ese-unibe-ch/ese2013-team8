package ch.unibe.ese.shopnote.core.sqlite;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ListManager;


public class SQLiteItemAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ListManager manager;
	
	public SQLiteItemAdapter(Context context, int layout, ListManager manager) {
		super(context, layout);
		this.manager = manager;
		initializeArray();
	}
	
	public SQLiteItemAdapter(Context context, int layout, List<Item> itemList) {
		super(context, layout);
		this.manager = null;
		this.addAll(itemList);
	}
	
	private void initializeArray() {
		List<Item> itemList = manager.getAllItems();
		
		for(Item item: itemList)
			this.add(item);
	}

	public boolean contains(Item item) {
		return this.getPosition(item) > 0;
	}
}
