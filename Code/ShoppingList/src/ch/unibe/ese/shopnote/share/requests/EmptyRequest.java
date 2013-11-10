package ch.unibe.ese.shopnote.share.requests;


public class EmptyRequest extends Request {

	public EmptyRequest(String phoneNumber) {
		super(phoneNumber);
		this.setHandled();
		this.setSuccessful();
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
