package ch.unibe.ese.server.core;

import ch.unibe.ese.server.database.DatabaseManager;
import ch.unibe.ese.share.FriendRequest;
import ch.unibe.ese.share.RegisterRequest;
import ch.unibe.ese.share.Request;

/**
 * Forwarding and filtering of Requests
 * @author Stephan
 *
 */
public class RequestHandler {

	private DatabaseManager dbManager;
	
	public RequestHandler() {
		this.dbManager = new DatabaseManager();
	}
	
	public Request[] handle(Request[] request) {
		for (Request r : request) {
			this.handle(r);
		}
		return request;
	}

	public Request handle(Request request) {
		System.out.println("Handling Request Type " + request.getType() + " from " + request.getPhoneNumber());
		switch (request.getType()) {
		case Request.REGISTER_REQUEST:
			this.dbManager.addUser(request);
			return request;
		case Request.FRIEND_REQUEST:
			this.dbManager.searchUser((FriendRequest)request);
			return request;
		default:
			return request;
		}
	}

}
