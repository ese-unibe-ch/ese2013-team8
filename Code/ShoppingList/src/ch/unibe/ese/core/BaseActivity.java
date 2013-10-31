package ch.unibe.ese.core;

import ch.unibe.ese.core.sqlite.SQLitePersistenceManager;
import android.app.Activity;
import android.widget.Toast;

/**
 * Extension of {@link Activity} that allowes easy access to the global managers
 * (like {@link ListManager} or {@link FriendsManager}.
 */
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

	public FriendsManager getFriendsManager() {
		BaseApplication app = (BaseApplication) this.getApplication();
		FriendsManager manager = app.getFriendsManager();
		if (manager == null) {
			manager = new FriendsManager(new SQLitePersistenceManager(
					getApplicationContext()));
			app.setFriendsManager(manager);
		}
		return manager;
	}
	
	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
