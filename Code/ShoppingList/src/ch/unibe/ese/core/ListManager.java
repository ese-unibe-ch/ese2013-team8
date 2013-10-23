package ch.unibe.ese.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: prevent duplicated list names

/**
 * The ListManager is responsible for access to all {@link ShoppingList
 * shoppinglists}.
 */
public class ListManager {

	private List<ShoppingList> shoppingLists;
	private PersistenceManager persistenceManager;
	private Map<ShoppingList, List<Item>> listToItems;

	ListManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		this.listToItems = new HashMap<ShoppingList, List<Item>>();
		try {
			shoppingLists = persistenceManager.readLists();
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

	public void addItemToList(Item item, ShoppingList list) {
		if (item == null || list == null)
			throw new IllegalArgumentException("null is not allowed");
		List<Item> items = listToItems.get(list);
		if (items == null) {
			items = new ArrayList<Item>();
			listToItems.put(list, items);
		}
		items.add(item);
		persistenceManager.save(item, list);

	}

	public void removeItemFromList(Item item, ShoppingList list) {
		if (item == null || list == null)
			throw new IllegalArgumentException("null is not allowed");
		List<Item> items = listToItems.get(list);
		if (items == null) {
			items = new ArrayList<Item>();
			listToItems.put(list, items);
		}
		items.remove(item);
		try {
			persistenceManager.remove(item,list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}

	/**
	 * Gets all Items from this shopping list.
	 * 
	 * @param list
	 *            not null.
	 * @return not null, unmodifiable.
	 */
	public List<Item> getItemsFor(ShoppingList list) {
		List<Item> items = listToItems.get(list);
		if (items == null) {
			items = persistenceManager.getItems(list);
		}
		// TODO: add separate list for bought items
		return items; //Collections.unmodifiableList(items);
	}
}
