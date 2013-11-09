package ch.unibe.ese.shopnote.core;

import ch.unibe.ese.shopnote.core.sqlite.SQLitePersistenceManager;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.RegisterRequest;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Extension of {@link Activity} that allows easy access to the global managers
 * (like {@link ListManager}, {@link FriendsManager} or {@link SyncManager}.
 */
public class BaseActivity extends Activity {
	
	public static final String EXTRAS_LIST_ID = "listId";
	public static final String EXTRAS_ITEM_ID = "itemId";
	public static final String EXTRAS_ITEM_NAME = "itemName";
	public static final String EXTRAS_ITEM_EDIT= "itemEdit";
	public static final String EXTRAS_RECIPE_ID = "recipeId";
	public static final String EXTRAS_FRIEND_ID = "friendId";
	public static final String EXTRAS_FRIEND_NAME= "friendName";
	

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
			// Dirty:
			manager.addRequest(new RegisterRequest(getMyPhoneNumber()));
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
	
	/**
	 * Checks if the android device has a connection to the internet
	 * 
	 * @return true if it is online
	 */
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public String getMyPhoneNumber() {
		TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tMgr.getLine1Number();
		return number;
	}
	
	/**
	 * Needs to be implemented by every activity
	 * Refreshes all the content it has
	 */
	public void refresh() {};
	    
	 /**
	 * Force opens the soft keyboard
	 */
	public void openKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	/**
	 * Force closes the soft keyboard
	 */
	public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
	@Override
	public void finish() {
		closeKeyboard();
		super.finish();
	}
}
