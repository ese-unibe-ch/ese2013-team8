package ch.unibe.ese.core;

import java.util.Date;

public class ShoppingList {
	private String name;
	private Date dueDate;
	private String shop;

	/**
	 * @param name
	 *            not <code>null</code>
	 */
	public ShoppingList(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((shop == null) ? 0 : shop.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingList other = (ShoppingList) obj;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (shop == null) {
			if (other.shop != null)
				return false;
		} else if (!shop.equals(other.shop))
			return false;
		return true;
	}
}
