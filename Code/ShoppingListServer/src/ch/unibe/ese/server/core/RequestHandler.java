package ch.unibe.ese.server.core;

import ch.unibe.ese.server.database.DatabaseManager;
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
		switch (request.getType()) {
		case Request.REGISTER_REQUEST:
			this.dbManager.addUser(request);
		}
		return request;
	}

}
