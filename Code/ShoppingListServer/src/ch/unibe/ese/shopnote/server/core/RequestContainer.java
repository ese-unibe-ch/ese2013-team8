package ch.unibe.ese.shopnote.server.core;

import java.util.ArrayList;

import ch.unibe.ese.shopnote.share.requests.EmptyRequest;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * This class contains all requests for one user specified by the userId;
 * It gets stored in the Neodatis object database
 */
public class RequestContainer {

	private ArrayList<Request> requests;
	private int userId;
	
	public RequestContainer(int userId) {
		this.userId = userId;
		this.requests = new ArrayList<Request>();
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
	public ArrayList<Request> getRequests() {
		return requests;
	}
	
	/**
	 * Get and delete all requests from the container
	 * @return
	 */
	public ArrayList<Request> popRequests() {
		ArrayList<Request> temp = getRequests();
		cleanContainer();
		return temp;
	}
	
	/**
	 * Delete all requests from the container
	 */
	public void cleanContainer() {
		this.requests = new ArrayList<Request>();
	}
	
	/**
	 * Get the userId which the container belongts to
	 * @return
	 */
	public int getUserId() {
		return userId;
	}
	
}
