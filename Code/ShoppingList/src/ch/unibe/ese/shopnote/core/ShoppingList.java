package ch.unibe.ese.shopnote.core;

import java.util.Date;

public class ShoppingList extends Entity{
	private String name;
	private Date dueDate;
	private String shop;
	private boolean archived;
	private boolean shared;

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

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	public boolean isShared() {
		return shared;
	}
	
	public void setShared(boolean shared) {
		this.shared = shared;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
	}

	public String toString() {
		return this.name;
	}
}
