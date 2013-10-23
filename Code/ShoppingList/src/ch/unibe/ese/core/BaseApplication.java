package ch.unibe.ese.core;

import android.app.Application;

public class BaseApplication extends Application {
	private ListManager listManager;

	public ListManager getListManager() {
		return listManager;
	}

	public void setListManager(ListManager listManager) {
		this.listManager = listManager;
	}
}
