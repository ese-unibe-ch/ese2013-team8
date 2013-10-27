package ch.unibe.ese.core;

import android.app.Application;

/**
 * Extension of {@link Application} that provides different managers for the
 * shoppinglist application.
 */
public class BaseApplication extends Application {
	private ListManager listManager;
	private FriendsManager friendsManager;

	public ListManager getListManager() {
		return listManager;
	}

	public void setListManager(ListManager listManager) {
		this.listManager = listManager;
	}

	public FriendsManager getFriendsManager() {
		return friendsManager;
	}

	public void setFriendsManager(FriendsManager friendsManager) {
		this.friendsManager = friendsManager;
	}
}
