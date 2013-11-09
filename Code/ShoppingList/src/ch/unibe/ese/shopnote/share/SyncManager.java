package ch.unibe.ese.shopnote.share;

import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * Main Class of the share package
 * It manages the synchronisation between the server and the client
 *
 */

public class SyncManager {

	private static SyncManager instance;
	
	private RequestQueue rQueue;
	
	public SyncManager() {
		instance = this;
		this.rQueue = new RequestQueue();
	}
	
	public static SyncManager getInstance() {
		return instance;
	}
	
	public RequestQueue getQueue() {
		return this.rQueue;
	}
	
	public void synchronise(BaseActivity context) {
		if(context.isOnline()) {
			RequestListener listener = new RequestListener(context, rQueue.getRequests());
			RequestSender sender = new RequestSender(listener);
			sender.execute(rQueue.getRequests());
			rQueue.clear();
		}
	}
	
	public void addRequest(Request request) {
		this.rQueue.addRequest(request);
	}
}
