package ch.unibe.ese.share;

public class ShareRequest extends Request {

	private String friendNumber;
	
	public ShareRequest(String phoneNumber) {
		super(phoneNumber);
	}

	@Override
	public int getType() {
		return Request.SHARE_REQUEST;
	}
	
}
