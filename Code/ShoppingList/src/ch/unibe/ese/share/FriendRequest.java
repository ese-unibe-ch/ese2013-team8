package ch.unibe.ese.share;

public class FriendRequest extends Request {
	
	private String friendNumber;
	
	public FriendRequest(String phoneNumber) {
		super(phoneNumber);
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setFriendNumber(String friendNumber) {
		this.friendNumber = friendNumber;
	}
	
	public String getFriendNumber() {
		return this.friendNumber;
	}

}
