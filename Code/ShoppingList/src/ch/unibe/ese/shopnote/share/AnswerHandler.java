package ch.unibe.ese.shopnote.share;

import android.content.Context;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.share.requests.FriendRequest;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * Gets passed in a RequestSender to listen to its result
 *
 */

public class AnswerHandler {

	private BaseActivity context;
	private FriendsManager friendsManager;
	private SyncManager syncManager;
	
	public AnswerHandler(Context context) {
		this.context = (BaseActivity) context;
		friendsManager = this.context.getFriendsManager();
		syncManager = this.context.getSyncManager();
	}
	
	public void setRequests(Request... requests) {
		for (Request r: requests) {
			if(r.wasSuccessful()) {
				setConsequences(r);
			} else {
				syncManager.addRequest(r);
			}
		}
	}
	
	private void setConsequences(Request request) {
		switch (request.getType()) {
		case Request.FRIEND_REQUEST:
			long friendId = ((FriendRequest)request).getFriendId();
			friendsManager.setFriendHasApp(friendId);
		case Request.SHARELIST_REQUEST:
			//TODO
		}
	}
	
	public void updateUI() {
		context.refresh();
	}
}
