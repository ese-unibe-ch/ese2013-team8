package ch.unibe.ese.core;

import java.util.ArrayList;

/**
 * Class which is used to combine different Items to a recipe
 * 
 * @author ESE TEAM 8
 * 
 */
public class Recipe {
	private Long id;
	private String name;
	private ArrayList<Item> itemList;

	public Recipe(String name) {
		this.name = name;
		itemList = new ArrayList<Item>();
	}

	public String toString() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		if (this.id != null)
			throw new IllegalArgumentException(
					"it's not allowed to set the ID of an item twice");
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		invariant();
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<Item> list) {
		this.itemList = list;
	}

	public void addItem(Item item) {
		if (!itemList.contains(item))
			itemList.add(item);
	}

	public void removeItem(Item item) {
		itemList.remove(item);
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException(
					"it's not allowed to set the ID of an item twice");
	}

	public boolean contains(Item item) {
		for (Item compare : itemList)
			if (item.equals(compare))
				return true;
		return false;
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
		Recipe other = (Recipe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
