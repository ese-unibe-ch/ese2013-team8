package ch.unibe.ese.shoppinglist;

import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import android.app.Activity;
import android.content.Intent;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class ShoppingListActionMode implements Callback {

	private ListManager manager;
	private ShoppingList selectedList;
	private ArrayAdapter shoppingListAdapter;
	private Activity homeActivity;
	private int listIndex;
	
	public ShoppingListActionMode(ListManager manager, ShoppingList selectedList, ArrayAdapter shoppingListAdapter, Activity homeActivity) {
		this.manager = manager;
		this.selectedList = selectedList;
		this.shoppingListAdapter = shoppingListAdapter;
		this.homeActivity = homeActivity;
	}
    // Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.actionbar_longpress_shoppinglist, menu);
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
                shoppingListAdapter.notifyDataSetChanged();
                mode.finish(); 
                // open list edit screen
	        	Intent intent = new Intent(homeActivity, CreateListActivity.class);
	        	listIndex = manager.getShoppingLists().indexOf(selectedList);
	        	intent.putExtra("selectedList", listIndex);
	            homeActivity.startActivity(intent);
                return true;
            case R.id.action_remove:
            	manager.removeShoppingList(selectedList);
            	shoppingListAdapter.notifyDataSetChanged();
            	mode.finish(); // Action picked, so close the CAB
            	return true;
            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
//        mActionMode = null;
    }

}
