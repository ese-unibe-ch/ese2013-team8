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
 * @author Stephan
 *
 */

public class RequestListener {

	private Request[] requests;
	private BaseActivity context;
	private FriendsManager friendManager;
	
	public RequestListener(Context context, Request... requests) {
		this.requests = requests;
		this.context = (BaseActivity) context;
		friendManager = this.context.getFriendsManager();
	}
	
	public void setHandled(int index) {
		requests[index].setHandled();
	}
	
	public boolean isHandled(int index) {
		return requests[index].isHandled();
	}
	
	public void setSuccessful(int index) {
		requests[index].setSuccessful();
		setConsequences(requests[index]);
	}

	public boolean wasSuccessful(int index) {
		return requests[index].wasSuccessful();
	}
	
	private void setConsequences(Request request) {
		switch (request.getType()) {
		case Request.FRIEND_REQUEST:
			Friend friend = ((FriendRequest) request).getFriend();
			friendManager.setFriendHasApp(friend);
		case Request.SHARELIST_REQUEST:
			//TODO
		}
	}
	
	public void updateUI() {
		context.refresh();
	}
}
