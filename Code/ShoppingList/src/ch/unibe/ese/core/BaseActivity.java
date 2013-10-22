package ch.unibe.ese.core;

import android.app.Activity;

public class BaseActivity extends Activity {

	public ListManager getListManager() {
		BaseApplication app = (BaseApplication) this.getApplication();
		ListManager manager = app.getListManager();
		if (manager == null) {
			manager = new ListManager(new JsonPersistenceManager(
					getApplicationContext()));
			app.setListManager(manager);
		}
		return manager;
	}
}
