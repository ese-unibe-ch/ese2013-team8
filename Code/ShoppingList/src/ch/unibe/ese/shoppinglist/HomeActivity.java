package ch.unibe.ese.shoppinglist;

import ch.unibe.ese.core.ListManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HomeActivity extends Activity {
	
	private ListManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		manager = new ListManager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}
}