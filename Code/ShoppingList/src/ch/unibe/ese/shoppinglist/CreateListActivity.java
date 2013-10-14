package ch.unibe.ese.shoppinglist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class CreateListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_list);
		// hide the action bar on this activity
		getActionBar().hide();
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

}
