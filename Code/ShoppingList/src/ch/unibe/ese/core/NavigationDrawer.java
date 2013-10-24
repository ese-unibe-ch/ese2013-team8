package ch.unibe.ese.core;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.ese.shoppinglist.R;
import ch.unibe.ese.shoppinglist.R.array;
import ch.unibe.ese.shoppinglist.R.drawable;
import ch.unibe.ese.shoppinglist.R.id;
import ch.unibe.ese.shoppinglist.R.layout;
import ch.unibe.ese.shoppinglist.R.string;

public class NavigationDrawer {


	public static DrawerLayout constructNavigationDrawer(DrawerLayout drawLayout, Activity activity) {
	
		
		drawLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		String navigation_items[] = activity.getResources().getStringArray(R.array.navigation_items);
		//String navigation_options[]= getResources().getStringArray(R.array.navigation_options);

		ListView mDrawerList =(ListView) activity.findViewById(R.id.left_drawer);
	    mDrawerList.setAdapter(new ArrayAdapter<String>(activity, R.layout.shopping_list_item, navigation_items));
	    
//	    ViewGroup header_navigation = (ViewGroup) getLayoutInflater().inflate(R.layout.navigation_drawer_bar, mDrawerList, false);
//	    mDrawerList.addHeaderView(header_navigation, null, false);
	    
//	    mDrawerOptions = (ListView) findViewById(R.id.left_drawer_options);
//	    mDrawerOptions.setAdapter(new ArrayAdapter<String>(this, R.layout.shopping_list_item, navigation_options));


        
        ActionBarDrawerToggle actbardrawertoggle= new ActionBarDrawerToggle(activity, drawLayout, R.drawable.ic_action_edit ,R.string.drawer_open, R.string.drawer_close)
        {
        	public void onDrawerClosed(View view) {
        		super.onDrawerClosed(view);
        	}
        	
        	public void onDrawerOpened(View drawerView){
        		super.onDrawerOpened(drawerView);
        	}
        	
        };
        
        drawLayout.setDrawerListener(actbardrawertoggle);
        
        return drawLayout;

	}
	
	
}