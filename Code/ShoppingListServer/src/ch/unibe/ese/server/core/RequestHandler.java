package ch.unibe.ese.server.core;

import ch.unibe.ese.server.database.NeodatisDatabaseManager;
import ch.unibe.ese.server.database.SQLiteDatabaseManager;
import ch.unibe.ese.share.requests.Request;
import ch.unibe.ese.share.requests.ShareListRequest;

/**
 * Forwarding and filtering of Requests
 *
 */
public class RequestHandler {

	private SQLiteDatabaseManager dbManager;
	private NeodatisDatabaseManager odbManager;
	
	public RequestHandler() {
		// Manages users and connections between users
		this.dbManager = new SQLiteDatabaseManager();
		// Manages requests (objects)
		this.odbManager = new NeodatisDatabaseManager();
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
			System.out.println("\tRegister request");
			this.dbManager.addUser(request);
			return request;
		case Request.FRIEND_REQUEST:
			System.out.println("\tFriend request");
			if(this.dbManager.findUser(request)>-1)
				request.setSuccessful();
			return request;
		case Request.SHARELIST_REQUEST:
			System.out.println("\tShareList request");
			this.dbManager.shareList((ShareListRequest)request);
			return request;
		default:
			return request;
		}
	}

}
