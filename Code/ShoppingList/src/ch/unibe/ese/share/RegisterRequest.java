package ch.unibe.ese.share;

public class RegisterRequest extends Request {
	
	private static final long serialVersionUID = 8085524766542717980L;

	public RegisterRequest(String phoneNumber) {
		super(phoneNumber);
	}

	@Override
	public int getType() {
		return Request.REGISTER_REQUEST;
	}

}
