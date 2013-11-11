package ch.unibe.ese.shopnote.share.requests;

import ch.unibe.ese.shopnote.core.Friend;

/**
 * Ask the server if your friend has the app
 *
 */
public class FriendRequest extends Request {

	private static final long serialVersionUID = -2582241373391195362L;
	private Long friendId;

	public FriendRequest(Friend friend) {
		super(friend.getPhoneNr());
		this.friendId = friend.getId();
	}

	@Override
	public int getType() {
		return Request.FRIEND_REQUEST;
	}
	
	public long getFriendId() {
		return this.friendId;
	}
}
