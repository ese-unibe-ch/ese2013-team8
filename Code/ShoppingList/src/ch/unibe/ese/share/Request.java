package ch.unibe.ese.share;

import java.io.Serializable;

public abstract class Request implements Serializable {
	private static final long serialVersionUID = 6439284234031136995L;
	
	// Define an integer for every Type of request you implement
	public static final int REGISTER_REQUEST = 1;
	
	// Phone Number is always passed with the request
	private String phoneNumber;
	// Tells if the request has been handled by the server
	private boolean isHandled;
	
	public Request(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.isHandled = false;
	}
	
	/**
	 * Return the class-integer you defined above
	 * @return type as Integer defined in Request
	 */
	public abstract int getType();
	
	/**
	 * Get the phone number of source
	 * @return
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setHandled() {
		this.isHandled = true;
	}
	
	public boolean isHandled() {
		return this.isHandled;
	}
	
	/**
	 * Get some string representation
	 */
	public String toString() {
		return this.phoneNumber + ", type: " + getType();
	}
	
}
