package ch.unibe.ese.sidelist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import ch.unibe.ese.activities.ManageFriendsActivity;
import ch.unibe.ese.shoppinglist.R;



public class DrawerItemClickListener implements OnItemClickListener {
	private Activity activity;
	private int id;

    
	public DrawerItemClickListener(Activity activity, int id){
		this.activity = activity;
		this.id = id;
	}
	
	
	public void onItemClick(AdapterView parent, View view, int position, long idNumber) {
		if(this.id == R.id.left_drawer)  selectMenuItem(position);
		if(this.id == R.id.left_drawer_options)  selectOptionsItem(position);
    }

	private void selectMenuItem(int position) {
		switch(position){
			case 1: 
				break;
			case 2: 
				break;
			case 3:
				break;
		}	
	}

	private void selectOptionsItem(int position) {
		switch(position){
		case 1: 
			break;
		case 2: 
			break;
		case 3:
			//start Friendscreen
			Intent friendsIntent = new Intent(activity, ManageFriendsActivity.class);
			activity.startActivity(friendsIntent);
			break;
		case 4:
			//start Options
			Intent optionsIntent = new Intent(activity, ch.unibe.ese.activities.OptionsActivity.class);
			activity.startActivity(optionsIntent);
			break;
		
		}
		
	}
	
}