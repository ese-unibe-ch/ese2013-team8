package ch.unibe.ese.shopnote.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.Recipe;

/**
 * 	Creates a frame to create new recipes or edit them if the intent has an extra
 */
public class CreateRecipeActivity extends BaseActivity {
	private ListManager manager;
	private Recipe recipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_recipe);
        
		//set chosen color theme
		updateColor();
		
		getActionBar().hide();
		
		manager = getListManager();
		
		// Edit Recipe Name
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			long recipeIndex = extras.getLong(EXTRAS_RECIPE_ID);
			recipe = manager.getRecipeAt(recipeIndex);
			setText(recipe);
		} 
		
		openKeyboard();
	}

	private void updateColor() {
		RelativeLayout lo = (RelativeLayout) findViewById(R.id.relativeLayoutRecipe);
		updateThemeTextBox(lo);
		View someView = findViewById(R.id.RelativeLayoutCreateRecipeComplete);
		updateTheme(someView, getActionBar());
		LinearLayout llbuttons = (LinearLayout) findViewById(R.id.buttonBar);
		LinearLayout buttonsShadow = (LinearLayout) findViewById(R.id.buttonShadow);
		LinearLayout buttonsDivider = (LinearLayout) findViewById(R.id.buttonDivider);
		updateSaveAbordButtons(llbuttons, buttonsShadow, buttonsDivider);
	}
	
	/**
	 * enter recipe data in edittext fields if editing a recipe
	 */
	private void setText(Recipe recipe) {
		// Title
		TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
		textViewTitle.setText(this.getString(R.string.edit_recipe_title));
		
		// Input
		EditText recipeName = (EditText) findViewById(R.id.editRecipeName);
		recipeName.setText(recipe.toString());
	}
	
	/** Called when the user touches the abort button */
	public void goBack(View view) {
		finish();
	}

	/** Called when the user touches the save button */
	public void saveRecipe(View view) {
		// get name and change it if necessary
		EditText recipeName = (EditText) findViewById(R.id.editRecipeName);
		String name = recipeName.getText().toString();
		if (name == null || name.trim().isEmpty()) {
			Toast.makeText(this, this.getString(R.string.error_name), Toast.LENGTH_SHORT).show();
			return;
		}
		if (name.charAt(0) != '/')
			name = "/" + name;

		if (recipe == null)
			recipe = new Recipe(name);
		else
			recipe.setName(name);
		
		// save the item
		manager.saveRecipe(recipe);
		
		// open the created recipe
		Intent intent = new Intent(this, ViewRecipeActivity.class);
		intent.putExtra(EXTRAS_RECIPE_ID, recipe.getId());
		this.startActivity(intent);
		finish();
	}
	
	@Override
	public void finish() {
        Intent data = new Intent();
        if(recipe != null)
        	data.putExtra(EXTRAS_RECIPE_ID, recipe.getId());
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
