package ch.unibe.ese.core;

import java.util.Collections;
import java.util.List;

/**
 * The ListManager is responsible for access to all {@link ShoppingList
 * shoppinglists}.
 */
public class ListManager {

	private List<ShoppingList> shoppingLists;
	private PersistenceManager persistenceManager;

	public ListManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		shoppingLists = persistenceManager.read();
	}

	/**
	 * @return unmodifiable list
	 */
	public List<ShoppingList> getShoppingLists() {
		return Collections.unmodifiableList(shoppingLists);
	}

	/**
	 * @param list
	 *            not <code>null</code>
	 */
	public void addShoppingList(ShoppingList list) {
		if (list == null)
			throw new IllegalArgumentException("null is not allowed");
		if (!shoppingLists.contains(list))
			shoppingLists.add(list);
	}

	public void removeShoppingList(ShoppingList list) {
		shoppingLists.remove(list);
	}

	/**
	 * Persists the ShoppingLists.
	 */
	public void persist() {
		persistenceManager.save(this.shoppingLists);
	}
}
