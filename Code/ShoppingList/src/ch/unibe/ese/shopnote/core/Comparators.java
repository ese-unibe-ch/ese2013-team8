package ch.unibe.ese.shopnote.core;

import java.util.Comparator;

import ch.unibe.ese.shopnote.core.entities.Friend;
import ch.unibe.ese.shopnote.core.entities.ShoppingListItem;
import ch.unibe.ese.shopnote.core.entities.Recipe;
import ch.unibe.ese.shopnote.core.entities.ShoppingList;

/**
 * Bietet {@link Comparator} an.
 */
public class Comparators {
	
	private Comparators(){
		// private constructor for util class.
	}

	/**
	 * Comparator für {@link ShoppingList}.
	 * <p>
	 * Sortiert anhand {@link ShoppingList#getName() Name} aufsteigend.
	 */
	public static final Comparator<ShoppingList> LIST_COMPARATOR = new Comparator<ShoppingList>() {

		@Override
		public int compare(ShoppingList lhs, ShoppingList rhs) {
			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	};
	/**
	 * Comparator für {@link ShoppingListItem}.
	 * <p>
	 * Sortiert anhand {@link ShoppingListItem#getName() Name} aufsteigend.
	 */
	public static final Comparator<ShoppingListItem> ITEM_COMPARATOR = new Comparator<ShoppingListItem>() {

		@Override
		public int compare(ShoppingListItem lhs, ShoppingListItem rhs) {
			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	};
	/**
	 * Comparator für {@link Friend}.
	 * <p>
	 * Sortiert anhand {@link Friend#getName() Name} aufsteigend.
	 */
	public static final Comparator<Friend> FRIEND_COMPARATOR = new Comparator<Friend>() {

		@Override
		public int compare(Friend lhs, Friend rhs) {
			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	};
	/**
	 * Comparator für {@link Recipe}.
	 * <p>
	 * Sortiert anhand {@link Recipe#getName() Name} aufsteigend.
	 */
	public static final Comparator<Recipe> RECIPE_COMPARATOR = new Comparator<Recipe>() {
		
		@Override
		public int compare(Recipe lhs, Recipe rhs) {
			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	};
}
