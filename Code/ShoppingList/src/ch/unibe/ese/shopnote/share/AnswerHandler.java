package ch.unibe.ese.shopnote.share;

import java.util.List;

import android.content.Context;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ShoppingList;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.share.requests.CreateSharedListRequest;
import ch.unibe.ese.shopnote.share.requests.FriendRequest;
import ch.unibe.ese.shopnote.share.requests.Request;
import ch.unibe.ese.shopnote.share.requests.ShareListRequest;
import ch.unibe.ese.shopnote.share.requests.UnShareListRequest;
import ch.unibe.ese.shopnote.share.requests.listchange.ItemRequest;
import ch.unibe.ese.shopnote.share.requests.listchange.ListChangeRequest;
import ch.unibe.ese.shopnote.share.requests.listchange.RenameListRequest;

/**
 * Gets passed in a RequestSender to listen to its result
 * Updates all neccessary information in the core and the UI
 *
 */

public class AnswerHandler {

	private BaseActivity context;
	private FriendsManager friendsManager;
	private SyncManager syncManager;
	private ListManager listManager;
	
	public AnswerHandler(Context context) {
		this.context = (BaseActivity) context;
		friendsManager = this.context.getFriendsManager();
		syncManager = this.context.getSyncManager();
		listManager = this.context.getListManager();
	}
	
	/**
	 * Sets answers to requests and tells the Handler to handle them accordingly
	 * @param requests
	 */
	public void setRequests(Request... requests) {
		for (Request r: requests) {
			if(r.wasSuccessful()) {
				setConsequences(r);
			}
			if(r.isHandled()) {
				// This will be used for error reporting
				// i.e. Reporting to the user something went wrong (Handled but not successful)
			} else {
				syncManager.addRequest(r);
			}
		}
	}
	
	/**
	 * Set consequences for answers on requests
	 * @param request
	 */
	private void setConsequences(Request request) {
		switch (request.getType()) {
		
		// Empty requests are like pings, just say "hello"
		case Request.EMPTY_REQUEST:
			return;
		
		// Sent a FriendRequest to the server and got the answer if my friend is registered with the app
		case Request.FRIEND_REQUEST:
			long friendId = ((FriendRequest)request).getFriendId();
			friendsManager.setFriendHasApp(friendId);
			return;
			
		// ShareListRequest => List has been successfully shared with my friend
		case Request.SHARELIST_REQUEST:
			ShoppingList list = listManager.getShoppingList(((ShareListRequest)request).getListId());
			list.setShared(true);
			return;
			
		// UnshareListRequest => My friend has been successfully deleted from the sharing list on the server
		case Request.UNSHARELIST_REQUEST:
			ShoppingList list2 = listManager.getShoppingList(((UnShareListRequest)request).getListId());
			if(friendsManager.getSharedFriends(list2).isEmpty()) {
				list2.setShared(false);
			}
			return;
			
		// The server asks me to create a new shared list (one that has been shared by another user)
		case Request.CREATE_SHARED_LIST_REQUEST:
			String listName = ((CreateSharedListRequest)request).getListName();
			ShoppingList newList = new ShoppingList(listName);
			newList.setShared(true);
			// Sets the local List ID
			listManager.saveShoppingList(newList);
			
			for(String number : ((CreateSharedListRequest)request).getSharedFriendNumbers()) {
				System.err.println("Friend Number: "+ number);
				friendsManager.addFriend(new Friend(number,"User"));
				friendsManager.addFriendToList(newList, friendsManager.getFriendWithPhoneNr(number));
			}
			
			long id = newList.getId();
			((CreateSharedListRequest)request).setLocalListId(id);
			syncManager.addRequest(request);
			syncManager.synchronise(context);
			return;
			
		// Those are the general requests concerning the content of a list (name, items, ...)
		// See processListChangeRequest() for further details on how to handle them
		case Request.LIST_CHANGE_REQUEST:
			processListChangeRequest((ListChangeRequest)request);
			return;
		}
	}
	
	/**
	 * This method processes all requests that want to change the content or the name of a list
	 * @param request
	 */
	private void processListChangeRequest(ListChangeRequest request) {
		ShoppingList list = listManager.getShoppingList(request.getLocalListId());
		switch (request.getSubType()) {
		
		// One of your sharing partners has changed the name of the list
		case ListChangeRequest.RENAME_LIST_REQUEST:
			list.setName(((RenameListRequest)request).getNewName());
			listManager.saveShoppingList(list);
			return;
			
		// One of your sharing partners has added/changed an item in the list
		case ListChangeRequest.ITEM_REQUEST:
			List<Item> itemlist = listManager.getItemsFor(list);
			Item receivedItem = ((ItemRequest)request).getItem().copy();
			Item localItem = receivedItem;
			for(Item i: itemlist) {
				if(i.getName().equals(receivedItem.getName())) {
					localItem = i;
				}
			}
			
			// set change notification count
			int changesCount = list.getChangesCount();
			
			if (((ItemRequest) request).isDeleted()) {
				listManager.removeItemFromList(localItem,list);
				changesCount++;
			} 
			else {
				localItem.setBought(((ItemRequest) request).isBought());
				listManager.addItemToList(localItem,list);
				changesCount++;
			}
			
			list.setChangesCount(changesCount);
			listManager.saveShoppingList(list);

			return;
		}
	}

	/**
	 * Calls the refresh method on the activity
	 * If the activity is already destroyed it simply does nothing (except a little memory leak)
	 * This is the only method which is called on the UI thread after the communication with the server
	 */
	public void updateUI() {
		context.refresh();
	}
}
