package ch.unibe.ese.shopnote.core;

import java.util.List;

/**
 * The PersistenceManager is responsible to read and save ShoppingLists.
 * <p>
 * It's mandatory that the Lists are the same after saving and reading, thus the
 * following code must be true: <br />
 * <code>
 * PersistenceManager pm = ...<br />
 * pm.save(lists);<br />
 * List<..> readLists = pm.read();<br />
 * lists.equals(readLists);<br />
 * </code>
 */
public interface PersistenceManager {
	
	/**
	 * Searches for the item with the given name.
	 * @param itemName
	 * @return null if the item does not exist.
	 */
	Item getItem(String itemName);

	/**
	 * Reads ShoppingLists from their persistent state.
	 * 
	 * @return never <code>null</code>.
	 */
	List<ShoppingList> getLists();
	
	/**
	 * Saves a new created or editet list
	 * @param list
	 */
	long save(ShoppingList list);
	
	/**
	 * Removes an already existing list
	 * @param list
	 */
	void remove(ShoppingList list);

	/**
	 * Adds a new Item to a shopping list
	 * @param item
	 * @param list
	 */
	void save(Item item, ShoppingList list);

	/**
	 * Removes an already existing item from a list
	 * @param item
	 * @param list
	 */
	void remove(Item item, ShoppingList list);

	/**
	 * Returns all Items in list
	 * @param list
	 * @return
	 */
	List<Item> getItems(ShoppingList list);

	/**
	 * Reads friends from their persistent state.
	 * 
	 * @return never <code>null</code>.
	 */
	List<Friend> getFriends();
	
	/**
	 * Adds a new friend to the db
	 * @param Friend
	 */
	long save(Friend friend);

	/**
	 * Removes an already existing friend from the db
	 * @param friend
	 */
	void removeFriend(Friend friend);

	/**
	 * Get all items which are saved in the db
	 * @return List<Item> of all Items in db
	 */
	List<Item> getAllItems();
	
	/**
	 * Saves item to the db where all items are saved.
	 * @param item
	 */
	void save(Item item);
	
	/**
	 * Removes item from item db
	 * @param item
	 */
	void remove(Item item);

	/**
	 * Reads all recipes on the db
	 * @return
	 */
	List<Recipe> readRecipes();
	
	/**
	 * Saves a recipe to the db
	 * @param recipe
	 */
	void save(Recipe recipe);

	/**
	 * removes a recipe from the db
	 * @param recipe
	 */
	void remove(Recipe recipe);

	/**
	 * saves a friend to a synchronized shopping list in the db
	 * @param friend
	 * @param list
	 */
	void save(ShoppingList list, Friend friend);
	
	/**
	 * saves a friend to a synchronized recipe in the db
	 * @param friend
	 * @param list
	 */
	void save(Recipe recipe, Friend friend);

	/**
	 * removes a friend from a synchronized shopping list in the db
	 * @param list
	 * @param friend
	 */
	void remove(ShoppingList list, Friend friend);
	
	/**
	 * removes a friend from a synchronized recipe in the db
	 * @param list
	 * @param friend
	 */
	void remove(Recipe recipe, Friend friend);
	
	/**
	 * Get all friends with which you shared a list
	 * @param list
	 * @return ArrayList of friends for a specific shared list
	 */
	List<Friend> getSharedFriends(ShoppingList list);
	
	/**
	 * Get all friends with which you shared a recipe
	 * @param list
	 * @return ArrayList of friends for a specific recipe
	 */
	List<Friend> getSharedFriends(Recipe recipe);
}
