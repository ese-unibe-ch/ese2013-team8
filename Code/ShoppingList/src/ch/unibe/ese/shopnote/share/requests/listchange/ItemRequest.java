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
	private boolean delete;
	
	public ItemRequest(String phoneNumber, long localListId, Item item) {
		super(phoneNumber, localListId);
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setDelete(boolean isDeleted) {
		this.delete = isDeleted;
	}
	
	public boolean isDeleted() {
		return delete;
	}

	@Override
	public int getSubType() {
		return ListChangeRequest.ITEM_REQUEST;
	}
	
}
