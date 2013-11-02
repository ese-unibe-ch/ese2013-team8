package ch.unibe.ese.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import ch.unibe.ese.core.Item;
import ch.unibe.ese.core.ListManager;
import ch.unibe.ese.core.ShoppingList;
import ch.unibe.ese.shoppinglist.R;

public class ShoppingListActionMode implements Callback {

	private ListManager manager;
	private ShoppingList selectedList;
	private ArrayAdapter<ShoppingList> shoppingListAdapter;
	private Activity activity;
	private Item selectedItem;
	private ArrayAdapter<Item> itemAdapter;
	private boolean isList;
	
	/**
	 * Is called when longpress on shoppinglist in homescreen 
	 * @param manager
	 * @param selectedList
	 * @param shoppingListAdapter
	 * @param homeActivity
	 */
	public ShoppingListActionMode(ListManager manager, ShoppingList selectedList, ArrayAdapter<ShoppingList> shoppingListAdapter, Activity homeActivity) {
		this.manager = manager;
		this.selectedList = selectedList;
		this.shoppingListAdapter = shoppingListAdapter;
		this.activity = homeActivity;
		isList = true;
	}
	
	/**
	 * Is called when longpress on item in a shoppinglist
	 * @param manager
	 * @param selectedItem
	 * @param selectedList
	 * @param itemAdapter
	 * @param activity
	 */
	public ShoppingListActionMode(ListManager manager, Item selectedItem, ShoppingList selectedList, ArrayAdapter<Item> itemAdapter, Activity activity) {
		this.manager = manager;
		this.selectedItem = selectedItem;
		this.selectedList = selectedList;
		this.itemAdapter = itemAdapter;
		this.activity = activity;
		isList = false;
	}
	
	/**
	 * Is called when longpress on item in the ItemListManager
	 * @param manager
	 * @param selectedItem
	 * @param itemAdapter
	 * @param activity
	 */
	public ShoppingListActionMode(ListManager manager, Item selectedItem, ArrayAdapter<Item> itemAdapter, Activity activity){
		this.manager = manager;
		this.selectedItem = selectedItem;
		this.selectedList = null;
		this.itemAdapter = itemAdapter;
		this.activity = activity;
		isList = false;
	}
	
    // Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
    	int listIndex = -1;
    	if(selectedList != null) //when called from ItemList, listIndex stays -1 
    		listIndex = manager.getShoppingLists().indexOf(selectedList);
    	
        switch (item.getItemId()) {
            case R.id.action_edit:
            	if (isList) {
	                shoppingListAdapter.notifyDataSetChanged();
	                mode.finish(); 
	                // open list edit screen
		        	Intent intent = new Intent(activity, CreateListActivity.class);
		        	intent.putExtra("selectedList", listIndex);
		            activity.startActivity(intent);
            	}
            	else {
	                itemAdapter.notifyDataSetChanged();
	                mode.finish(); 
	                // open item edit screen
		        	Intent intent = new Intent(activity, CreateItemActivity.class);
		        	intent.putExtra("selectedItem",selectedItem.getId());
		        	intent.putExtra("editItem", true);
		            activity.startActivityForResult(intent, 1);	
            	}
                return true;
            case R.id.action_remove:
            	if (isList) {
            		//TODO: just temporary bugfix to update the list from here!
	            	manager.removeShoppingList(selectedList);
	            	shoppingListAdapter.notifyDataSetChanged();
	            	mode.finish(); // Action picked, so close the CAB
            	}
            	else {
            		if(selectedList != null){
            			manager.removeItemFromList(selectedItem, selectedList);
            			ViewListActivity viewListActivity = (ViewListActivity) activity;
            			viewListActivity.updateAdapters();
            		} else {
            			manager.remove(selectedItem);
            			ItemListActivity itemListActivity = (ItemListActivity) activity;
            			itemListActivity.updateAdapter();
            		}
            		itemAdapter.notifyDataSetChanged();
	            	mode.finish(); // Action picked, so close the CAB
            	}
            	return true;
            case R.id.action_share:
            	if(selectedList != null){
	            	Intent intentShare = new Intent(activity, ShareListActivity.class);
	            	intentShare.putExtra("selectedList", listIndex);
	                activity.startActivity(intentShare);
	                mode.finish(); // Action picked, so close the CAB
	            	return true;
            	} else
            		Toast.makeText(activity, "Not implemented yet", Toast.LENGTH_SHORT).show();

            default:
                return false;
        }
    }

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		
	}

    
}
