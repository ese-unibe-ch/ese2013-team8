package ch.unibe.ese.shopnote.share.requests.listchange;

import ch.unibe.ese.shopnote.core.entities.ShoppingListItem;

/**
 * Request for changes happening on Items<br>
 * including: add, delete, change, set bought
 *
 */
public class ItemRequest extends ListChangeRequest {

	private static final long serialVersionUID = 7044889552973885929L;
	
	private ShoppingListItem item;
	private boolean isDeleted;
	private boolean isBought;

	public ItemRequest(String phoneNumber, long localListId, ShoppingListItem item) {
		super(phoneNumber, localListId);
		this.item = item;
		this.isDeleted = false;
		this.isBought = false;
	}
	
	public ShoppingListItem getItem() {
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

	@Override
	public ListChangeRequest getCopy() {
		ItemRequest copy = new ItemRequest(this.getPhoneNumber(), this.getLocalListId(), this.item);
		copy.setBought(this.isBought);
		copy.setDelete(this.isDeleted);
		copy.isRecipe(isRecipe());
		return copy;
	}
	
}
