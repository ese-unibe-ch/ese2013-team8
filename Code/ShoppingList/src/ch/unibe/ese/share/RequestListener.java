package ch.unibe.ese.share;

import ch.unibe.ese.share.requests.Request;

/**
 * Gets passed in a RequestSender to listen to its result
 * 
 * @author Stephan
 *
 */

public class RequestListener {

	private Request[] request;
	
	public RequestListener(Request... request) {
		this.request = request;
	}
	
	public void setHandled(int index) {
		request[index].setHandled();
	}
	
	public boolean isHandled(int index) {
		return request[index].isHandled();
	}
	
	public void setSuccessful(int index) {
		request[index].setSuccessful();
	}
	
	public boolean wasSuccessful(int index) {
		return request[index].wasSuccessful();
	}
	
}
