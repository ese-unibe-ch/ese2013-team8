package ch.unibe.ese.shopnote.drawer;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.activities.ArchiveActivity;
import ch.unibe.ese.shopnote.activities.HomeActivity;
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
		// TODO: switch to activity if already open instead of creating as new one
		switch(position+1){
			case 1:
				// start Home activity
				if (activity.getClass() != HomeActivity.class) {
					Intent homeIntent = new Intent(activity, HomeActivity.class);
					activity.startActivity(homeIntent);
				}
				break;
			case 2:
				// start ItemList activity
				if (activity.getClass() != ItemListActivity.class) {
					Intent itemListIntent = new Intent(activity, ItemListActivity.class);
					activity.startActivity(itemListIntent);
				}
				break;
			case 3:
				// start Recipe activity
				if (activity.getClass() != ManageRecipeActivity.class) {
					Intent recipeActivity = new Intent(activity, ManageRecipeActivity.class);
					activity.startActivity(recipeActivity);
				}
				break;
			case 4:
				// start Friends activity
				if (activity.getClass() != ManageFriendsActivity.class) {
					Intent friendsIntent = new Intent(activity, ManageFriendsActivity.class);
					activity.startActivity(friendsIntent);
				}
				break;
			case 5: 
				// start Archive activity
				if (activity.getClass() != ArchiveActivity.class) {
					Intent archiveIntent = new Intent(activity, ArchiveActivity.class);
					activity.startActivity(archiveIntent);
				}
				break;
			case 6:
				// start Settings activity
				if (activity.getClass() != SettingsActivity.class) {
					Intent optionsIntent = new Intent(activity, SettingsActivity.class);
					activity.startActivity(optionsIntent);
				}
				break;
		}
		
		// close drawer
		DrawerLayout drawLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		drawLayout.closeDrawers();
	}
}