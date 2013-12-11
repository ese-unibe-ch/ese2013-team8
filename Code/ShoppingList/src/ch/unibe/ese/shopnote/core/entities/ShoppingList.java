package ch.unibe.ese.shopnote.core.entities;

import java.util.Date;


/**
 * This class represents the ShoppingList with it's attributes <br>
 * It knows nothing about the item it contains. This knowledge is stored in the Listmanager.
 *
 */
public class ShoppingList extends ItemList<ShoppingListItem> {
	private Date dueDate;
	private String shop;
	private boolean archived;
	private boolean shared;
	private int changesCount;
	// Only temporary => doesn't need to be persisted
	private long serverListId;

	/**
	 * @param name
	 *            not <code>null</code>
	 */
	public ShoppingList(String name) {
		super(name);
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
	
	public int getChangesCount() {
		return changesCount;
	}
	
	public void setChangesCount(int changesCount) {
		this.changesCount = changesCount;
	}
	
	public long getServerListId() {
		return serverListId;
	}

	public void setServerListId(long serverListId) {
		this.serverListId = serverListId;
	}
}
