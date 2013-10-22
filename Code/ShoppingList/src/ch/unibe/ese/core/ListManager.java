package ch.unibe.ese.core;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

// TODO: prevent duplicated list names

/**
 * The ListManager is responsible for access to all {@link ShoppingList
 * shoppinglists}.
 */
public class ListManager {

	private List<ShoppingList> shoppingLists;
	private PersistenceManager persistenceManager;

	public ListManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		try {
			shoppingLists = persistenceManager.read();
		} catch (IOException e) {
			// TODO throw an appropriate exception
			throw new IllegalStateException(e);
		}
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
		if (!shoppingLists.contains(list)) {
			shoppingLists.add(list);
		}
		try {
			persistenceManager.save(list);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	public void removeShoppingList(ShoppingList list) {
		shoppingLists.remove(list);
		try {
			persistenceManager.remove(list);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
