package ch.unibe.ese.shopnote.server.core;

import java.util.ArrayList;

import ch.unibe.ese.shopnote.share.requests.EmptyRequest;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * This class contains all requests for a specific user specified by the userId;
 * It gets stored in the Neodatis object database
 */
public class RequestContainer {

	private ArrayList<Request> requests;
	private int userId;
	
	public RequestContainer(int userId) {
		this.userId = userId;
		this.requests = new ArrayList<Request>();
		// Make sure the list is never empty
		requests.add(new EmptyRequest(""));
	}
	
	/**
	 * Add a request to the container
	 * @param request
	 */
	public void addRequest(Request request) {
		requests.add(request);
	}
	
	/**
	 * Get all requests from the container without deleting them
	 * @return
	 */
	public Request[] getRequests() {
		return requests.toArray(new Request[requests.size()]);
	}
	
	/**
	 * Get and delete all requests from the container
	 * @return
	 */
	public Request[] popRequests() {
		Request[] requests = getRequests();
		cleanContainer();
		return requests;
	}
	
	/**
	 * Delete all requests from the container
	 */
	public void cleanContainer() {
		this.requests.clear();
	}
	
	/**
	 * Get the userId which the container belongts to
	 * @return
	 */
	public int getUserId() {
		return userId;
	}
	
}
