package ch.unibe.ese.shopnote.server.database;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import ch.unibe.ese.shopnote.server.core.RequestContainer;
import ch.unibe.ese.shopnote.share.requests.Request;

public class NeodatisDatabaseManager {

	private SQLiteDatabaseManager sqliteManager;
	private ODB database;
	
	public NeodatisDatabaseManager(SQLiteDatabaseManager sqliteManager) {
		this.sqliteManager = sqliteManager;
		database = ODBFactory.open("shoppinglist.odb");
	}
	
	public void storeRequest(Request... requests) {
		for(Request r:requests) {
			this.storeRequest(r);
		}
	}
	
	public void storeRequest(Request request) {
		int userId = sqliteManager.findUser(request);
		if(userId <=-1) {
			System.out.println("Requests for non existent user received");
			return;
		}
		RequestContainer container= getRequestContainer(userId);
		container.addRequest(request);
	}
	
	public Request[] getRequestsForUser(int userId) {
		RequestContainer container = getRequestContainer(userId);
		return container.getRequests();
	}
	
	private RequestContainer getRequestContainer(int userId) {
		IQuery query = new CriteriaQuery(RequestContainer.class, Where.equal("userId", userId));
		Objects<RequestContainer> container = database.getObjects(query);
		if(container.size() >= 2) {
			throw new IllegalStateException("No more than one container per user allowed");
		}
		return container.getFirst();
	}
	
}
