package ch.unibe.ese.shopnote.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;
import ch.unibe.ese.shopnote.core.FriendsManager;
import ch.unibe.ese.shoppinglist.R;

/**
 * Creates the Action Bar for Friends to edit or remove them
 * @author ESE Team 8
 *
 */

public class FriendListActionMode implements Callback {

	private FriendsManager manager;
	private Friend selectedFriend;
	private ArrayAdapter<Friend> friendsAdapter;
	private Activity activity;
	
	public FriendListActionMode(FriendsManager manager, ArrayAdapter<Friend> friendsAdapter, Friend selectedFriend, Activity homeActivity) {
		this.manager = manager;
		this.friendsAdapter = friendsAdapter;
		this.selectedFriend = selectedFriend;
		this.activity = homeActivity;
	}
	
	
    // Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.actionbar_longpress_friendlist, menu);
        return true;
    }

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
	                friendsAdapter.notifyDataSetChanged();
	                mode.finish(); 
	                // open list edit screen
		        	Intent intent = new Intent(activity, CreateFriendActivity.class);
		        	intent.putExtra(BaseActivity.EXTRAS_FRIEND_ID, selectedFriend.getId());
		            activity.startActivity(intent);

                return true;
            case R.id.action_remove:
	            	manager.removeFriend(selectedFriend);
	            	friendsAdapter.remove(selectedFriend);
	            	friendsAdapter.notifyDataSetChanged();
	            	mode.finish(); // Action picked, so close the CAB

            	return true;
           
            default:
                return false;
        }
    }

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// nothing to do
	}
}
