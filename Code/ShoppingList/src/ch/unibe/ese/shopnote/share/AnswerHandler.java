package ch.unibe.ese.shopnote.share;

import android.content.Context;
import ch.unibe.ese.shopnote.core.ShoppingList;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.share.requests.CreateSharedListRequest;
import ch.unibe.ese.shopnote.share.requests.FriendRequest;
import ch.unibe.ese.shopnote.share.requests.ListChangeRequest;
import ch.unibe.ese.shopnote.share.requests.RenameListRequest;
import ch.unibe.ese.shopnote.share.requests.Request;

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
	
	public void setRequests(Request... requests) {
		for (Request r: requests) {
			if(r.wasSuccessful()) {
				setConsequences(r);
			}
			if(r.isHandled()) {
				// Do something
			} else {
				syncManager.addRequest(r);
			}
		}
	}
	
	private void setConsequences(Request request) {
		switch (request.getType()) {
		case Request.FRIEND_REQUEST:
			long friendId = ((FriendRequest)request).getFriendId();
			friendsManager.setFriendHasApp(friendId);
			return;
		case Request.SHARELIST_REQUEST:
			//TODO set the list shared flag on true
			return;
		case Request.CREATE_SHARED_LIST_REQUEST:
			String listName = ((CreateSharedListRequest)request).getListName();
			listManager.saveShoppingList(new ShoppingList(listName));
			ShoppingList list = listManager.getShoppingLists().get(listManager.getShoppingLists().size()-1);
			long id = list.getId();
			((CreateSharedListRequest)request).setLocalListId(id);
			syncManager.addRequest(request);
			return;
		case Request.LIST_CHANGE_REQUEST:
			processListChangeRequest((ListChangeRequest)request);
		}
	}
	
	private void processListChangeRequest(ListChangeRequest request) {
		ShoppingList list = listManager.getShoppingList(request.getLocalListId());
		switch (request.getType()) {
		case ListChangeRequest.RENAME_LIST_REQUEST:
			list.setName(((RenameListRequest)request).getNewName());
			listManager.saveShoppingList(list);
			return;
		}
	}

	public void updateUI() {
		context.refresh();
	}
}
