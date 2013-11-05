package ch.unibe.ese.core;

import java.io.IOException;
import java.util.ArrayList;
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
	 * Reads ShoppingLists from their persistent state.
	 * 
	 * @return never <code>null</code>.
	 * @throws IOException
	 */
	List<ShoppingList> readLists();
	
	/**
	 * Saves a new created or editet list
	 * @param list
	 * @throws IOException
	 */
	void save(ShoppingList list);
	
	/**
	 * Removes an already existing list
	 * @param list
	 * @throws IOException
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
	 * @throws IOException
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
	 * @throws IOException
	 */
	ArrayList<Friend> readFriends();
	
	/**
	 * Adds a new friend to the db
	 * @param Friend
	 */
	void save(Friend friend);

	/**
	 * Removes an already existing friend from the db
	 * @param friend
	 */
	void removeFriend(Friend friend);

	/**
	 * Get all items which are saved in the db
	 * @return List<Item> of all Items in db
	 */
	ArrayList<Item> getAllItems();
	
	/**
	 * Saves item to the db where all items are saved.
	 * @param item
	 */
	public void save(Item item);
	
	/**
	 * Removes item from item db
	 * @param item
	 */
	public void remove(Item item);

	/**
	 * Reads all recipes on the db
	 * @return
	 */
	List<Recipe> readRecipes();
	
	/**
	 * Saves a recipe to the db
	 * @param recipe
	 */
	public void save(Recipe recipe);

	/**
	 * removes a recipe from the db
	 * @param recipe
	 */
	void remove(Recipe recipe);
}
