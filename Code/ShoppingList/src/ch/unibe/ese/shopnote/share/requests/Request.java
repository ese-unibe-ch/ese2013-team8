package ch.unibe.ese.shopnote.share.requests;

import java.io.Serializable;

/**
 * Abstract class for Requests
 * Every request needs to send the phone number as primary identification on the server
 * There are 2 flags 'isHandled' and 'wasSuccessful'. Check below for description
 *
 */

public abstract class Request implements Serializable {
	
	private static final long serialVersionUID = 8753041839526964200L;
	
	// Define an integer for every Type of request you implement
	// Dummy request that only servers as an answer with no content
	public static final int EMPTY_REQUEST = 0;
	// Server organization requests
	public static final int REGISTER_REQUEST = 1;
	public static final int FRIEND_REQUEST = 2;
	public static final int SHARELIST_REQUEST = 3;
	public static final int UNSHARELIST_REQUEST = 4;
	public static final int CREATE_SHARED_LIST_REQUEST = 5;
	// Client organisation requests
	public static final int LIST_CHANGE_REQUEST = 6;
	public static final int GET_SHARED_FRIENDS_REQUEST = 7;
	
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
	
	/**
	 * Set the Request as handled by the server
	 */
	public void setHandled() {
		this.isHandled = true;
	}
	
	/**
	 * Ask if the request is already handled by the server
	 * @return true if it is handled
	 */
	public boolean isHandled() {
		return this.isHandled;
	}
	
	/**
	 * Set the request successful, if the server processed it (successfully)
	 */
	public void setSuccessful() {
		this.wasSuccessful = true;
		this.setHandled();
	}
	
	/**
	 * Ask if the request was successfully handled by the server
	 * @return true if it was successful
	 */
	public boolean wasSuccessful() {
		return this.wasSuccessful;
	}
}
