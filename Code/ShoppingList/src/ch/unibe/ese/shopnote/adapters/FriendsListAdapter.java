package ch.unibe.ese.shopnote.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.core.Friend;

/**
 * Provides the layout for the friendslist
 */
public class FriendsListAdapter extends ArrayAdapter<Friend> {

	private BaseActivity context;
	
	public FriendsListAdapter(BaseActivity context, int resource, List<Friend> friendsList) {
		super(context, resource, friendsList);
		this.context = context;
	}

	private class FriendHolder {
		TextView name;
		TextView phonenumber;
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
		else if (phoneNumber.startsWith("+41")) {
			phoneNumber = phoneNumber.substring(3, phoneNumber.length());
			phoneNumber = "0" + phoneNumber;
		}
		holder.phonenumber.setText(phoneNumber);
		
		//updateTextColor
		context.updateTextColor(holder.name);
		context.updateTextColorSecond(holder.phonenumber);
		
		return convertView;
	}
	
}
