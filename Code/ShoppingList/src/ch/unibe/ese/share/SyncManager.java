package ch.unibe.ese.share;

import ch.unibe.ese.share.requests.Request;

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
	
	public void synchronise() {
		RequestListener listener = new RequestListener(rQueue.getRequests());
		RequestSender sender = new RequestSender(listener);
		sender.execute(rQueue.getRequests());
		rQueue.clear();
	}
	
	public void addRequest(Request request) {
		this.rQueue.addRequest(request);
	}
}
