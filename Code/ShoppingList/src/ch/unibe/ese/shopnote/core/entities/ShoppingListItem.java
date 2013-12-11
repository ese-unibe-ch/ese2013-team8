package ch.unibe.ese.shopnote.core.entities;

import java.io.Serializable;


/**
 * Represents an Item can be stored in a {@link ShoppingList ShoppingList}.
 * 
 */
public class ShoppingListItem extends Item implements Serializable {

	private static final long serialVersionUID = -6167160984174824511L;

	private boolean bought;

	public ShoppingListItem(String name) {
		super(name);
		
		invariant();
	}

	public boolean isBought() {
		return bought;
	}

	public void setBought(boolean bought) {
		this.bought = bought;
	}

	public ShoppingListItem copy() {
		ShoppingListItem copy = new ShoppingListItem(this.name);
		copy.setBought(this.isBought());
		copy.setPrice(this.price);
		copy.setShop(this.shop);
		copy.setQuantity(this.quantity, this.unit);
		return copy;
	}
}
