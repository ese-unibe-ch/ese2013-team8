package ch.unibe.ese.shopnote.share.requests;


public class EmptyRequest extends Request {

	private static final long serialVersionUID = 1649191463132789024L;

	public EmptyRequest(String phoneNumber) {
		super(phoneNumber);
		this.setHandled();
		this.setSuccessful();
	}

	@Override
	public int getType() {
		return 0;
	}
}
