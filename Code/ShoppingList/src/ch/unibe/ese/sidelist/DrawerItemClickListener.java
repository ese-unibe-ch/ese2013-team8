package ch.unibe.ese.sidelist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import ch.unibe.ese.activities.ArchiveActivity;
import ch.unibe.ese.activities.ItemListActivity;
import ch.unibe.ese.activities.ManageFriendsActivity;
import ch.unibe.ese.activities.OptionsActivity;
import ch.unibe.ese.shoppinglist.R;

/**
 * Creates a listener for the navigation drawer 
 * @author ESE Team 8
 *
 */

public class DrawerItemClickListener implements OnItemClickListener {
	private Activity activity;
	private int id;

    
	public DrawerItemClickListener(Activity activity, int id){
		this.activity = activity;
		this.id = id;
	}
	
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long idNumber) {
		if(this.id == R.id.left_drawer)  selectMenuItem(position);
		if(this.id == R.id.left_drawer_options)  selectOptionsItem(position);
    }

	private void selectMenuItem(int position) {
		switch(position){
			case 1: 
				Toast.makeText(activity, activity.getString(R.string.error_missing), Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(activity, activity.getString(R.string.error_missing), Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(activity, activity.getString(R.string.error_missing), Toast.LENGTH_SHORT).show();
				break;
		}	
	}

	private void selectOptionsItem(int position) {
		switch(position){
		case 1: 
			//start Archive
			Intent archiveIntent = new Intent(activity, ArchiveActivity.class);
			activity.startActivity(archiveIntent);
			break;
		case 2: 
			//start itemList
			Intent itemListIntent = new Intent(activity, ItemListActivity.class);
			activity.startActivity(itemListIntent);
			break;
		case 3:
			//start Friendscreen
			Intent friendsIntent = new Intent(activity, ManageFriendsActivity.class);
			activity.startActivity(friendsIntent);
			break;
		case 4:
			//start Options
			Intent optionsIntent = new Intent(activity, OptionsActivity.class);
			activity.startActivity(optionsIntent);
			break;
		
		}
		
	}
	
}