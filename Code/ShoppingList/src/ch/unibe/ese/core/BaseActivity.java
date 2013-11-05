package ch.unibe.ese.core;

import ch.unibe.ese.core.sqlite.SQLitePersistenceManager;
import ch.unibe.ese.share.SyncManager;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Extension of {@link Activity} that allows easy access to the global managers
 * (like {@link ListManager}, {@link FriendsManager} or {@link SyncManager}.
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

	public SyncManager getSyncManager() {
		BaseApplication app = (BaseApplication) this.getApplication();
		SyncManager manager = app.getSyncManager();
		if (manager == null) {
			manager = new SyncManager();
			app.setSyncManager(manager);
		}
		return manager;
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Sets a text to a TextView.
	 * <p>
	 * If the TextView with the given id does not exist, a
	 * {@link NullPointerException} is thrown.
	 * 
	 * @param id
	 *            id of the TextView.
	 * @param value
	 *            The value to set.
	 */
	public void setTextViewText(int id, String value) {
		TextView textView = (TextView) findViewById(id);
		textView.setText(value);
	}

	/**
	 * Gets the text from the TextView with the given id.
	 * <p>
	 * If the TextView with the given id does not exist, a
	 * {@link NullPointerException} is thrown.
	 * 
	 * @param id
	 *            id of the TextView.
	 * @return The text in the view.
	 */
	public String getTextViewText(int id) {
		TextView textView = (TextView) findViewById(id);
		return textView.getText().toString();
	}
}
