package ch.unibe.ese.shopnote.core;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Items can be stored in shopping lists
 *
 */
public class Item  extends Entity implements Serializable {

	private static final long serialVersionUID = -6167160984174824511L;
	
	private String name;
	private BigDecimal price;
	private Shop shop;
	private String quantity;
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

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
	}

	public String toString() {
		return this.name;
	}

	public Item copy() {
		Item copy = new Item(this.name);
		copy.setBought(this.isBought());
		copy.setPrice(this.price);
		copy.setShop(this.shop);
		copy.setQuantity(this.quantity);
		return copy;
	}
	
}
