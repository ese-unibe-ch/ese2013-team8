package ch.unibe.ese.core;

import ch.unibe.ese.core.sqlite.SQLitePersistenceManager;
import android.app.Activity;

public class BaseActivity extends Activity {

	public ListManager getListManager() {
		BaseApplication app = (BaseApplication) this.getApplication();
		ListManager manager = app.getListManager();
		if (manager == null) {
			manager = new ListManager(new SQLitePersistenceManager(
					getApplicationContext()));
			app.setListManager(manager);
		}
		return manager;
	}
}
