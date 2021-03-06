package ch.unibe.ese.shopnote.share;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.share.requests.RegisterRequest;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * Main Class of the share package
 * It manages the synchronisation between the server and the client
 *
 */

public class SyncManager {

	private static SyncManager instance;
	private RequestQueue rQueue;
	private boolean waitForTask;
	
	
	public SyncManager() {
		instance = this;
		this.rQueue = new RequestQueue();
		this.waitForTask = false;
	}
	
	public static SyncManager getInstance() {
		return instance;
	}
	
	public RequestQueue getQueue() {
		return this.rQueue;
	}
	
	
	public synchronized void synchroniseAndWaitForTaskToEnd(BaseActivity context) {
		this.waitForTask = true;
		this.synchronise(context);
		this.waitForTask = false;
	}

	public synchronized void synchronise(BaseActivity context) {
		if (context.isOnline()) {
			context.isSyncing(true);
			// To be sure, that the client is registered on the server
//			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//			long timeOfApprovement = settings.getLong("timeOfApprovement", 0);
			rQueue.addFirst(new RegisterRequest(context.getMyPhoneNumber(), 0));

			AnswerHandler handler = new AnswerHandler(context);
			RequestSender sender = new RequestSender(handler);
			sender.execute(rQueue.getRequests());
			if (waitForTask)
				waitForTask(sender);
			rQueue.clear();
		}
	}

	private void waitForTask(RequestSender sender) {
		try {
			sender.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			System.err.println("Connection was interrupted");
		} catch (ExecutionException e) {
			System.err.println("Error during the execution");
		} catch (TimeoutException e) {
			System.err.println("Connection timeout");
		}
		
	}

	public void addRequest(Request request) {
		this.rQueue.addRequest(request);
	}
}
