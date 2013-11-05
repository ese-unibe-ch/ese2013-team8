package ch.unibe.ese.core;

import java.util.ArrayList;

public class FriendsManager {
	private ArrayList<Friend> friendsList;
	private PersistenceManager persistenceManager;
	
	public FriendsManager(PersistenceManager persistenceManager){
		this.persistenceManager = persistenceManager;
		friendsList = persistenceManager.getFriends();
	}
	
	/**
	 * Checks if friend is on the server and adds him to the friendlist when successful
	 * @param phoneNr
	 * @param name
	 * @int 0 successful, >= 1 fail
	 */
	public int addFriend(Friend friend){
		if(checkIfDouble(friend))
			return 1;
		
		// TODO
		// You cannot wait for a network request to finish
		// It needs to update the status of the friend (setChanged) when the
		// result of the request is here.
		//FriendRequest fRequest = new FriendRequest("" + friend.getPhoneNr());
		//SyncManager.getInstance().addRequest(fRequest);
		//SyncManager.getInstance().synchronise();
		// \TODO
		friendsList.add(friend);
		
		//Save friend to database
		persistenceManager.save(friend);
		return 0;
	}
	
	/**
	 * Checks if a friend is already in the friendslist
	 * @return true if already in list, otherwise false
	 */
	private boolean checkIfDouble(Friend friend) {
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
	 * Permits to update the name of a friend
	 * @param updated friend
	 */
	public void update(Friend friend){
		persistenceManager.save(friend);
		
		//update friendslist
		int index = getIndex(friend);
		updateFriendList(index, friend);
	}
	
	/**
	 * Get the index of the friend
	 * @param friend
	 * @return Index of friend, when no friend -1
	 */
	public int getIndex(Friend friend){
		int index = -1;
		for(Friend compare: friendsList){
			if(friend.getPhoneNr() == compare.getPhoneNr())
				index = friendsList.indexOf(compare);
		}
		
		return index;	
	}
	
	/**
	 * updates the friendList
	 * @param index
	 * @param friend
	 */
	public void updateFriendList(int index, Friend friend){
		friendsList.remove(index);
		friendsList.add(index, friend);
	}
	
	/**
	 * Removes friend only on the phone, no access to server is needed.
	 * @param friend to remove
	 * @return if successful
	 */
	public boolean removeFriend(Friend friend){
		persistenceManager.removeFriend(friend);
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
