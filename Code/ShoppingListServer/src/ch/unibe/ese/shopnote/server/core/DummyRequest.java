package ch.unibe.ese.shopnote.server.core;

import ch.unibe.ese.shopnote.share.requests.Request;

public class DummyRequest extends Request {

	public DummyRequest(String phoneNumber) {
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
