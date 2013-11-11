package ch.unibe.ese.shopnote.share.requests;

public class RenameListRequest extends ListChangeRequest {

	private static final long serialVersionUID = -56030446877521615L;
	
	private String newName;
	
	public RenameListRequest(String phoneNumber, long listId, String newName) {
		super(phoneNumber, listId);
		this.newName = newName;
	}
	
	public String getNewName() {
		return this.newName;
	}

}
