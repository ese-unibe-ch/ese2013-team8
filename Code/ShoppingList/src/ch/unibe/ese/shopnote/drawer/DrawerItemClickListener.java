package ch.unibe.ese.shopnote.drawer;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import ch.unibe.ese.shopnote.activities.ArchiveActivity;
import ch.unibe.ese.shopnote.activities.ItemListActivity;
import ch.unibe.ese.shopnote.activities.ManageFriendsActivity;
import ch.unibe.ese.shopnote.activities.ManageRecipeActivity;
import ch.unibe.ese.shopnote.activities.SettingsActivity;

/**
 * Creates a listener for the navigation drawer 
 * @author ESE Team 8
 *
 */

public class DrawerItemClickListener implements OnItemClickListener {
	private Activity activity;
 
	public DrawerItemClickListener(Activity activity, int id){
		this.activity = activity;
	}
	
	/**
	 * Selects the position of a drawer menu item based on the order of the strings
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long idNumber) {
		switch(position+1){
		case 1:
			// start ItemList activity
			Intent itemListIntent = new Intent(activity, ItemListActivity.class);
			activity.startActivity(itemListIntent);
			break;
		case 2:
			// start Recipe activity
			Intent recipeActivity = new Intent(activity, ManageRecipeActivity.class);
			activity.startActivity(recipeActivity);
			break;
		case 3:
			// start Friends activity
			Intent friendsIntent = new Intent(activity, ManageFriendsActivity.class);
			activity.startActivity(friendsIntent);
			break;
		case 4: 
			// start Archive activity
			Intent archiveIntent = new Intent(activity, ArchiveActivity.class);
			activity.startActivity(archiveIntent);
			break;
		case 5:
			// start Settings activity
			Intent optionsIntent = new Intent(activity, SettingsActivity.class);
			activity.startActivity(optionsIntent);
			break;	
		}
	}
}