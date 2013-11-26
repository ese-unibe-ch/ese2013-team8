package ch.unibe.ese.shopnote.share.requests.listchange;

public class EmptyListChangeRequest extends ListChangeRequest {
	
	private static final long serialVersionUID = -2853222543774607884L;

	public EmptyListChangeRequest(String phoneNumber, long localListId) {
		super(phoneNumber, localListId);
	}

	@Override
	public int getSubType() {
		return ListChangeRequest.EMPTY_REQUEST;
	}

}
