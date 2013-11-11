package ch.unibe.ese.shopnote.server.database;

import java.util.ArrayList;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import ch.unibe.ese.shopnote.server.core.RequestContainer;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * Persists all ListChangeRequests that have to be handled by the users and not by the server
 *
 */
public class NeodatisDatabaseManager {

	private SQLiteDatabaseManager sqliteManager;
	private ODB database;
	
	public NeodatisDatabaseManager(SQLiteDatabaseManager sqliteManager) {
		this.sqliteManager = sqliteManager;
		database = ODBFactory.open("shoppinglist.odb");
		System.out.println("Openend Neodatis Database Successful");
	}
	
	/**
	 * Store an array of requests
	 */
	public void storeRequest(Request... requests) {
		for(Request r:requests) {
			this.storeRequest(r);
		}
	}
	
	/**
	 * Store a Request into the right container (each registered user has one)
	 * @param request
	 */
	public void storeRequest(Request request) {
		int userId = sqliteManager.findUser(request);
		if(userId <=-1) {
			System.out.println("Requests for non existent user received");
			return;
		}
		RequestContainer container = getRequestContainer(userId);
		container.addRequest(request);
		database.store(container);
	}
	
	/**
	 * Get all requests stored in the requestcontainer of the user with userId
	 * @param userId
	 * @return all Pending requests of user
	 */
	public ArrayList<Request> getRequestsForUser(int userId) {
		RequestContainer container = getRequestContainer(userId);
		ArrayList<Request> requests = container.popRequests();
		database.store(container);
		return requests;
	}
	
	/**
	 * Gets the RequestContainer from the database
	 * @param userId
	 * @return
	 */
	private RequestContainer getRequestContainer(int userId) {
		IQuery query = new CriteriaQuery(RequestContainer.class, Where.equal("userId", userId));
		Objects<RequestContainer> container = database.getObjects(query);
		if(container.size() >= 2 || container.isEmpty()) {
			throw new IllegalStateException("Not the right amount of Containers stored in the database");
		}
		return container.getFirst();
	}

	/**
	 * Adds a new container for a new user
	 * @param request
	 */
	public void addContainer(int userId) {
		RequestContainer container = new RequestContainer(userId);
		database.store(container);
	}
	
}
