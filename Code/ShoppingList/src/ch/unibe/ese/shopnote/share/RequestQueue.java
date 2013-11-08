package ch.unibe.ese.shopnote.share;

import java.util.LinkedList;

import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * Every Request that is generated locally gets saved in this queue.
 * It is later forwarded to the RequestSender and then to the Server.
 * 
 * Processing of the answers from the Server is up to the {@link AnswerHandler}.
 *
 */

public class RequestQueue {

	private LinkedList<Request> requests;

	public RequestQueue() {
		this.requests = new LinkedList<Request>();
	}
	
	public void addRequest(Request request) {
		this.requests.add(request);
	}
	
	public Request[] getRequests() {
		return requests.toArray(new Request[requests.size()]);
	}
	
	public void clear() {
		requests.clear();
	}
	
	public void removeRequest(Request request) {
		if(requests.contains(request)) {
			requests.remove(request);
		}
	}
	
}
