package ch.unibe.ese.core;

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
	 */
	List<ShoppingList> read();

	/**
	 * Saves ShoppingLists int their persistent state.
	 * 
	 * @param lists
	 *            not <code>null</code>.
	 */
	void save(List<ShoppingList> lists);
}
