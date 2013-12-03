package ch.unibe.ese.shopnote.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which is used to combine different Items to a recipe
 * 
 */
public class Recipe extends Entity {
	private String name;
	private List<Item> itemList;
	private String notes;
	private boolean showNotes;

	public Recipe(String name) {
		if(name.charAt(0) != '/')
			name = "/" + name;
		this.name = name;
		
		itemList = new ArrayList<Item>();
	}

	public String toString() {
		return name.replace("/", "");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		invariant();
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> list) {
		this.itemList = list;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public boolean isNotesVisible() {
		return showNotes;
	}

	public void setNotesVisible(boolean showNotes) {
		this.showNotes = showNotes;
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
}
