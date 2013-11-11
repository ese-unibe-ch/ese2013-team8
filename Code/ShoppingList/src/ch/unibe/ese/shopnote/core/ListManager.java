package ch.unibe.ese.shopnote.core;

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

	private final List<ShoppingList> shoppingLists;
	private final PersistenceManager persistenceManager;
	private final Map<ShoppingList, List<Item>> listToItems;
	private List<Recipe> recipeList;

	public ListManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		this.listToItems = new HashMap<ShoppingList, List<Item>>();
		this.shoppingLists = persistenceManager.getLists();
		this.recipeList = persistenceManager.readRecipes();
	}

	/**
	 * @return unmodifiable list
	 */
	public List<ShoppingList> getShoppingLists() {
		return Collections.unmodifiableList(shoppingLists);
	}

	public ShoppingList getShoppingList(long id) {
		for (ShoppingList list : shoppingLists) {
			if (list.getId() == id)
				return list;
		}
		return null;
	}

	/**
	 * @param list
	 *            not <code>null</code>
	 */
	public void saveShoppingList(ShoppingList list) {
		if (list == null)
			throw new IllegalArgumentException("null is not allowed");
		if (!shoppingLists.contains(list)) {
			shoppingLists.add(list);
		}
		persistenceManager.save(list);
	}

	public void removeShoppingList(ShoppingList list) {
		shoppingLists.remove(list);
		persistenceManager.remove(list);
	}

	/**
	 * Fuegt das Item der Shoppinglist hinzu.
	 * 
	 * @param item
	 *            nicht null
	 * @param list
	 *            nicht null
	 * @return true falls das Item hinzugefuegt wurde, false sonst (wenn das Item
	 *         bereits in der Liste ist).
	 */
	public boolean addItemToList(Item item, ShoppingList list) {
		if (item == null || list == null)
			throw new IllegalArgumentException("null is not allowed");
		List<Item> items = listToItems.get(list);
		if (items == null) {
			items = new ArrayList<Item>();
			listToItems.put(list, items);
		}
		persistenceManager.save(item, list);
		if (!items.contains(item))
			return items.add(item);
		return false;
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
		persistenceManager.remove(item, list);

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
			listToItems.put(list, items);
		}
		return Collections.unmodifiableList(items);
	}

	/**
	 * Gets all items-objects which are in the item table
	 * 
	 * @return ArrayList<Item>
	 */
	public ArrayList<Item> getAllItems() {
		return persistenceManager.getAllItems();
	}

	/**
	 * Adds an item to the item list or updates it if it is already in the list
	 * 
	 * @param item
	 */
	public void save(Item item) {
		persistenceManager.save(item);
	}
	
	public Item getItem(Long id) {
		ArrayList<Item> listOfItems = persistenceManager.getAllItems();
		for(Item item: listOfItems) {
			if(item.getId() == id) return item;
		}
		return null;
	}

	/**
	 * Removes an specific item from the db
	 * @param item
	 */
	public void remove(Item item) {
		persistenceManager.remove(item);

		for (ShoppingList list : shoppingLists)
			removeItemFromList(item, list);
	}

	/**
	 * Returns a list of all recipes which are saved in the database
	 * 
	 * @return list of recipes
	 */
	public List<Recipe> getRecipes() {
		return recipeList;
	}

	/**
	 * Saves all Recipes to the database
	 * 
	 * @param recipe
	 */
	public void saveRecipe(Recipe recipe) {
		persistenceManager.save(recipe);
		recipeList = persistenceManager.readRecipes();
	}

	/**
	 * Removes a Recipe from the database
	 * 
	 * @param recipe
	 */
	public void removeRecipe(Recipe recipe) {
		recipeList.remove(recipe);
		persistenceManager.remove(recipe);
	}

	/**
	 * Gets the Recipe at the correct position
	 * 
	 * @param position
	 * @return recipe at position x
	 */
	public Recipe getRecipeAt(Long id) {
		for(Recipe recipe: recipeList)
			if(recipe.getId() == id) return recipe;
		return null;
	}

	public Recipe getRecipeAt(int position) {
		return recipeList.get(position);
	}
	
	public Recipe getRecipe(long id) {
		for (Recipe recipe : recipeList) {
			if (recipe.getId() == id)
				return recipe;
		}
		return null;
	}
}
