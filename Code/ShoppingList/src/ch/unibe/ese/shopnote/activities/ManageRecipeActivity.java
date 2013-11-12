package ch.unibe.ese.shopnote.activities;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.Recipe;
import ch.unibe.ese.shopnote.drawer.NavigationDrawer;
import ch.unibe.ese.shopnote.R;

/**
 *	Creates a frame which enlists all recipes and the possibility to manage them
 */
public class ManageRecipeActivity extends BaseActivity {
	private ListManager manager; 
	private ArrayAdapter<Recipe> recipeAdapter;
	private List<Recipe> recipes;
	private DrawerLayout drawMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_recipe);
		// Show the Up button in the action bar.
		setupActionBar();
		
		manager = getListManager();
		
		// Create drawer menu
		NavigationDrawer nDrawer = new NavigationDrawer();
		drawMenu = nDrawer.constructNavigationDrawer(drawMenu, this);
				
		updateRecipeList();
	}
	
	/**
	 * Updates the Viewlist, which shows all friends and adds itemclicklistener
	 */
	public void updateRecipeList(){
		recipes = manager.getRecipes();
		recipeAdapter = new ArrayAdapter<Recipe>(this,
				R.layout.shopping_list_item, recipes);
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
		
		// Add click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ManageRecipeActivity.this, ViewRecipeActivity.class);
				
				// get recipe Id
				Recipe recipe = recipes.get(position);
				
				intent.putExtra(EXTRAS_RECIPE_ID, recipe.getId());
				ManageRecipeActivity.this.startActivity(intent);
			}
		});
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) 
				updateRecipeList();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

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
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_new:
				Intent createRecipeIntent = new Intent(this, CreateRecipeActivity.class);
				this.startActivityForResult(createRecipeIntent, 1);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
    	drawMenu.closeDrawers();
	}
}
