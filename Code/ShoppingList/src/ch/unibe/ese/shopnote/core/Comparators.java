package ch.unibe.ese.shopnote.core;

import java.util.Comparator;

/**
 * Bietet {@link Comparator} an.
 */
public class Comparators {
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
	 * Comparator für {@link Item}.
	 * <p>
	 * Sortiert anhand {@link Item#getName() Name} aufsteigend.
	 */
	public static final Comparator<Item> ITEM_COMPARATOR = new Comparator<Item>() {

		@Override
		public int compare(Item lhs, Item rhs) {
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
