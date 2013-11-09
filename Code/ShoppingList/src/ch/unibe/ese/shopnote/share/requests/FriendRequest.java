package ch.unibe.ese.shopnote.share.requests;

import ch.unibe.ese.shopnote.core.Friend;

/**
 * Ask the server if your friend has the app
 *
 */
public class FriendRequest extends Request {

	private static final long serialVersionUID = -2582241373391195362L;
	private Friend friend;

	public FriendRequest(Friend friend) {
		super(friend.getPhoneNr());
		this.friend = friend;
	}

	@Override
	public int getType() {
		return Request.FRIEND_REQUEST;
	}
	
	public Friend getFriend() {
		return this.friend;
	}

	@Override
	public String toString() {
		if(this.wasSuccessful()) 
			return "Your friend has the app";
		if(this.isHandled())
			return "Your friend does not have the app";
		else 
			return "Request wasn't handled";
	}

}
