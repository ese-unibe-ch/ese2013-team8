package ch.unibe.ese.shopnote.adapters;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.activities.ItemListActivity;
import ch.unibe.ese.shopnote.activities.ViewRecipeActivity;
import ch.unibe.ese.shopnote.core.Item;

/**
 *	Modified ArrayAdapter to display the items including the name, quantity and price (if defined)
 */
public class ItemAdapter extends ArrayAdapter<Item> {

private Context context;
	
	public ItemAdapter(Context context, int resource, List<Item> itemList) {
		super(context, resource, itemList);
		this.context = context;
	}

	private class ItemHolder {
		CheckBox checkBox;
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
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_checkBox);
			holder.name = (TextView) convertView.findViewById(R.id.item_name);
			holder.quantity = (TextView) convertView.findViewById(R.id.item_quantity);
			holder.price = (TextView) convertView.findViewById(R.id.item_price);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}
		
		// tick checkbox
		if ((context.getClass() == ItemListActivity.class) || (context.getClass() == ViewRecipeActivity.class))
			holder.checkBox.setVisibility(View.GONE);
		else {
			holder.checkBox.setChecked(item.isBought());
			holder.checkBox.setEnabled(false);
		}
		
		// set name
		holder.name.setText(item.getName());
		
		// set quantity
		if (item.getQuantity() != null) {
			
			String quantity = item.getQuantity().toString();
			
			int ordinal = item.getUnit().ordinal();
			Resources res = convertView.getResources();
			String localizedUnit = res.getStringArray(R.array.item_units_short)[ordinal];

			StringBuilder sb = new StringBuilder(quantity).append(" ").append(localizedUnit);
			holder.quantity.setText(sb.toString());
		}
		else
			holder.quantity.setText("");
		
		// set price
		Currency currency = Currency.getInstance(Locale.getDefault());
		if (item.getPrice() != null) {
			String price = item.getPrice().toString();
			holder.price.setText(price + " " + currency);
		}
		else
			holder.price.setText("");
		return convertView;
	}
}