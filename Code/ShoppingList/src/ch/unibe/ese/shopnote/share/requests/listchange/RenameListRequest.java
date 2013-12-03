package ch.unibe.ese.shopnote.share.requests.listchange;

/**
 * This request asks the user to rename his Shared list
 *
 */
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

	@Override
	public int getSubType() {
		return ListChangeRequest.RENAME_LIST_REQUEST;
	}

	@Override
	public ListChangeRequest getCopy() {
		return new RenameListRequest(this.getPhoneNumber(), this.getLocalListId(), this.newName);
	}

}
