package ch.unibe.ese.shopnote.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.unibe.ese.shopnote.core.Comparators;

/**
 * Class which is used to combine different Items to a recipe
 * 
 */
public class Recipe extends Entity {
	private String name;
	private List<ShoppingListItem> itemList;
	private String notes;
	private boolean showNotes;
	private boolean shared;
	private int changesCount;
	
	// Temporary field, doesn't need to be persisted
	private long serverId;

	public Recipe(String name) {
		if(name.charAt(0) != '/')
			name = "/" + name;
		this.name = name;
		
		itemList = new ArrayList<ShoppingListItem>();
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

	public List<ShoppingListItem> getItemList() {
		Collections.sort(itemList, Comparators.ITEM_COMPARATOR);
		return Collections.unmodifiableList(itemList);
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
	
	public boolean isShared() {
		return shared;
	}
	
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	public int getChangesCount() {
		return changesCount;
	}
	
	public void setChangesCount(int changesCount) {
		this.changesCount = changesCount;
	}
	
	public void addItem(ShoppingListItem item) {
		if (!itemList.contains(item))
			itemList.add(item);
	}

	public void removeItem(Item item) {
		itemList.remove(item);
	}
	
	public void setServerId(long id) {
		this.serverId = id;
	}
	
	public long getServerId() {
		return this.serverId;
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
