package ch.unibe.ese.shopnote.share.requests.listchange;

public class AddSharedFriendRequest extends ListChangeRequest {

	private static final long serialVersionUID = 5397098567269278909L;
	
	private String friendNumber;
	
	public AddSharedFriendRequest(String phoneNumber, long localListId, String friendNumber) {
		super(phoneNumber, localListId);
		this.friendNumber = friendNumber;
	}
	
	public String getFriendNumber() {
		return friendNumber;
	}

	public void setFriendNumber(String friendNumber) {
		this.friendNumber = friendNumber;
	}

	@Override
	public int getSubType() {
		return ListChangeRequest.ADD_SHARED_FRIEND_REQUEST;
	}

	@Override
	public ListChangeRequest getCopy() {
		return new AddSharedFriendRequest(this.getPhoneNumber(), this.getLocalListId(), this.friendNumber);
	}

}
