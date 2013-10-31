package ch.unibe.ese.server.database;

import java.sql.*;

import ch.unibe.ese.server.core.ShoppingListServer;
import ch.unibe.ese.share.requests.Request;

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
	// Create statement
	public static final String CREATE_TABLE_USERS = "create table if not exists " + TABLE_USERS + "(" + 
			COLUMN_USERS_ID + " integer primary key autoincrement, " +
			COLUMN_USERS_PHONENUMBER + " varchar(20)" +
			");";
	private static final String DROP_TABLES = "drop table " + TABLE_USERS + ";";
	
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
			if(ShoppingListServer.WIPE_DATABSE_ON_STARTUP) {
				stmt.executeUpdate(DROP_TABLES);
			}
			stmt.executeUpdate(CREATE_TABLE_USERS);
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

	public void findUser(Request request) {
		String phoneNumber = request.getPhoneNumber();
		Statement stmt;
		try {
			stmt = this.c.createStatement();
			String selectUserifExists = "select * from " + TABLE_USERS + " where " + COLUMN_USERS_PHONENUMBER + "=\"" + phoneNumber + "\";";
			ResultSet rs = stmt.executeQuery(selectUserifExists);
			if(rs.next()) {
				System.out.println("User " + rs.getString(COLUMN_USERS_PHONENUMBER) + " does exist in Database");
				request.setSuccessful();
			} else {
				System.out.println("User " + phoneNumber + " does not exist in Database");
			}
			request.setHandled();
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

}
