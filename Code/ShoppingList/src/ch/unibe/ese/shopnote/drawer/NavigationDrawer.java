package ch.unibe.ese.shopnote.drawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.adapters.DrawerAdapter;

/**
 * Creates a slide bar for an Activity with all main navigation points
 */
public class NavigationDrawer {
	
	private ListView menuListView;
	
	public DrawerLayout constructNavigationDrawer(DrawerLayout drawLayout, Activity activity) {
	
		// Create drawer and get strings
		drawLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		String navigation_items[] = activity.getResources().getStringArray(R.array.navigation_items);

		// Create Adapter for items and add them
		DrawerAdapter itemsAdapter = new DrawerAdapter(activity, R.layout.drawer_menuitem, navigation_items);    

		menuListView = (ListView) activity.findViewById(R.id.left_drawer);
		menuListView.setAdapter(itemsAdapter);
        menuListView.setOnItemClickListener(new DrawerItemClickListener(activity, R.id.left_drawer));
        
        return drawLayout;
	}
}