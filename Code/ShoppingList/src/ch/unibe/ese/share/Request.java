package ch.unibe.ese.share;

import java.io.Serializable;

/**
 * Abstract class for Requests
 * Every request needs to send the phone number as primary identification on the server
 * There are 2 flags 'isHandled' and 'wasSuccessful'. Check below for description
 * 
 * @author Stephan
 *
 */

public abstract class Request implements Serializable {
	private static final long serialVersionUID = 6439284234031136995L;
	
	// Define an integer for every Type of request you implement
	public static final int REGISTER_REQUEST = 1;
	public static final int FRIEND_REQUEST = 2;
	
	// Phone Number is always passed with the request
	private String phoneNumber;
	// Tells if the request has been handled by the server
	private boolean isHandled;
	// Tells if the handling was successfull (e.g. user was added / not yet existent)
	private boolean wasSuccessful;
	
	public Request(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.isHandled = false;
		this.wasSuccessful = false;
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
	
	public void setSuccessful() {
		this.wasSuccessful = true;
	}
	
	public boolean wasSuccessful() {
		return this.wasSuccessful;
	}
	
	/**
	 * Get some string representation
	 */
	public String toString() {
		return this.phoneNumber + ", type: " + getType();
	}
	
}
