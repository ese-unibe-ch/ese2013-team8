package ch.unibe.ese.shopnote.core.sqlite;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ItemRecipeAdapter;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.Recipe;


public class SQLiteItemAdapter extends ArrayAdapter<ItemRecipeAdapter> {

	private Context context;
	private ListManager manager;

	
	public SQLiteItemAdapter(Context context, int layout, ListManager manager) {
		super(context, layout);
		this.manager = manager;
		initializeArray();
	}
	
	
	private void initializeArray() {
		ArrayList<Item> itemList = manager.getAllItems();
		for(Item item: itemList)
			this.add(new ItemRecipeAdapter(item));
		
		ArrayList<Recipe> recipeList = (ArrayList<Recipe>) manager.getRecipes();
		for(Recipe recipe : recipeList)
			this.add(new ItemRecipeAdapter(recipe));
	}

}
	
