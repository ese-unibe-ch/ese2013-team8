package ch.unibe.ese.shopnote.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ListManager is responsible for access to all {@link ShoppingList
 * shoppinglists}.
 */
public class ListManager {

	private static final BigDecimal THOUSAND = new BigDecimal("1000");
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
	 * Adds the item to the shoppinglist
	 * 
	 * @param item
	 *            not null
	 * @param list
	 *            not null
	 * @return true if the item was added (false if it only was updated or not
	 *         changed at all)
	 */
	public boolean addItemToList(Item item, ShoppingList list) {
		if (item == null || list == null)
			throw new IllegalArgumentException("null is not allowed");
		List<Item> items = listToItems.get(list);
		if (items == null) {
			items = new ArrayList<Item>();
			listToItems.put(list, items);
		}
		for (Item item2 : items) {
			if (!item2.isBought() && item2.getName().equals(item.getName())) {
				if (item.getUnit() != null && item.getUnit() == item2.getUnit()) {
					BigDecimal newQuantity = item.getQuantity().add(item2.getQuantity());
					item2.setQuantity(newQuantity, item2.getUnit());
					item = item2; // we want to save only one item.
				} else if (ItemUnit.MASSES.contains(item.getUnit()) && ItemUnit.MASSES.contains(item2.getUnit())) {
					// Both units are masses. Convert it first to grams and then add them.
					BigDecimal mass1 = item.getUnit()==ItemUnit.GRAM ? item.getQuantity():item.getQuantity().multiply(THOUSAND);
					BigDecimal mass2 = item2.getUnit()==ItemUnit.GRAM ? item2.getQuantity():item2.getQuantity().multiply(THOUSAND);
					BigDecimal finalMass = mass1.add(mass2);
					ItemUnit unit = ItemUnit.GRAM;
					if (finalMass.compareTo(THOUSAND) > 0){
						finalMass = finalMass.divide(THOUSAND);
						unit = ItemUnit.KILO_GRAM;
					}
					item2.setQuantity(finalMass, unit);
					item = item2;
				}
			}
		}
		persistenceManager.save(item, list);
		if (!items.contains(item))
			return items.add(item);
		return false;
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
		Collections.sort(items, Comparators.ITEM_COMPARATOR);
		return Collections.unmodifiableList(items);
	}

	/**
	 * Gets all items-objects which are in the item table
	 * 
	 * @return ArrayList<Item>
	 */
	public List<Item> getAllItems() {
		List<Item> items = persistenceManager.getAllItems();
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
	public Item getItem(Long id) {
		List<Item> listOfItems = persistenceManager.getAllItems();
		for (Item item : listOfItems) {
			if (item.getId() == id)
				return item;
		}
		return null;
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

	public Recipe getRecipeAt(int position) {
		return recipeList.get(position);
	}

	public void updateRecipe() {
		recipeList = persistenceManager.readRecipes();
	}

}
