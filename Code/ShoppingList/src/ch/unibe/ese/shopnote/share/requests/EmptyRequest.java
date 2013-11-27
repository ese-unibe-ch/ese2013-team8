package ch.unibe.ese.shopnote.share.requests;

/**
 * This request is like a ping, just to say hello
 *
 */

public class EmptyRequest extends Request {

	private static final long serialVersionUID = 1649191463132789024L;

	public EmptyRequest(String phoneNumber) {
		super(phoneNumber);
		this.setHandled();
		this.setSuccessful();
	}

	@Override
	public int getType() {
		return Request.EMPTY_REQUEST;
	}
}
