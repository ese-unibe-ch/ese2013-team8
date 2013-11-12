package ch.unibe.ese.shopnote.share.requests;

public class CreateSharedListRequest extends Request {

	private static final long serialVersionUID = 7937903634262911605L;
	private long serverListId;
	private long localListId;
	private String listname;
	
	public CreateSharedListRequest(String phoneNumber, long serverListId, String listname) {
		super(phoneNumber);
		this.serverListId = serverListId;
		this.listname = listname;
		this.setSuccessful();
	}

	@Override
	public int getType() {
		return Request.CREATE_SHARED_LIST_REQUEST;
	}

	public long getServerListid() {
		return this.serverListId;
	}
	
	public String getListName() {
		return this.listname;
	}
	
	public void setLocalListId(long id) {
		this.localListId = id;
	}
	
	public long getLocalListId() {
		return this.localListId;
	}
	
}
