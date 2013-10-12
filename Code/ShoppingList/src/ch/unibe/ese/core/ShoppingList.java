package ch.unibe.ese.core;

import java.util.Date;

public class ShoppingList {
	private String name;
	private Date dueDate;
	private String shop;

	public ShoppingList(String name) {
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	private void invariant() {
		if (this.name == null || this.name.length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
	}
}
