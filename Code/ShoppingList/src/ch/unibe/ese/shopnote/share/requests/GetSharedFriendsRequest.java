package ch.unibe.ese.shopnote.share.requests;

import java.util.ArrayList;


/**
 * This request asks for all participants of a shared list.<br>
 * If a participant is added on the server, but didn't download the List yet, he isn't in this list
 */
public class GetSharedFriendsRequest extends Request {

	private static final long serialVersionUID = 5397098567269278909L;
	
	private long localListId;
	private boolean isRecipe;

	private ArrayList<String> friendNumbers;
	
	public GetSharedFriendsRequest(String phoneNumber, long localListId) {
		super(phoneNumber);
		this.localListId = localListId;
		this.friendNumbers = new ArrayList<String>();
	}
	
	public long getLocalListId() {
		return localListId;
	}

	public void setLocalListId(long localListId) {
		this.localListId = localListId;
	}
	
	public ArrayList<String> getFriendNumbers() {
		return friendNumbers;
	}

	public void setFriendNumbers(ArrayList<String> friendNumbers) {
		this.friendNumbers = friendNumbers;
	}
	
	public void addFriendNumber(String number) {
		this.friendNumbers.add(number);
	}
	
	public void isRecipe(boolean b) {
		this.isRecipe = b;
	}
	
	public boolean isRecipe() {
		return this.isRecipe;
	}

	@Override
	public int getType() {
		return Request.GET_SHARED_FRIENDS_REQUEST;
	}

}
