package ch.unibe.ese.shopnote.core;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Items can be stored in shopping lists
 * 
 */
public class Item extends Entity implements Serializable {

	private static final long serialVersionUID = -6167160984174824511L;

	private String name;
	private BigDecimal price;
	private Shop shop;
	private BigDecimal quantity;
	private ItemUnit unit;
	private boolean bought;

	public Item(String name) {
		if (name != null)
			this.name = name.trim();
		invariant();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		invariant();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public boolean isBought() {
		return bought;
	}

	public void setBought(boolean bought) {
		this.bought = bought;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity with its unit.
	 * <p>
	 * Either both are null or both are not null.
	 * 
	 * @param quantity
	 * @param unit
	 */
	public void setQuantity(BigDecimal quantity, ItemUnit unit) {
		this.quantity = quantity;
		this.unit = unit;
		invariant();
	}

	public ItemUnit getUnit() {
		return unit;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
		if (quantity == null ^ unit == null)
			throw new IllegalArgumentException(String.format(
					"It's not allowed to have either quantity or unit null: Quantity: %s. Unit: %s", quantity, unit));
	}

	public String toString() {
		return this.name;
	}

	public Item copy() {
		Item copy = new Item(this.name);
		copy.setBought(this.isBought());
		copy.setPrice(this.price);
		copy.setShop(this.shop);
		copy.setQuantity(this.quantity, this.unit);
		return copy;
	}
}
