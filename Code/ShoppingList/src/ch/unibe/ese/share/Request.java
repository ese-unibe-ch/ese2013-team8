package ch.unibe.ese.share;

public abstract class Request {

	private String phoneNumber;
	
	public Request(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public String toString() {
		return this.phoneNumber;
	}
	
}
