package ch.unibe.ese.shopnote.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendsManager {
	private ArrayList<Friend> friendsList;
	private PersistenceManager persistenceManager;

	public FriendsManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		friendsList = persistenceManager.getFriends();
	}

	/**
	 * Checks if friend is on the server and adds him to the friendlist when
	 * successful
	 * 
	 * @param phoneNr
	 * @param name
	 * @int 0 successful, >= 1 fail
	 */
	public int addFriend(Friend friend) {
		// TODO this does not really work as we compare the friends id, which is null.
		// So its possible to add a friend with same name and number multiple times.
		// Fix this when adding select friends from phone book.
		if (friendsList.contains(friend))
			return 1;

		// Add the friend
		friendsList.add(friend);

		// Save friend to database
		persistenceManager.save(friend);
		return 0;
	}

	/**
	 * @return List of all added friends
	 */
	public List<Friend> getFriendsList() {
		return Collections.unmodifiableList(friendsList);
	}

	/**
	 * Permits to update the name of a friend
	 * 
	 * @param updated
	 *            friend
	 */
	public void update(Friend friend) {
		persistenceManager.save(friend);
	}

	/**
	 * Removes friend only on the phone, no access to server is needed.
	 * 
	 * @param friend
	 *            to remove
	 * @return if successful
	 */
	public boolean removeFriend(Friend friend) {
		persistenceManager.removeFriend(friend);
		return friendsList.remove(friend);
		// TODO: remove friend from database
	}

	/**
	 * Returns the friend to which the number belongs, if no friend found,
	 * returns null
	 * 
	 * @param PhoneNr
	 * @return
	 */
	public Friend getFriend(long id) {
		for (Friend friend : friendsList) {
			if (friend.getId() == id)
				return friend;
		}
		return null;
	}
	
	/**
	 * Adds a friend to a synchronized shopping list
	 * @param friend
	 * @param list
	 */
	public void addFriendToList(ShoppingList list, Friend friend) {
		//TODO: send the information to the server and add the friend to the list on the server
		persistenceManager.save(list, friend);
	}
	
	public void deleteFriendToList(ShoppingList list, Friend friend) {
		//TODO: send the information to the server and remove the friend on the server
		//(just the entry of the shared Shoppinglist, not the friend as a onject of course^^)
		persistenceManager.remove(list, friend);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Friend friend : friendsList)
			result.append(friend).append("\n");
		return result.toString();
	}
}
