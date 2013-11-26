package ch.unibe.ese.shopnote.core;

import android.app.Application;
import ch.unibe.ese.shopnote.share.SyncManager;

/**
 * Extension of {@link Application} that provides different managers for the
 * shoppinglist application.
 */
public class BaseApplication extends Application {
	private ListManager listManager;
	private FriendsManager friendsManager;
	private SyncManager syncManager;

	void setSyncManager(SyncManager syncManager) {
		this.syncManager = syncManager;
	}

	public SyncManager getSyncManager() {
		return syncManager;
	}

	public ListManager getListManager() {
		return listManager;
	}

	void setListManager(ListManager listManager) {
		this.listManager = listManager;
	}

	public FriendsManager getFriendsManager() {
		return friendsManager;
	}

	void setFriendsManager(FriendsManager friendsManager) {
		this.friendsManager = friendsManager;
	}
}
