package ch.unibe.ese.shopnote.adapters;

import java.util.List;

import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.core.Friend;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Provides the layout for the friendslist
 */
public class FriendsListAdapter extends ArrayAdapter<Friend> {

	private Context context;
	
	public FriendsListAdapter(Context context, int resource, List<Friend> friendsList) {
		super(context, resource, friendsList);
		this.context = context;
	}

	private class FriendHolder {
		TextView name;
		TextView phonenumber;
		ImageView hasTheAppIcon;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendHolder holder;
		Friend friend = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.friend_list_item, null);
			holder = new FriendHolder();
			holder.name = (TextView) convertView.findViewById(R.id.friend_list_item_name);
			holder.phonenumber = (TextView) convertView.findViewById(R.id.friend_list_phonenumber);
			holder.hasTheAppIcon = (ImageView) convertView.findViewById(R.id.friend_list_item_hasTheAppIcon);
			convertView.setTag(holder);
		} else {
			holder = (FriendHolder) convertView.getTag();
		}
		
		// set name
		holder.name.setText(friend.getName());
		
		// set phone number
		String phoneNumber = friend.getPhoneNr();
		if (phoneNumber.startsWith("41")) {
			phoneNumber = phoneNumber.substring(2, phoneNumber.length());
			phoneNumber = "0" + phoneNumber;
		}
		holder.phonenumber.setText(phoneNumber);
		
		// set app status
		if(friend.hasTheApp()) {
			holder.hasTheAppIcon.setImageResource(R.drawable.ic_action_ok);
		} else {
			holder.hasTheAppIcon.setImageResource(R.drawable.ic_action_abort);
		}
		return convertView;
	}
	
}
