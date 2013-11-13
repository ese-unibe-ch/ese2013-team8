package ch.unibe.ese.shopnote.adapters;

import java.util.ArrayList;
import java.util.List;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.core.ShoppingList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * LayoutAdapter for Shopping lists in {@link HomeActivity}
 *
 */

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {
	
	private List<ShoppingList> shoppingLists;
	private ArrayList<Integer> boughtItemCount = new ArrayList<Integer>();
	private ArrayList<Integer> totalItemCount = new ArrayList<Integer>();
	private Context context;

	public ShoppingListAdapter(Context context, int resource, List<ShoppingList> shoppingLists) {
		super(context, resource, shoppingLists);
		this.context = context;
		this.shoppingLists = shoppingLists;
	}
	
	private class ListHolder {
		TextView name;
		TextView count;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ListHolder holder;
		ShoppingList list = shoppingLists.get(position);
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.shopping_list_item_count, null);
			holder = new ListHolder();
			holder.name = (TextView) convertView.findViewById(R.id.shopping_list_name);
			holder.count = (TextView) convertView.findViewById(R.id.shopping_list_count);
			convertView.setTag(holder);
		} else {
			holder = (ListHolder) convertView.getTag();
		}
		
		// set name
		holder.name.setText(list.toString());

		//set count
		if (totalItemCount.get(position) != 0)
			holder.count.setText(
					boughtItemCount.get(position) + " / " + 
					totalItemCount.get(position));
		
		return convertView;
	}
	
	public void setCount (int boughtItems, int totalItems) {
		boughtItemCount.add(boughtItems);
		totalItemCount.add(totalItems);
	}
}