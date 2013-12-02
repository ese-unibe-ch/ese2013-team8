package ch.unibe.ese.shopnote.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;
import ch.unibe.ese.shopnote.share.SyncManager;
import ch.unibe.ese.shopnote.share.requests.FriendRequest;


/**
 * The FriendsManager provides all information about Friends locally stored on your phone<br>
 * He knows which friend is assigned to which shoppinglist.
 *
 */
public class FriendsManager {
	private List<Friend> friendsList;
	private PersistenceManager persistenceManager;
	private SyncManager syncManager;
	private BaseActivity baseActivity;
	
	private ArrayList<Friend> friendsInPhoneBook;
	private boolean noFriendAdded;

	public FriendsManager(PersistenceManager persistenceManager, SyncManager syncManager, BaseActivity baseActivity) {
		this.persistenceManager = persistenceManager;
		friendsList = persistenceManager.getFriends();
		this.syncManager = syncManager;
		this.baseActivity = baseActivity;
	}

	/**
	 * Checks if friend is on the server and adds him to the friendlist when
	 * successful
	 * 
	 * @param phoneNr
	 * @param name
	 * @return id of friend in long, -1 when failure 
	 */
	public long addFriend(Friend friend) {
		long id = checkIfDouble(friend);
		if (id >= 0) return id;
		
		// Add the friend
		friendsList.add(friend);

		// Save friend to database
		id = persistenceManager.save(friend);
		
		if(baseActivity != null && !friend.hasTheApp())
			checkIfFriendHasApp(friend);
		
		return id;
	}

	private void checkIfFriendHasApp(Friend friend) {
		FriendRequest fr = new FriendRequest(friend);
		syncManager.addRequest(fr);	
		syncManager.synchronise(baseActivity);
	}

	private long checkIfDouble(Friend friend) {
		for(Friend compare: friendsList) 
			if(compare.getPhoneNr().equals(friend.getPhoneNr())) return compare.getId();
		return -1l;
	}

	/**
	 * Gets a list of all friends added (also friends who do not have the app)
	 * @return List of all added friends, unmodifiable
	 */
	public List<Friend> getFriendsList() {
		Collections.sort(friendsList, Comparators.FRIEND_COMPARATOR);
		return Collections.unmodifiableList(friendsList);
	}
	
	/**
	 * Gets a list of all friends which have the app
	 * @return List of all friends with the app, unmodifiable
	 */
	public List<Friend> getFriendsWithApp() {
		List<Friend> list = new ArrayList<Friend>();
		for(Friend friend: friendsList)
			if(friend.hasTheApp())
				list.add(friend);
		return Collections.unmodifiableList(list);
	}

	/**
	 * Permits to update the name of a friend
	 * 
	 * @param updated
	 *            friend
	 */
	public void update(Friend friend) {
		persistenceManager.save(friend);
	}

	/**
	 * Removes friend only on the phone, no access to server is needed.
	 * 
	 * @param friend
	 *            to remove
	 * @return if successful
	 */
	public boolean removeFriend(Friend friend) {
		persistenceManager.removeFriend(friend);
		
		long id = friend.getId();
		for(Friend compare: friendsList)
			if(compare.getId() == id)
				return this.friendsList.remove(compare);
		return false;
	}

	/**
	 * Returns the friend to which the number belongs, if no friend found,
	 * returns null
	 * 
	 * @param PhoneNr
	 * @return
	 */
	public Friend getFriend(long id) {
		for (Friend friend : friendsList) {
			if (friend.getId() == id)
				return friend;
		}
		return null;
	}
	
	/**
	 * Get all friends which are assigned to this shoppinglist
	 * @param list
	 * @return
	 */
	public List<Friend> getSharedFriends(ShoppingList list) {
		return persistenceManager.getSharedFriends(list);
	}
	
	/**
	 * Adds a friend to a synchronized shopping list
	 * @param friend
	 * @param list
	 */
	public void addFriendToList(ShoppingList list, Friend friend) {
		//TODO: send the information to the server and add the friend to the list on the server
		persistenceManager.save(list, friend);
	}
	
	/**
	 * Deletes a friend from a synchronized shopping list
	 * @param list
	 * @param friend
	 */
	public void removeFriendOfList(ShoppingList list, Friend friend) {
		//TODO: send the information to the server and remove the friend on the server
		//(just the entry of the shared Shoppinglist, not the friend as a onject of course^^)
		persistenceManager.remove(list, friend);
	}
	
	/**
	 * Mark a friend as app-owner (used by the Syncmanager)
	 * @param friendId
	 */
	public void setFriendHasApp(long friendId) {
		if(friendId >= 0) {
			Friend friend = getFriend(friendId);
			friend.setHasApp();
			persistenceManager.save(friend);
		} else {
			for(Friend friend: friendsInPhoneBook) {
				if(friend.getId() == friendId) {
					noFriendAdded = false;
					addFriend(friend);
					friend.setHasApp();
					Looper.prepare();
					Toast.makeText(baseActivity.getApplicationContext(), "Friend added: " + friend.getName(), Toast.LENGTH_SHORT).show();
					Looper.loop();
				}
			}

		}
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Friend friend : friendsList)
			result.append(friend).append("\n");
		return result.toString();
	}
	
	/**
	 * Gets all Contacts from phone book and checks if someone has the app and adds him to the friendslibrary
	 */
	public void checkPhoneBookForFriends() {
		new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				new Handler() {
		              public void handleMessage(Message msg) {
		            	  super.handleMessage(msg);
		              }
				};
				checkPhoneBookForFriendsHelper();
				Looper.loop();
			}

			private void checkPhoneBookForFriendsHelper() {
				friendsInPhoneBook = new ArrayList<Friend>();
				getListWithFriendsFromPhoneBook(friendsInPhoneBook);
				noFriendAdded = true;
		        
				for(Friend friend: friendsInPhoneBook) {
					FriendRequest fr = new FriendRequest(friend);
					syncManager.addRequest(fr);
				}
				syncManager.synchronise(baseActivity);
				
//				if(noFriendAdded)
//					Toast.makeText(baseActivity.getApplicationContext(), "Sadly no one of your friends has the app", Toast.LENGTH_SHORT).show();
				
				Toast.makeText(baseActivity.getApplicationContext(), "Synchronization finished", Toast.LENGTH_SHORT).show();
			}
		}).start();
	}

	
	private void getListWithFriendsFromPhoneBook(
			ArrayList<Friend> friendsInPhoneBook) {
		ContentResolver cr = baseActivity.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        long idCounter = -1;
        
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                  String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                  if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                     Cursor pCur = cr.query(
                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                               null,
                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                               new String[]{id}, null);
                     while (pCur.moveToNext()) {
                         String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                         Friend friend = new Friend(phoneNo, name);
                         friend.setId(idCounter--);
                         friendsInPhoneBook.add(friend);
                     }
                    pCur.close();
                }
            }
        }
	}
}
