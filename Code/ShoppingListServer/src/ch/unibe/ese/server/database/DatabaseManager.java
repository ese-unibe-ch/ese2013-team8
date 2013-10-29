package ch.unibe.ese.server.database;

import java.sql.*;

import ch.unibe.ese.share.FriendRequest;
import ch.unibe.ese.share.RegisterRequest;
import ch.unibe.ese.share.Request;
import ch.unibe.ese.share.ShareRequest;

/**
 * This Class organizes the database on the server. Can maybe be split up to some smaller classes
 * @author Stephan
 *
 */

public class DatabaseManager {
	// Name definitions
	public static final String TABLE_USERS = "users";
	public static final String COLUMN_USERS_ID = "userId";
	public static final String COLUMN_USERS_PHONENUMBER = "phoneNumber";
	public static final String TABLE_SHAREDWITH = "sharedwith";
	public static final String COLUMN_FRIEND_ID = "friendId";
	// Create statement
	public static final String CREATE_TABLE_USERS = "create table if not exists " + TABLE_USERS + "(" + 
			COLUMN_USERS_ID + " integer primary key autoincrement, " +
			COLUMN_USERS_PHONENUMBER + " varchar(20)" +
			");";
	public static final String CREATE_TABLE_SHAREDWITH = "create table if not exists " + TABLE_SHAREDWITH + "(" +
			COLUMN_USERS_ID + " integer not null references " + TABLE_USERS + ", " +
			COLUMN_FRIEND_ID + " integer not null references " + TABLE_USERS + "." + COLUMN_USERS_ID + 
			");";
	
	// instance variables
	Connection c;
	
	public DatabaseManager() {
		this.onCreate();
	}

	private void onCreate() {
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			this.c = DriverManager.getConnection("jdbc:sqlite:shoppinglist.db");

			stmt = this.c.createStatement();
			stmt.executeUpdate(CREATE_TABLE_USERS);
			stmt.executeUpdate(CREATE_TABLE_SHAREDWITH);
			stmt.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
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
			String selectUserifExists = "select * from " + TABLE_USERS + " where " + COLUMN_USERS_PHONENUMBER + "=\"" + phoneNumber + "\";";
			ResultSet rs = stmt.executeQuery(selectUserifExists);
			if(!rs.next()) {
				String insertUser = "insert into " + TABLE_USERS + " (" + COLUMN_USERS_PHONENUMBER + ") values (\"" + phoneNumber + "\");";			
				stmt.executeUpdate(insertUser);
				rs = stmt.executeQuery(selectUserifExists);
				System.out.println("Added user: " + rs.getString(COLUMN_USERS_PHONENUMBER));
				request.setSuccessful();
			} else {
				System.out.println("User " + rs.getString(COLUMN_USERS_PHONENUMBER) + " already existed");
			}
			request.setHandled();
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Sets successful, if the requested friend is in database
	 * @param request
	 */
	public void searchUser(FriendRequest request) {
		String friendNumber = request.getFriendNumber();
		Statement stmt;
		try {
			stmt = this.c.createStatement();
			String selectUserifExists = "select * from " + TABLE_USERS + " where " + COLUMN_USERS_PHONENUMBER + "=\"" + friendNumber + "\";";
			ResultSet rs = stmt.executeQuery(selectUserifExists);
			if(rs.next()) {
				System.out.println("User " + rs.getString(COLUMN_USERS_PHONENUMBER) + " is in Database");
				request.setSuccessful();
			} else {
				System.out.println("User is not in databse: " + rs.getString(COLUMN_USERS_PHONENUMBER));
			}
			request.setHandled();
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Stores a Shopping list
	 */
	public void shareShoppingList(ShareRequest request) {
		//TODO
		
	}
	
}
