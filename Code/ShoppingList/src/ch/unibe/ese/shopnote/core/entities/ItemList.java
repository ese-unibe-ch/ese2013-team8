package ch.unibe.ese.shopnote.core.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.unibe.ese.shopnote.core.ItemException;
import ch.unibe.ese.shopnote.core.ItemUnit;

/**
 * Represents a list of items.
 */
public abstract class ItemList<I extends Item> extends Entity {
	private List<I> items;
	private String name;

	protected ItemList(String name) {
		this.name = name.trim();
		this.items = new ArrayList<I>();
		invariant();
	}

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            not <code>null</code>
	 */
	public void setName(String name) {
		this.name = name;
		invariant();
	}
	
	public void addItem(I item) {
		
	}
	
// TODO refactor this shit	
//	/**
//	 * Merges the newItem into the list of existing items.<p>
//	 * If the list contains an item with the same name and the same unit, it will merge this item with the newItem.
//	 * @param newItem
//	 * @param items
//	 * @return the item to persist.
//	 * @throws ItemException thrown if another item with the same name, but different unit is in the list.
//	 */
//	private ShoppingListItem mergeItem(I newItem) throws ItemException{
//		for (I item2 : items) {
//			if (!item2.isBought() && item2.getName().equals(newItem.getName())) {
//				// sum price
//				if (newItem.getPrice() != null) {
//					if (item2.getPrice() == null)
//						item2.setPrice(newItem.getPrice());
//					else 
//						item2.setPrice(item2.getPrice().add(newItem.getPrice()));
//				}
//				if (newItem.getUnit() == null && item2.getUnit() == null) {
//					// Both have no unit --> item has now 2 pieces.
//					item2.setQuantity(BigDecimal.valueOf(2), ItemUnit.PIECE);
//					return item2;
//				} else if (newItem.getUnit() == null && item2.getUnit() == ItemUnit.PIECE) {
//					// new Item has no unit, existing item has PIECE as unit. add one piece to the existing item.
//					item2.setQuantity(item2.getQuantity().add(BigDecimal.ONE), ItemUnit.PIECE);
//					return item2;
//				} else if (newItem.getUnit() != null && newItem.getUnit() == item2.getUnit()) {
//					// Both have same unit --> add them together
//					BigDecimal newQuantity = newItem.getQuantity().add(item2.getQuantity());
//					item2.setQuantity(newQuantity, item2.getUnit());
//					return item2; // we want to save only one item.
//				} else if (ItemUnit.MASSES.contains(newItem.getUnit()) && ItemUnit.MASSES.contains(item2.getUnit())) {
//					// Both units are masses. Convert it first to grams and then add them.
//					BigDecimal mass1 = newItem.getUnit()==ItemUnit.GRAM ? newItem.getQuantity():newItem.getQuantity().multiply(THOUSAND);
//					BigDecimal mass2 = item2.getUnit()==ItemUnit.GRAM ? item2.getQuantity():item2.getQuantity().multiply(THOUSAND);
//					BigDecimal finalMass = mass1.add(mass2);
//					ItemUnit unit = ItemUnit.GRAM;
//					if (finalMass.compareTo(THOUSAND) > 0){
//						finalMass = finalMass.divide(THOUSAND);
//						unit = ItemUnit.KILO_GRAM;
//					}
//					item2.setQuantity(finalMass, unit);
//					return item2;
//				} else {
//					throw new ItemException();
//				}
//			}
//		}
//		return newItem;
//	}


	public List<I> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	@Override
	public String toString() {
		return name;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
	}
}
