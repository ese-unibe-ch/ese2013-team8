package ch.unibe.ese.core;

import java.util.ArrayList;

public class FriendsManager {
	private ArrayList<Friend> friendsList;
	private PersistenceManager persistenceManager;
	
	public FriendsManager(){
		friendsList = new ArrayList<Friend>();
		/*
		this.persistenceManager = persistenceManager;
		try {
			//TODO: Load friends when creating manager from SQL database
			//friendsList = persistenceManager.readFriends();
		} catch (IOException e) {
			// TODO throw an appropriate exception
			throw new IllegalStateException(e);
		}*/	
	}
	
	public boolean addFriend(int phoneNr, String name){
		//TODO: Check if friend has app on server, if no, return false, else:
		
		return friendsList.add(new Friend(phoneNr, name));
		//TODO: Save friend to database
	}
	
	public ArrayList<Friend> getFriendsList(){
		return new ArrayList<Friend>(friendsList);
	}
	
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
