package ch.unibe.ese.shopnote.adapters;

import java.util.ArrayList;
import java.util.List;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.core.Recipe;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *	Modified ArrayAdapter to display the shopping lists including the (bought) item count
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {
	
	private List<Recipe> recipes;
	private Context context;

	public RecipeAdapter(Context context, int resource, List<Recipe> recipes) {
		super(context, resource, recipes);
		this.context = context;
		this.recipes = recipes;
	}
	
	private class ListHolder {
		TextView name;
		ImageView isShared;
		TextView notification;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ListHolder holder;
		Recipe recipe = recipes.get(position);
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.recipe_item, null);
			holder = new ListHolder();
			holder.name = (TextView) convertView.findViewById(R.id.recipe_name);
			holder.isShared = (ImageView) convertView.findViewById(R.id.recipe_isShared);
			holder.notification = (TextView) convertView.findViewById(R.id.recipe_notification);
			convertView.setTag(holder);
		} else {
			holder = (ListHolder) convertView.getTag();
		}
		
		// set name
		holder.name.setText(recipe.toString());
		
		// set shared icon
		if (recipe.isShared()) {
			holder.isShared.setVisibility(View.VISIBLE);
			holder.isShared.setImageResource(R.drawable.ic_action_share);
		}
		else
			holder.isShared.setVisibility(View.GONE);	
		
		// set notification count
		// TODO: Add recipe changes count
		
		/*if (recipe.getChangesCount() != 0) {
			holder.notification.setText("" + recipe.getChangesCount());
			holder.notification.setVisibility(View.VISIBLE);
		}
		else
			holder.notification.setVisibility(View.GONE);*/
		
		return convertView;
	}
}