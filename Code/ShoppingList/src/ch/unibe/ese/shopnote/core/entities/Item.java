package ch.unibe.ese.shopnote.core.entities;

import java.math.BigDecimal;

import ch.unibe.ese.shopnote.core.ItemUnit;

public abstract class Item extends Entity {

	protected String name;
	protected BigDecimal price;
	protected Shop shop;
	protected BigDecimal quantity;
	protected ItemUnit unit;
	
	/**
	 * Creates a new Item with the values from <code>item</code>.
	 * 
	 * @param item not null
	 */
	protected Item(Item item){
		this.name = item.name;
		this.price = item.price;
		this.shop = item.shop;
		this.quantity = item.quantity;
		this.unit = item.unit;
		
		invariant();
	}
	
	/**
	 * Creates a new Item with the given name.
	 * @param name not <code>null</code> or {@link String#isEmpty() empty}
	 */
	protected Item(String name) {
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

	protected void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
		if (quantity == null ^ unit == null)
			throw new IllegalArgumentException(String.format(
					"It's not allowed to have either quantity or unit null: Quantity: %s. Unit: %s", quantity, unit));
	}

	public String toString() {
		return this.name;
	}
}