package ch.unibe.ese.shopnote.share.requests;


/**
 * Some trivial implementation of the Request class
 * It just forwards the Phonenumber, which is already implemented in the basic Request class
 * 
 */
public class RegisterRequest extends Request {
	
	private static final long serialVersionUID = 8085524766542717980L;

	private long installDate;

	public RegisterRequest(String phoneNumber, long installDate) {
		super(phoneNumber);
		this.installDate = installDate;
	}

	@Override
	public int getType() {
		return Request.REGISTER_REQUEST;
	}
	
	public long getInstallDate() {
		return installDate;
	}

}
