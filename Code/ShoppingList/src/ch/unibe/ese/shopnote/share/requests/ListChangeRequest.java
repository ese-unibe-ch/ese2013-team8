package ch.unibe.ese.shopnote.share.requests;

/**
 * This class represents every request that has not to be handled by the server but by the client that will receive it
 *
 */
public abstract class ListChangeRequest extends Request {

	private static final long serialVersionUID = -8582076542699084542L;

	public static final int RENAME_LIST_REQUEST = 100;
	
	private long localListId;
	
	public ListChangeRequest(String phoneNumber, long localListId) {
		super(phoneNumber);
		this.localListId = localListId;
	}

	@Override
	public int getType() {
		return Request.LIST_CHANGE_REQUEST;
	}
	
	public void setLocaListId(long id) {
		this.localListId = id;
	}
	
	public long getLocalListId() {
		return this.localListId;
	}

}
