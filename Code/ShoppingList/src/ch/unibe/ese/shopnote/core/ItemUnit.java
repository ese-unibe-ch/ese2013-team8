package ch.unibe.ese.shopnote.core;

import java.util.Arrays;
import java.util.List;

/**
 * Defines the allowed units for an item.
 */
public enum ItemUnit {
	// Do not change the order of these. it must be the same as in strings.xml->item_units for the correct lookup.
	PIECE, GRAM, KILO_GRAM, LITER;
	
	/**
	 * List that contains all masses.
	 */
	public static final List<ItemUnit> MASSES = Arrays.asList(GRAM, KILO_GRAM);
}
