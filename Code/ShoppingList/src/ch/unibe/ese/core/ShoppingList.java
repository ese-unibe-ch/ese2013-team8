package ch.unibe.ese.core;

import java.util.Date;

public class ShoppingList {
	private String name;
	private Date dueDate;
	private String shop;
	private boolean archived;
	private Long id;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		if (this.id != null)
			throw new IllegalStateException(
					"It's not allowed to set the id twice. Old: " + this.id
							+ "/New: " + id);
		this.id = id;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String toString() {
		return this.name;
	}
}
