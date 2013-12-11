package ch.unibe.ese.shopnote.share.requests;

/**
 * Asks the server to share the {@link ch.unibe.ese.shopnote.core.entities.ShoppingList} specified by {@link ch.unibe.ese.core.ShoppingList.getId()} 
 * with ONE friend. If you want to share it with multiple friends, you need to send multiple {@link ShareListRequest} 
 */
public class ShareListRequest extends Request {

	private static final long serialVersionUID = 538335121131311647L;
	
	private String friendNumber;
	private long listId;
	private String listname;
	private boolean isRecipe;

	public ShareListRequest(String phoneNumber, String friendNumber, long listId, String listname) {
		super(phoneNumber);
		this.friendNumber = friendNumber;
		this.listId = listId;
		this.listname = listname;
		this.isRecipe = false;
	}

	public String getFriendNumber() {
		return this.friendNumber;
	}
	
	public long getListId() {
		return this.listId;
	}
	
	public String getListName() {
		return this.listname;
	}
	
	public void setListId(long id) {
		this.listId = id;
	}
	
	public void setFriend(String friendNumber) {
		this.friendNumber = friendNumber;
	}
	
	public void isRecipe(boolean b) {
		this.isRecipe = b;
	}
	
	public boolean isRecipe() {
		return this.isRecipe;
	}
	
	@Override
	public int getType() {
		return Request.SHARELIST_REQUEST;
	}

}
