package ch.unibe.ese.shopnote.share.requests;

/**
 * Do the reverse of the ShareListRequest: <br>
 * Ask the server to delete the entry where your friend is matched to your shopping list
 *
 */
public class UnShareListRequest extends Request {

	private static final long serialVersionUID = 3578967523359694420L;
	private String friendNumber;
	private long listId;

	public UnShareListRequest(String phoneNumber, String friendNumber, long listId) {
		super(phoneNumber);
		this.friendNumber = friendNumber;
		this.listId = listId;
	}

	public String getFriendNumber() {
		return this.friendNumber;
	}
	
	public long getListId() {
		return this.listId;
	}
	
	public void setListId(long id) {
		this.listId = id;
	}
	
	public void setFriend(String friendNumber) {
		this.friendNumber = friendNumber;
	}
	
	@Override
	public int getType() {
		return Request.UNSHARELIST_REQUEST;
	}
	
	@Override
	public String toString() {
		if(this.wasSuccessful()) 
			return "Deleted friend from shared list";
		if(this.isHandled())
			return "This friend is not registered with this list";
		else 
			return "Request wasn't handled";
	}


}
