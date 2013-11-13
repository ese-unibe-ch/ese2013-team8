package ch.unibe.ese.shopnote.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ItemRecipeAdapter;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.Recipe;

/**
 * Adapter for autocompletion when you want to add an Item
 *
 */
public class ItemAutocompleteAdapter extends ArrayAdapter<ItemRecipeAdapter> {

        private Context context;
        private ListManager manager;

        
        public ItemAutocompleteAdapter(Context context, int layout, ListManager manager) {
                super(context, layout);
                this.manager = manager;
                initializeArray();
        }
        
        
        private void initializeArray() {
                List<Item> itemList = manager.getAllItems();
                for(Item item: itemList)
                        this.add(new ItemRecipeAdapter(item));
                
                List<Recipe> recipeList = manager.getRecipes();
                for(Recipe recipe : recipeList)
                        this.add(new ItemRecipeAdapter(recipe));
        }
}