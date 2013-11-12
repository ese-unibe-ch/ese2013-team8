package ch.unibe.ese.shopnote.adapters;

import ch.unibe.ese.shopnote.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<String> {
	
	private Context context;
	private String[] menuItems;

	public DrawerAdapter(Context context, int resource, String[] menuItems) {
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
			// set ItemList icon
			holder.icon.setImageResource(R.drawable.ic_action_list);
			break;
		case 2:
			// set Recipe icon
			holder.icon.setImageResource(R.drawable.ic_action_favorite);
			break;
		case 3:
			// set Friends icon
			holder.icon.setImageResource(R.drawable.ic_action_friends);
			break;
		case 4: 
			// set Archive icon
			holder.icon.setImageResource(R.drawable.ic_action_archive);
			break;
		case 5:
			// set Settings icon
			holder.icon.setImageResource(R.drawable.ic_action_settings);
			break;	
		}
	
		// set name
		holder.menuItem.setText(item);
		
		return convertView;
	}
	


}
