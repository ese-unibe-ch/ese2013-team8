package ch.unibe.ese.core;

import java.util.ArrayList;

public class FriendsManager {
	private ArrayList<Friend> friendsList;
	private PersistenceManager persistenceManager;
	
	public FriendsManager(PersistenceManager manager){
		friendsList = new ArrayList<Friend>();
	
		this.persistenceManager = manager;
		try {
			//TODO: Load friends when creating manager from SQL database
			friendsList = (ArrayList<Friend>) persistenceManager.readFriends();
		} catch (Exception e) {
			// TODO throw an appropriate exception
			//throw new IllegalStateException(e);
		}	
	}
	
	/**
	 * Checks if friend is on the server and adds him to the friendlist when successful
	 * @param phoneNr
	 * @param name
	 * @int 0 successful, >= 1 fail
	 */
	public int addFriend(int phoneNr, String name){
		if(checkIfDouble(phoneNr, name))
			return 1;
		
		//TODO: Check if friend has app on server, if no, return 2, else:
		friendsList.add(new Friend(phoneNr, name));
		
		//TODO: Save friend to database
		return 0;
	}
	
	/**
	 * Checks if a friend is already in the friendslist
	 * @return true if already in list, otherwise false
	 */
	private boolean checkIfDouble(int phoneNr, String name) {
		Friend friend = new Friend(phoneNr, name);
		for(Friend compare: friendsList)
			if(compare.equals(friend)) return true;
		
		return false;
	}

	/**
	 * @return List of all added friends
	 */
	public ArrayList<Friend> getFriendsList(){
		return new ArrayList<Friend>(friendsList);
	}
	
	/**
	 * Removes friend only on the phone, no access to server is needed.
	 * @param friend to remove
	 * @return if successful
	 */
	public boolean removeFriend(Friend friend){
		return friendsList.remove(friend);
		//TODO: remove friend from database
	}
	
	/**
	 * Returns the friend to which the number belongs, if no friend found, returns null
	 * @param PhoneNr
	 * @return
	 */
	public Friend getFriendFromNr(int nr){
		for(Friend friend: friendsList){
			if(friend.getPhoneNr() == nr) return friend;
		}
		return null;
	}
	
	public String toString(){
		String result = "";
		for(Friend friend : friendsList)
			result += friend.toString() + "\n";
		return result;
	}	
}
