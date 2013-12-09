package ch.unibe.ese.shopnote.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.activities.ArchiveActivity;
import ch.unibe.ese.shopnote.activities.HomeActivity;
import ch.unibe.ese.shopnote.activities.ItemListActivity;
import ch.unibe.ese.shopnote.activities.ManageFriendsActivity;
import ch.unibe.ese.shopnote.activities.ManageRecipeActivity;
import ch.unibe.ese.shopnote.activities.SettingsActivity;
import ch.unibe.ese.shopnote.core.BaseActivity;

/**
 *	Modified ArrayAdapter to display the drawer menu items including their icons
 */
public class DrawerAdapter extends ArrayAdapter<String> {
	
	private BaseActivity context;
	private String[] menuItems;

	public DrawerAdapter(BaseActivity context, int resource, String[] menuItems) {
		super(context, resource, menuItems);
		this.context = context;
		this.menuItems = menuItems;
	}
	
	private class MenuItemHolder {
		ImageView icon;
		TextView menuItem;
		

	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuItemHolder holder;
		String item = menuItems[position];
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.drawer_menuitem, null);
			holder = new MenuItemHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.drawer_menuItem_icon);
			holder.menuItem = (TextView) convertView.findViewById(R.id.drawer_menuItem_name);
			convertView.setTag(holder);
		} else {
			holder = (MenuItemHolder) convertView.getTag();
		}
		
		switch(position+1){
		case 1:
			// set Home icon
			holder.icon.setImageResource(R.drawable.ic_action_home);
			// set current activity name bold
			if (context.getClass() == HomeActivity.class)
				holder.menuItem.setTypeface(null, Typeface.BOLD);
			break;
		case 2:
			// set ItemList icon
			holder.icon.setImageResource(R.drawable.ic_action_list);
			// set current activity name bold
			if (context.getClass() == ItemListActivity.class)
				holder.menuItem.setTypeface(null, Typeface.BOLD);
			break;
		case 3:
			// set Recipe icon
			holder.icon.setImageResource(R.drawable.ic_action_favorite);
			// set current activity name bold
			if (context.getClass() == ManageRecipeActivity.class)
				holder.menuItem.setTypeface(null, Typeface.BOLD);
			break;
		case 4:
			// set Friends icon
			holder.icon.setImageResource(R.drawable.ic_action_friends);
			// set current activity name bold
			if (context.getClass() == ManageFriendsActivity.class)
				holder.menuItem.setTypeface(null, Typeface.BOLD);
			break;
		case 5: 
			// set Archive icon
			holder.icon.setImageResource(R.drawable.ic_action_archive);
			// set current activity name bold
			if (context.getClass() == ArchiveActivity.class)
				holder.menuItem.setTypeface(null, Typeface.BOLD);
			break;
		case 6:
			// set Settings icon
			holder.icon.setImageResource(R.drawable.ic_action_settings);
			// set current activity name bold
//			if (context.getClass() == SettingsActivity.class)
//				holder.menuItem.setTypeface(null, Typeface.BOLD);
			break;	
		}
	
		// set name
		holder.menuItem.setText(item);
		
		// set color
		context.updateTextColor(holder.menuItem);
		
		return convertView;
	}
}