package ch.unibe.ese.share.requests;

/**
 * Ask the server if your friend has the app
 *
 */
public class FriendRequest extends Request {

	private static final long serialVersionUID = 6609247700424021899L;

	public FriendRequest(String phoneNumber) {
		super(phoneNumber);
	}

	@Override
	public int getType() {
		return Request.FRIEND_REQUEST;
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
