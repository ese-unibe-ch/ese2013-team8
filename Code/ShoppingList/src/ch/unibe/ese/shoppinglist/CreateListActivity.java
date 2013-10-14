package ch.unibe.ese.shoppinglist;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.unibe.ese.core.JsonPersistenceManager;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class CreateListActivity extends Activity {

	private ListManager manager;
	private ShoppingList list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_list);
		// hide the action bar on this activity
		getActionBar().hide();
		
		manager = new ListManager(new JsonPersistenceManager(getApplicationContext()));
		list = new ShoppingList(" ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_list, menu);
		return true;
	}
	
	/** Called when the user touches the abort button */
	public void goBack(View view) {
    	Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
	}
	
	/** Called when the user touches the save button */
	public void saveList(View view) {	
		// set name
		EditText textName = (EditText)findViewById(R.id.editTextName);
		String name = textName.getText().toString();
		list.setName(name);
		
		// set due date
		EditText textDate = (EditText)findViewById(R.id.editTextDate);
		Date date;
		try {
			date = new SimpleDateFormat("dd.MM.yyyy").parse(textDate.getText().toString());
			list.setDueDate(date);
		} catch (ParseException e) {
			// TODO: handle exception
		}
		
		// set shop
		EditText textShop = (EditText)findViewById(R.id.editTextShop);
		String shop = textShop.getText().toString();
		list.setShop(shop);

		// save the shopping list
		try {
			manager.addShoppingList(list);
			manager.persist();
		}
		catch (IllegalStateException e){
			// TODO: handle exception
		}
		
    	Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
	}

}
