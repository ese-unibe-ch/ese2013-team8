package ch.unibe.ese.sidelist;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import ch.unibe.ese.shoppinglist.R;

/**
 * Creates a slide bar for an Activity with all main navigation points
 * @author ESE Team 8
 *
 */
public class NavigationDrawer {

	private ListView menuListView;
	private ListView optionsListView;
	

	public DrawerLayout constructNavigationDrawer(DrawerLayout drawLayout, Activity activity) {
	
		//create drawer and get Strings
		drawLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		String navigation_items[] = activity.getResources().getStringArray(R.array.navigation_items);
		String navigation_options[]= activity.getResources().getStringArray(R.array.navigation_options);
		String navigation_section[]= activity.getResources().getStringArray(R.array.navigation_sections);

		//create Adapters for items and add them
		HeaderListAdapter itemsAdapter = new HeaderListAdapter(activity);
		ArrayAdapter<String> menuItemsAdapter = new ArrayAdapter<String>(activity, R.layout.drawer_menuitem, navigation_items);

		itemsAdapter.addSection(navigation_section[0], menuItemsAdapter);        

		menuListView = (ListView) activity.findViewById(R.id.left_drawer);
		menuListView.setAdapter(itemsAdapter);
		
		//create Adapters for options and add them
		HeaderListAdapter optionsAdapter = new HeaderListAdapter(activity);
		ArrayAdapter<String> optionItemsAdapter = new ArrayAdapter<String>(activity, R.layout.drawer_menuitem, navigation_options);
		
		optionsAdapter.addSection(navigation_section[1], optionItemsAdapter);
		
		optionsListView = (ListView)  activity.findViewById(R.id.left_drawer_options);
		optionsListView.setAdapter(optionsAdapter);

        
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
        
        //TODO: set listeners
        menuListView.setOnItemClickListener(new DrawerItemClickListener(activity, R.id.left_drawer));
        optionsListView.setOnItemClickListener(new DrawerItemClickListener(activity, R.id.left_drawer_options));
        
        return drawLayout;

	}
	

	
	
	private class HeaderListAdapter extends BaseAdapter
	{
		private final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
		private final ArrayAdapter<String> headers;
		
		
		public HeaderListAdapter(Context context){
			headers = new ArrayAdapter<String>(context, R.layout.drawer_header);
		}
		
		public void addSection(String section, Adapter adapter){
			this.headers.add(section);
			this.sections.put(section, adapter);
		}
		
		
		@Override
		public int getCount() {
		  int total = 0;
          for (Adapter adapter : this.sections.values())
              total += adapter.getCount() + 1;
          return total;
		}


		@Override
		public Object getItem(int position) {
		 for (Object section : this.sections.keySet()){
			 
	             Adapter adapter = sections.get(section);
	             int size = adapter.getCount() + 1;
	
	             // check if position inside this section
	             if (position == 0) return section;
	             if (position < size) return adapter.getItem(position - 1);
	
	             // otherwise jump into next section
	             position -= size;
	         }
		  return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 int sectionnum = 0;
             for (Object section : this.sections.keySet())
                 {
                     Adapter adapter = sections.get(section);
                     int size = adapter.getCount() + 1;

                     // check if position inside this section
                     if (position == 0) return headers.getView(sectionnum, convertView, parent);
                     if (position < size) return adapter.getView(position - 1, convertView, parent);

                     // otherwise jump into next section
                     position -= size;
                     sectionnum++;
                 }
             return null;
		}
				
	}
	
	
}