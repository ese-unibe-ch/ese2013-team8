package ch.unibe.ese.share;

import ch.unibe.ese.share.requests.Request;
import android.content.Context;

/**
 * Main Class of the share package
 * It manages the synchronisation between the server and the client
 *
 */

public class SyncManager {

	public static SyncManager instance;
	private RequestQueue rQueue;
	private AnswerHandler aHandler;
	
	public SyncManager() {
		this.instance = this;
		this.rQueue = new RequestQueue();
	}
	
	public static SyncManager getInstance() {
		return instance;
	}
	
	public RequestQueue getQueue() {
		return this.rQueue;
	}
	
	public void synchronise(Context context) {
		RequestListener listener = new RequestListener(rQueue.getRequests());
		RequestSender sender = new RequestSender(context, listener);
		sender.execute(rQueue.getRequests());
		rQueue.clear();
	}
	
	public void addRequest(Request request) {
		this.rQueue.addRequest(request);
	}
}
