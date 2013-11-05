package ch.unibe.ese.share.requests;

import ch.unibe.ese.core.ShoppingList;

/**
 * Asks the server to share the {@link ch.unibe.ese.core.ShoppingList} specified by {@link ch.unibe.ese.core.ShoppingList.getId()} 
 * with ONE friend. If you want to share it with multiple friends, you need to send multiple {@link ShareListRequest} 
 */
public class ShareListRequest extends Request {

	private static final long serialVersionUID = 538335121131311647L;
	
	private String friendNumber;
	private long listId;

	public ShareListRequest(String phoneNumber, String friendNumber, long listId) {
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
		return Request.SHARELIST_REQUEST;
	}
	
	@Override
	public String toString() {
		if(this.wasSuccessful()) 
			return "Shared List with friend";
		if(this.isHandled())
			return "List is already shared";
		else 
			return "Request wasn't handled";
	}

}
