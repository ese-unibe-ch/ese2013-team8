package ch.unibe.ese.shopnote.core;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import ch.unibe.ese.shopnote.core.entities.Item;
import ch.unibe.ese.shopnote.core.entities.ShoppingListItem;
import ch.unibe.ese.shopnote.core.entities.Recipe;
import ch.unibe.ese.shopnote.core.entities.ShoppingList;

/**
 * The ListManager is responsible for access to all {@link ShoppingList
 * shoppinglists}.
 */
public class ListManager {

	private static final BigDecimal THOUSAND = new BigDecimal("1000");
	private final List<ShoppingList> shoppingLists;
	private final PersistenceManager persistenceManager;
	private List<Recipe> recipeList;

	public ListManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		this.shoppingLists = persistenceManager.getLists();
		this.recipeList = persistenceManager.readRecipes();
	}

	/**
	 * @return unmodifiable list
	 */
	public List<ShoppingList> getShoppingLists() {
		Collections.sort(shoppingLists, Comparators.LIST_COMPARATOR);
		return Collections.unmodifiableList(shoppingLists);
	}

	/**
	 * Get a specific shopping list with unique id
	 * 
	 * @param id
	 * @return Shopping list with id
	 */
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
	public long saveShoppingList(ShoppingList list) {
		if (list == null)
			throw new IllegalArgumentException("null is not allowed");
		if (!shoppingLists.contains(list)) {
			shoppingLists.add(list);
		}
		long id = persistenceManager.save(list);
		return id;
	}

	public void removeShoppingList(ShoppingList list) {
		shoppingLists.remove(list);
		persistenceManager.remove(list);
	}

	/**
	 * Adds the item to the shoppinglist
	 * 
	 * @param item
	 *            not null
	 * @param list
	 *            not null
	 * @return true if the item was added (false if it only was updated or not
	 *         changed at all)
	 * @throws ItemException thrown if another item with the same name, but different unit is in the list.
	 */
	public boolean addItemToList(ShoppingListItem item, ShoppingList list) throws ItemException {
		if (item == null || list == null)
			throw new IllegalArgumentException("null is not allowed");
		
		List<ShoppingListItem> items = persistenceManager.getItems(list);
		item = mergeItem(item, items);
		
		persistenceManager.save(item, list);
		return !items.contains(item);
	}
	
	/**
	 * Updates the item in the shopping list.<p>
	 * If the item is not yet in the list, nothing is done.
	 * 
	 * @param item not null
	 * @param list not null
	 */
	public void updateItemInList(ShoppingListItem item, ShoppingList list) {
		if (item == null || item.getId() == null || list == null)
			throw new IllegalArgumentException("null is not allowed");

		List<ShoppingListItem> items = persistenceManager.getItems(list);
		if (!items.contains(item))
			return;
		
		persistenceManager.save(item, list);
	}
	
	/**
	 * Removes an item from this shopping list
	 * 
	 * @param item
	 * @param list
	 */
	public void removeItemFromList(Item item, ShoppingList list) {
		if (item == null || list == null)
			throw new IllegalArgumentException("null is not allowed");
		persistenceManager.remove(item, list);
	}

	/**
	 * Gets all Items from this shopping list.
	 * 
	 * @param list
	 *            not null.
	 * @return not null, unmodifiable.
	 */
	public List<ShoppingListItem> getItemsFor(ShoppingList list) {
		List<ShoppingListItem>	items = persistenceManager.getItems(list);
		Collections.sort(items, Comparators.ITEM_COMPARATOR);
		return Collections.unmodifiableList(items);
	}

	/**
	 * Gets all items-objects which are in the item table
	 * 
	 * @return ArrayList<Item>
	 */
	public List<ShoppingListItem> getAllItems() {
		List<ShoppingListItem> items = persistenceManager.getAllItems();
		Collections.sort(items, Comparators.ITEM_COMPARATOR);
		return items;
	}

	/**
	 * Adds an item to the item list or updates it if it is already in the list
	 * 
	 * @param item
	 */
	public void save(Item item) {
		persistenceManager.save(item);
	}

	/**
	 * Get a specific item with unique id
	 * 
	 * @param id
	 * @return item with id
	 */
	public ShoppingListItem getItem(Long id) {
		if (id == null)
			return null;
		return persistenceManager.getItem(id);
	}
	
	/**
	 * Gets the item with the given name.
	 * @param name
	 * @return null if no item with the name exists.
	 */
	public ShoppingListItem getItem(String name) {
		return persistenceManager.getItem(name);
	}

	/**
	 * Removes an specific item from the db
	 * 
	 * @param item
	 */
	public void remove(Item item) {
		for (ShoppingList list : shoppingLists)
			removeItemFromList(item, list);
		persistenceManager.remove(item);

	}

	/**
	 * Returns a list of all recipes which are saved in the database
	 * 
	 * @return list of recipes
	 */
	public List<Recipe> getRecipes() {
		Collections.sort(recipeList, Comparators.RECIPE_COMPARATOR);
		return Collections.unmodifiableList(recipeList);
	}

	/**
	 * Saves all Recipes to the database
	 * 
	 * @param recipe
	 */
	public void saveRecipe(Recipe recipe) {
		if (!recipeList.contains(recipe)) {
			recipeList.add(recipe);
		}
		persistenceManager.save(recipe);
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
		for (Recipe recipe : recipeList)
			if (recipe.getId() == id)
				return recipe;
		return null;
	}

	public void updateRecipe() {
		recipeList = persistenceManager.readRecipes();
	}
}
