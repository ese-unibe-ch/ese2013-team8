package ch.unibe.ese.share;

/**
 * Gets passed in a RequestSender to listen to its result
 * 
 * @author Stephan
 *
 */

public class RequestListener {

	private boolean wasSuccessful;
	private boolean isHandled;
	
	public RequestListener() {
		this.wasSuccessful = false;
		this.isHandled = false;
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
	
}
