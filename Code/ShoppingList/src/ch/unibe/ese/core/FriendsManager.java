package ch.unibe.ese.core;

import java.util.ArrayList;

public class FriendsManager {
	private ArrayList<Friend> friendsList;
	private PersistenceManager persistenceManager;
	
	public FriendsManager(PersistenceManager persistenceManager){
		this.persistenceManager = persistenceManager;
		friendsList = persistenceManager.readFriends();
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
		Friend friend = new Friend(phoneNr, name);
		friendsList.add(friend);
		
		//Save friend to database
		persistenceManager.save(friend);
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
	
	public String toString(){
		String result = "";
		for(Friend friend : friendsList)
			result += friend.toString() + "\n";
		return result;
	}	
}
