package ch.unibe.ese.shopnote.share.requests.listchange;

import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * This class represents every request that has not to be handled by the server but by the client that will receive it
 *
 */
public abstract class ListChangeRequest extends Request {

	private static final long serialVersionUID = -8582076542699084542L;

	public static final int RENAME_LIST_REQUEST = 100;
	public static final int ITEM_REQUEST = 110;
	public static final int SET_UNSHARED_REQUEST = 120;
	
	private long localListId;
	private boolean listIdPending;
	
	public ListChangeRequest(String phoneNumber, long localListId) {
		super(phoneNumber);
		this.localListId = localListId;
		this.listIdPending = false;
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
	
	public void setListIdPending(boolean b) {
		this.listIdPending = b;
	}
	
	public boolean islistIdPending() {
		return this.listIdPending;
	}

	public abstract int getSubType();

	public abstract ListChangeRequest getCopy();
	
}
