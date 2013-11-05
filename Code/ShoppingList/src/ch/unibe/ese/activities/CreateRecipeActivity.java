package ch.unibe.ese.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.Recipe;
import ch.unibe.ese.shoppinglist.R;

public class CreateRecipeActivity extends BaseActivity {
	private ListManager manager;
	private Recipe recipe;
	private ArrayList<Item> itemsOfRecipe;
	private ArrayAdapter<Item> itemAdapter;
	ArrayAdapter<Item> autocompleteAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_recipe);
		
		getActionBar().hide();
		manager = getListManager();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int recipeIndex = extras.getInt("recipeIndex");
			recipe = manager.getRecipeAt(recipeIndex);
			itemsOfRecipe = recipe.getItemList();
			setInput(recipe);
		} else
			itemsOfRecipe = new ArrayList<Item>();
		
		//create or update item list
		updateRecipeList();
		
		//add autocomplete items
		createAutocomplete();		
	}

	private void updateRecipeList() {
		// Get listOfRecipes and put it in the listview	
		itemAdapter = new ArrayAdapter<Item>(this,
				R.layout.shopping_list_item, itemsOfRecipe);
		ListView listView = (ListView) findViewById(R.id.recipeListView);
		listView.setAdapter(itemAdapter);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Item selectedItem = itemAdapter.getItem(position);
				CreateRecipeActivity.this.startActionMode(new RecipeListActionMode(
						CreateRecipeActivity.this.manager, recipe, selectedItem,
						CreateRecipeActivity.this.itemAdapter,
						CreateRecipeActivity.this));
				return true;
			}
		});
	}
	
	private AutoCompleteTextView createAutocomplete() {
		AutoCompleteTextView addItems = (AutoCompleteTextView) findViewById(R.id.editAddItemToRecipe);
		autocompleteAdapter = new ArrayAdapter<Item>(this,
				android.R.layout.simple_list_item_1, manager.getAllItems());
		addItems.setAdapter(autocompleteAdapter);
		
		addItems.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View arg1, int position,
	                long id) {
	        	Item item = (Item) autocompleteAdapter.getItem(position);
	        	if(!itemsOfRecipe.contains(item))
	        		itemsOfRecipe.add(item);
	        	updateRecipeList();
	        	
	        	EditText addItem = (EditText) findViewById(R.id.editAddItemToRecipe);
	        	addItem.setText("");
	        }
	    });
		
		return addItems;
	}
	
	private void setInput(Recipe recipe) {
		EditText recipeName = (EditText) findViewById(R.id.editRecipeName);
		recipeName.setText(recipe.getName());
	}
	
	/** Called when the user touches the abort button */
	public void goBack(View view) {
		super.finish();
	}

	/** Called when the user touches the save button */
	public void saveItem(View view) {
		// get name and change it if necessary
		EditText recipeName = (EditText) findViewById(R.id.editRecipeName);
		String name = recipeName.getText().toString();
		if (name == null || name.trim().isEmpty()) {
			Toast.makeText(this, this.getString(R.string.error_name), Toast.LENGTH_SHORT).show();
			return;
		}

		if (recipe == null)
			recipe = new Recipe(name);
		else
			recipe.setName(name);

		//add items to recipe
		recipe.setItemList(itemsOfRecipe);
		
		// save the item
		manager.saveRecipe(recipe);
		
		// go back to the list
		finish();
	}
	

	public void finish() {
        Intent data = new Intent();
        if(recipe != null)
        	data.putExtra("item", recipe.getId());
        setResult(Activity.RESULT_OK, data); 
        super.finish();
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_recipe, menu);
		return true;
	}

}
