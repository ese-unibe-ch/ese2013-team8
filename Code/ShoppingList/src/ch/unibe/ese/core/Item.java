package ch.unibe.ese.core;

import java.math.BigDecimal;

public class Item {
	private int id;
	private String name;
	private BigDecimal price;
	private Shop shop;
	private boolean bought;

	public Item(String name) {
		this.name = name;
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

	public int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	public boolean isBought() {
		return bought;
	}

	public void setBought(boolean bought) {
		this.bought = bought;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
	}

}
