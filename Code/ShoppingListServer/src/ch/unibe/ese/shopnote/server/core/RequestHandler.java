package ch.unibe.ese.shopnote.server.core;

import java.util.ArrayList;

import ch.unibe.ese.shopnote.server.database.NeodatisDatabaseManager;
import ch.unibe.ese.shopnote.server.database.SQLiteDatabaseManager;
import ch.unibe.ese.shopnote.share.requests.Request;
import ch.unibe.ese.shopnote.share.requests.ShareListRequest;
import ch.unibe.ese.shopnote.share.requests.UnShareListRequest;
import ch.unibe.ese.shopnote.share.requests.CreateSharedListRequest;

/**
 * Forwarding and filtering of Requests
 *
 */
public class RequestHandler {

	private SQLiteDatabaseManager dbManager;
	private NeodatisDatabaseManager odbManager;
	
	public RequestHandler() {
		this.dbManager = new SQLiteDatabaseManager();
		this.odbManager = new NeodatisDatabaseManager(dbManager);
		dbManager.setOdbManager(odbManager);
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
			return returnRequests(request);
			
		case Request.FRIEND_REQUEST:
			System.out.println("\tFriend request");
			if(this.dbManager.findUser(request)>-1)
				request.setSuccessful();
			return new Request[]{request};
			
		case Request.SHARELIST_REQUEST:
			System.out.println("\tShareList request");
			this.dbManager.shareList((ShareListRequest)request);
			return returnRequests(request);
			
		case Request.UNSHARELIST_REQUEST:
			System.out.println("\tUnShareList request");
			this.dbManager.unShareList((UnShareListRequest)request);
			return returnRequests(request);
			
		case Request.CREATE_SHARED_LIST_REQUEST:
			System.out.println("\tCreate Share List Request answer");
			this.dbManager.assignLocalToServerListId((CreateSharedListRequest)request);
			return returnRequests(request);
			
		case Request.LIST_CHANGE_REQUEST:
			System.out.println("\tGeneral list change requests");
			odbManager.storeRequest(request);
			return returnRequests(request);
			
		default:
			return returnRequests(request);
		}
	}
	
	public Request[] returnRequests(Request request) {
		int userId = dbManager.findUser(request);
		ArrayList<Request> requests = odbManager.getRequestsForUser(userId);
		requests.add(request);
		return requests.toArray(new Request[requests.size()]);
	}

}
