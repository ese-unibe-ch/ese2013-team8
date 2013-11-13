package ch.unibe.ese.shopnote.share.requests.listchange;

import ch.unibe.ese.shopnote.core.Item;

/**
 * Request for changes happening on Items<br>
 * including: add, delete, change, set bought
 *
 */
public class ItemRequest extends ListChangeRequest {

	private static final long serialVersionUID = 7044889552973885929L;
	
	private Item item;
	private boolean isDeleted;
	private boolean isBought;

	public ItemRequest(String phoneNumber, long localListId, Item item) {
		super(phoneNumber, localListId);
		this.item = item;
		this.isDeleted = false;
		this.isBought = false;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setDelete(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public boolean isBought() {
		return isBought;
	}

	public void setBought(boolean isBought) {
		this.isBought = isBought;
	}

	@Override
	public int getSubType() {
		return ListChangeRequest.ITEM_REQUEST;
	}
	
}
