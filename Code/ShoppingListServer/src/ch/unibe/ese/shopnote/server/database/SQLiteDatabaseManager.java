package ch.unibe.ese.shopnote.server.database;

import java.sql.*;

import ch.unibe.ese.shopnote.share.requests.RegisterRequest;
import ch.unibe.ese.shopnote.server.core.ShoppingListServer;
import ch.unibe.ese.shopnote.share.requests.CreateSharedListRequest;
import ch.unibe.ese.shopnote.share.requests.Request;
import ch.unibe.ese.shopnote.share.requests.ShareListRequest;
import ch.unibe.ese.shopnote.share.requests.UnShareListRequest;

/**
 * This Class organizes the database on the server.
 * It directly processes the requests on the database concerning Users and the IDs of the shared lists
 * <p>
 * Every Request that has to do with list or item updates goes to the {@link NeodatisDatabaseManager} (object database)
 */

public class SQLiteDatabaseManager {
	// Name definitions
	// User Table
	// All users of the app are registered here
	public static final String TABLE_USERS = "users";
	public static final String COLUMN_USER_ID = "userId";
	public static final String COLUMN_USER_PHONENUMBER = "phoneNumber";
	
	// Links local (not unique) list ids to unique server list ids using (userId, locallistid) as primary key
	public static final String TABLE_LOCALTOSERVER_LIST_ID = "localtoserverlistid";
	public static final String COLUMN_LOCAL_LIST_ID = "locallistid";
	public static final String COLUMN_SERVER_LIST_ID = "serverlistId";
	// Links shared lists to users (unique server list id)
	
	public static final String TABLE_SHAREDLISTS = "sharedlists";
	public static final String COLUMN_FRIEND_ID = "friendId";
	public static final String COLUMN_LIST_NAME = "listname";
	
	// Create statements
	public static final String CREATE_TABLE_USERS = "create table if not exists " + TABLE_USERS + "(" + 
			COLUMN_USER_ID + " integer primary key autoincrement, " +
			COLUMN_USER_PHONENUMBER + " varchar(20)" +
			");";
	public static final String CREATE_TABLE_LOCALTOSERVER_LIST_ID = "create table if not exists " + TABLE_LOCALTOSERVER_LIST_ID + "(" +
			COLUMN_USER_ID + " integer, " +
			COLUMN_LOCAL_LIST_ID + " integer, " +
			COLUMN_SERVER_LIST_ID + " integer default 0, " +
			"primary key(" + COLUMN_USER_ID + ", " + COLUMN_LOCAL_LIST_ID + ")" +
			");";
	public static final String CREATE_TABLE_SHAREDLISTS = "create table if not exists " + TABLE_SHAREDLISTS + "(" +
			COLUMN_USER_ID + " integer, " +
			COLUMN_FRIEND_ID + " integer, " +
			COLUMN_SERVER_LIST_ID + " integer, " +
			COLUMN_LIST_NAME + " varchar(30), " +
			"primary key(" + COLUMN_USER_ID + ", " + COLUMN_FRIEND_ID + ", " + COLUMN_SERVER_LIST_ID + ")" +
			");";
	// First dummy entry for localtoserver list id
	public static final String INSERT_DUMMY = "insert into " + TABLE_LOCALTOSERVER_LIST_ID + " values ( -1, -2, 0);";
	// Drop all Tables statement
	private static final String DROP_TABLE_USERS = "drop table if exists " + TABLE_USERS + ";";
	private static final String DROP_TABLE_SHAREDLISTS = "drop table if exists " + TABLE_SHAREDLISTS + ";";
	private static final String DROP_TABLE_LOCALTOSERVER_LIST_ID = "drop table if exists " + TABLE_LOCALTOSERVER_LIST_ID + ";";
	
	// instance variables
	private Connection c;
	
	public SQLiteDatabaseManager() {
		this.onCreate();
	}

	private void onCreate() {
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			this.c = DriverManager.getConnection("jdbc:sqlite:shoppinglist.db");
			
			stmt = this.c.createStatement();
			if(ShoppingListServer.WIPE_DATABSE_ON_STARTUP) {
				stmt.executeUpdate(DROP_TABLE_USERS);
				stmt.executeUpdate(DROP_TABLE_SHAREDLISTS);
				stmt.executeUpdate(DROP_TABLE_LOCALTOSERVER_LIST_ID);
			}
			stmt.executeUpdate(CREATE_TABLE_USERS);
			stmt.executeUpdate(CREATE_TABLE_LOCALTOSERVER_LIST_ID);
			if(ShoppingListServer.WIPE_DATABSE_ON_STARTUP) {
				stmt.executeUpdate(INSERT_DUMMY);
			}
			stmt.executeUpdate(CREATE_TABLE_SHAREDLISTS);
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened SQLite database successfully");
	}

	/**
	 * Add a user to the database if he isn't already in
	 * @param phoneNumber
	 */
	public void addUser(Request request) {
		String phoneNumber = request.getPhoneNumber();
		Statement stmt;
		try {
			stmt = this.c.createStatement();
			String selectUserifExists = "select * from " + TABLE_USERS + " where " + COLUMN_USER_PHONENUMBER + "=\"" + phoneNumber + "\";";
			ResultSet rs = stmt.executeQuery(selectUserifExists);
			if(!rs.next()) {
				String insertUser = "insert into " + TABLE_USERS + " (" + COLUMN_USER_PHONENUMBER + ") values (\"" + phoneNumber + "\");";			
				stmt.executeUpdate(insertUser);
				rs = stmt.executeQuery(selectUserifExists);
				System.out.println("\t:Added user: " + rs.getString(COLUMN_USER_PHONENUMBER));
				request.setSuccessful();
			} else {
				System.out.println("\t:User " + rs.getString(COLUMN_USER_PHONENUMBER) + " already existed");
			}
			request.setHandled();
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * returns UserId if it exists in database
	 * @param request
	 * @return
	 */
	public int findUser(Request request) {
		String phoneNumber = request.getPhoneNumber();
		Statement stmt;
		try {
			stmt = this.c.createStatement();
			String selectUserifExists = "select * from " + TABLE_USERS + " where " + COLUMN_USER_PHONENUMBER + "=\"" + phoneNumber + "\";";
			ResultSet rs = stmt.executeQuery(selectUserifExists);
			if(rs.next()) {
				System.out.println("\t:User " + rs.getString(COLUMN_USER_PHONENUMBER) + " does exist in Database");
				request.setHandled();
				return rs.getInt(COLUMN_USER_ID);
			} else {
				System.out.println("\t:User " + phoneNumber + " does not exist in Database");
				request.setHandled();
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		return -1;
	}

	/**
	 * Creates an entry in sharedlists
	 * @param request
	 */
	public void shareList(ShareListRequest request) {
		int userId = findUser(request);
		int friendId = findUser(new RegisterRequest(request.getFriendNumber()));
		String listname = request.getListName();
		
		if(userId == -1 || friendId == -1)
			return;
		long serverListId = createServerListIdifNotExists(userId, request.getListId());
		if(serverListId == -1)
			System.err.println("Failed to find/create global list id");
		
		Statement stmt;
		try {
			stmt = this.c.createStatement();
			String selectEntryifExists = "select * from " + TABLE_SHAREDLISTS + " where " + COLUMN_USER_ID + "=\"" + userId + "\" and " + 
					COLUMN_FRIEND_ID + "=\"" + friendId + "\" and " + COLUMN_SERVER_LIST_ID + "=\"" + serverListId + "\";";
			ResultSet rs = stmt.executeQuery(selectEntryifExists);
			if(rs.next()) {
				System.out.println("\t:List " + serverListId + " is already shared with user " + friendId);
				request.setHandled();
			} else {
				String insertSharedList = "insert into " + TABLE_SHAREDLISTS + " values (" +
						"\"" + userId + "\", " +
						"\"" + friendId + "\", " +
						"\"" + serverListId + "\"," +
						"\"" + listname + "\"" +
						");";	
				stmt.executeUpdate(insertSharedList);
				System.out.println("\t:List " + serverListId + " is now shared with users " + friendId + " and " + userId);
				request.setSuccessful();
			}

		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		
	}
	
	/**
	 * Removes an entry in sharedlists
	 * @param request
	 */
	public void unShareList(UnShareListRequest request) {
		int userId = findUser(request);
		int friendId = findUser(new RegisterRequest(request.getFriendNumber()));
		long serverListId = getServerListId(userId, request.getListId());
		
		if(userId <= -1 || friendId <= -1)
			return;
		if(serverListId <= -1)
			System.err.println("Failed to find/create global list id");
		
		Statement stmt;
		try {
			stmt = this.c.createStatement();
			String selectEntryifExists = "select * from " + TABLE_SHAREDLISTS + " where " + COLUMN_USER_ID + "=\"" + userId + "\" and " + 
					COLUMN_FRIEND_ID + "=\"" + friendId + "\" and " + COLUMN_SERVER_LIST_ID + "=\"" + serverListId + "\";";
			ResultSet rs = stmt.executeQuery(selectEntryifExists);
			if(rs.next()) {
				String deleteFriendfromList = "delete from " + TABLE_SHAREDLISTS + " where " +
						COLUMN_USER_ID + "=" + userId + " and " +
						COLUMN_FRIEND_ID + "=" + friendId + " and " +
						COLUMN_SERVER_LIST_ID + "=" + serverListId + ";";
				stmt.executeUpdate(deleteFriendfromList);
				System.out.println("\t:List " + serverListId + " is no longer shared with " + friendId);
				request.setSuccessful();
			} else {
				System.out.println("\t:List " + serverListId + " is not shared with user " + friendId);
				request.setHandled();
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		
	}

	/**
	 * Converts an userID and local ListId to the global list id (which is used by the server)
	 * @param userId
	 * @param listId
	 * @return serverListId
	 */
	private long createServerListIdifNotExists(int userId, long listId) {
		long serverListId = getServerListId(userId, listId);
		if (serverListId > -1) {
			return serverListId;
		} else {
			try {
				Statement stmt = this.c.createStatement();
				String createEntry = "insert into "
						+ TABLE_LOCALTOSERVER_LIST_ID + " values (\"" + userId
						+ "\",\"" + listId + "\", (select max("
						+ COLUMN_SERVER_LIST_ID + ")+1 from "
						+ TABLE_LOCALTOSERVER_LIST_ID + ")" + ");";
				stmt.executeUpdate(createEntry);
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}
		serverListId = getServerListId(userId, listId);
		if(serverListId <=-1) {
			throw new IllegalStateException();
		}
		return getServerListId(userId, listId);
	}
	
	/**
	 * Assign a Local list Id to a global server list id (as a response to a CreateShareListRequest)
	 * @param request
	 */
	public void assignLocalToServerListId(CreateSharedListRequest request) {
		int userId = findUser(request);
		assignLocalToServerListId(userId, request.getLocalListId(), request.getServerListid());
	}
	
	/**
	 * Assign a Local list Id to a global server list id (as a response to a CreateShareListRequest)
	 * @param request
	 */
	public void assignLocalToServerListId(int userId, long localListId, long serverListId) {
		// Check if there's already an entry for this list (it shouldn't)
		long maybeServerListId = getServerListId(userId, localListId);
		if(maybeServerListId >-1) {
			return;
		}
		try {
			Statement stmt = this.c.createStatement();
			String createEntry = "insert into "
					+ TABLE_LOCALTOSERVER_LIST_ID + " values (\"" + userId
					+ "\",\"" + localListId + "\",\"" + serverListId + ");";
			stmt.executeUpdate(createEntry);
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Gets the serverlistId that is identified by the userId and his local listId
	 * @param userId
	 * @param listId
	 * @return serverListId
	 */
	private long getServerListId(int userId, long listId) {
		long serverId = -1;
		Statement stmt;
		try {
			stmt = this.c.createStatement();
			String selectEntryifExists = "select * from " + TABLE_LOCALTOSERVER_LIST_ID + " where " + COLUMN_USER_ID + "=\"" + userId + "\" and " +
					COLUMN_LOCAL_LIST_ID + "=\"" + listId + "\";";
			ResultSet rs = stmt.executeQuery(selectEntryifExists);
			if(rs.next()) {
				serverId = rs.getInt(COLUMN_SERVER_LIST_ID);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		return serverId;
	}

}
