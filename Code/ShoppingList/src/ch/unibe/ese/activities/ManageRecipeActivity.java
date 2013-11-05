package ch.unibe.ese.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.Recipe;
import ch.unibe.ese.shoppinglist.R;

public class ManageRecipeActivity extends BaseActivity {
	private ListManager manager; 
	private ArrayAdapter<Recipe> recipeAdapter;
	private ArrayList<Recipe> listOfRecipes;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_recipe);
		
		manager = getListManager();
		updateRecipeList();
	}
	
	/**
	 * Updates the Viewlist, which shows all friends and adds itemclicklistener
	 */
	public void updateRecipeList(){
		listOfRecipes = (ArrayList<Recipe>) manager.readRecipes();
		
		recipeAdapter = new ArrayAdapter<Recipe>(this,
				R.layout.shopping_list_item, listOfRecipes );
		ListView listView = (ListView) findViewById(R.id.recipe_list);
		listView.setAdapter(recipeAdapter);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Recipe selectedRecipe = manager.getRecipeAt(position);
				ManageRecipeActivity.this.startActionMode(new RecipeListActionMode(
						manager, selectedRecipe, recipeAdapter, ManageRecipeActivity.this));
				return true;
			}
		});
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) 
				updateRecipeList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_recipe, menu);
		return true;
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_new:
				Intent createRecipeIntent = new Intent(this, CreateRecipeActivity.class);
				this.startActivityForResult(createRecipeIntent, 1);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
