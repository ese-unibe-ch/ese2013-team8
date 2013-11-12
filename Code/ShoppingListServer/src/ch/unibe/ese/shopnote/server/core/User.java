package ch.unibe.ese.shopnote.server.core;

/**
 * Information container for user data
 *
 */
public class User {

	private int userId;
	private long localListId;
	
	public User(int userId) {
		this.userId = userId;
	}
	
	public void setLocalListId(long id) {
		this.localListId = id;
	}
	
	public long getLocalListid() {
		return this.localListId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
