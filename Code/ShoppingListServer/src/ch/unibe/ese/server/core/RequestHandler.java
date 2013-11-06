package ch.unibe.ese.server.core;

import java.util.ArrayList;

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
	
	/**
	 * Slices up the request packet into single requests
	 * @param requests
	 * @return Answer requests
	 */
	public Request[] handle(Request[] requests) {
		ArrayList<Request> answers = new ArrayList<Request>();
		for (Request r : requests) {
			Request[] answer = this.handle(r);
			for(Request a : answer) {
				answers.add(a);
			}
		}
		return answers.toArray(new Request[answers.size()]);
	}

	/**
	 * This method handles one single request
	 * There is the possibility that it returns multiple requests
	 * @param request
	 * @return Array of Requests
	 */
	public Request[] handle(Request request) {
		switch (request.getType()) {
		case Request.REGISTER_REQUEST:
			System.out.println("\tRegister request");
			this.dbManager.addUser(request);
			return new Request[]{request};
		case Request.FRIEND_REQUEST:
			System.out.println("\tFriend request");
			if(this.dbManager.findUser(request)>-1)
				request.setSuccessful();
			return new Request[]{request};
		case Request.SHARELIST_REQUEST:
			System.out.println("\tShareList request");
			this.dbManager.shareList((ShareListRequest)request);
			return new Request[]{request};
		default:
			return new Request[]{request};
		}
	}

}
