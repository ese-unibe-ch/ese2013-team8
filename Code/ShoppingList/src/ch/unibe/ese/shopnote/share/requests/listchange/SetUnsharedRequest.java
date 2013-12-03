package ch.unibe.ese.shopnote.share.requests.listchange;

/**
 * The way the server tells a user that he has been deleted from a shared list. <br>
 * The user now sets the List on unshared.
 */
public class SetUnsharedRequest extends ListChangeRequest {
	
	private static final long serialVersionUID = -3418224817912572885L;

	public SetUnsharedRequest(String phoneNumber, long localListId) {
		super(phoneNumber, localListId);
		this.setHandled();
		this.setSuccessful();
	}

	@Override
	public int getSubType() {
		return ListChangeRequest.SET_UNSHARED_REQUEST;
	}

	@Override
	public ListChangeRequest getCopy() {
		return new SetUnsharedRequest(this.getPhoneNumber(), this.getLocalListId());
	}

}
