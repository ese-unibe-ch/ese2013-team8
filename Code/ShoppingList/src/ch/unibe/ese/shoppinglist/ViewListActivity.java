package ch.unibe.ese.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;

public class ViewListActivity extends BaseActivity {

	private ListManager manager;
	private ShoppingList list;
	private int listIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		setContentView(R.layout.activity_view_list);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		manager = getListManager();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Get list
			listIndex = extras.getInt("selectedList");
			list = manager.getShoppingLists().get(listIndex);
			setTitle(list.getName());
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	/** Called when the user touches the add item button */
	public void addItem(View view) {
	  	Intent intent = new Intent(this, CreateItemActivity.class);
	  	EditText textName = (EditText) findViewById(R.id.editTextName);
		String name = textName.getText().toString();
    	intent.putExtra("Item", name);
    	intent.putExtra("selectedList", listIndex);
        this.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		}
		return super.onOptionsItemSelected(item);
	}

}
