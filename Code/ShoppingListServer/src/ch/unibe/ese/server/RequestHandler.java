package ch.unibe.ese.server;

import ch.unibe.ese.share.Request;

public class RequestHandler {

	private DatabaseManager dbManager;
	
	public RequestHandler() {
		this.dbManager = new DatabaseManager();
	}
	
	public void handle(Request[] request) {
		int length = request.length;
		for (Request r : request) {
			this.handle(r);
		}
	}

	public void handle(Request r) {
		switch (r.getType()) {
		case Request.REGISTER_REQUEST:
			this.dbManager.addUser(r.getPhoneNumber());
		}
		
	}

}
