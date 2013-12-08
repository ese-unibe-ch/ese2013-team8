package ch.unibe.ese.shopnote.adapters;

import java.util.List;

import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 *	Modified ArrayAdapter to display the items including the name, quantity and price (if defined)
 */
public class ItemListAdapter extends ArrayAdapter<Item> {

private Context context;
	
	public ItemListAdapter(Context context, int resource, List<Item> itemList) {
		super(context, resource, itemList);
		this.context = context;
	}

	private class ItemHolder {
		TextView name;
		TextView quantity;
		TextView price;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemHolder holder;
		Item item = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.shopping_list_item_view, null);
			holder = new ItemHolder();
			holder.name = (TextView) convertView.findViewById(R.id.item_name);
			holder.quantity = (TextView) convertView.findViewById(R.id.item_quantity);
			holder.price = (TextView) convertView.findViewById(R.id.item_price);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		holder.name.setText(item.getName());
		if (item.getQuantity() != null) {
			String quantity = item.getQuantity().toString();
			int ordinal = item.getUnit().ordinal();
			Resources res = convertView.getResources();
			String localizedUnit = res.getStringArray(R.array.item_units)[ordinal];

			StringBuilder sb = new StringBuilder(quantity).append(" ").append(localizedUnit);
			holder.quantity.setText(sb.toString());
		}
		else
			holder.quantity.setText("");
		if (item.getPrice() != null) {
			// TODO: get user currency
			String price = item.getPrice().toString();
			holder.price.setText(price + " CHF");
		}
		else
			holder.price.setText("");
		return convertView;
	}
}