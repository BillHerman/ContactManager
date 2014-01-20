package com.example.contactmanager;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* Description: Feeds listview with contact objects
 * Programmer: Bill Herman
 * Email: wrherman@comcast.net
 * Date: 12/20/2014
 */

public class ContactAdapter extends BaseAdapter {

	private static final String TAG = "log";
	private LayoutInflater layoutInflater;
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	Context context;

	public ContactAdapter(ArrayList<Contact> contacts, Context context,
			String selection) {

		if (selection.length() == 0) {
			this.contacts = contacts;

		} else {
			for (Contact contact : contacts) {
				if (contact.getLastName().startsWith(selection)) {
					this.contacts.add(contact);
				}
			}
		}

		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);

	}

	public int getCount() {
		return this.contacts.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		convertView = layoutInflater.inflate(R.layout.activity_view_row, null);

		convertView.setId(position);
		convertView.setTag(contacts.get(position).getId());
		
		holder = new ViewHolder();

		// set up fields

		holder.firstName = (TextView) convertView.findViewById(R.id.firstName);

		holder.icon = (ImageView) convertView.findViewById(R.id.icon);

		holder.lastName = (TextView) convertView.findViewById(R.id.lastName);

		holder.phoneNumber = (TextView) convertView
				.findViewById(R.id.phoneNumber);

		holder.weatherConstant = (TextView) convertView
				.findViewById(R.id.weatherConstant);

		holder.weatherStatus = (TextView) convertView
				.findViewById(R.id.weatherStatus);

		holder.weatherTemp = (TextView) convertView
				.findViewById(R.id.weatherTemp);

		holder.weatherConstant = (TextView) convertView
				.findViewById(R.id.weatherConstant);

		
		// update fields

		holder.firstName.setText(this.contacts.get(position).getFirstName());

		holder.icon.setBackgroundResource(R.drawable.personal);

		holder.lastName.setText(this.contacts.get(position).getLastName());
		holder.phoneNumber.setText(Long.toString(contacts.get(position)
				.getPhoneNumber()));
		
		
		String strAvatarFilename = "contactManager" + contacts.get(position).getId() + ".jpg";
		Log.v(TAG,"strAvatarFilename = " + strAvatarFilename);
		

		Uri imageUri = Uri.fromFile(new File(context.getFilesDir(),
				strAvatarFilename));

		holder.icon = (ImageView) convertView.findViewById(R.id.icon);
		holder.icon.setImageDrawable(context.getResources().getDrawable(
				R.drawable.visa));
		holder.icon.setImageURI(imageUri);
		
		
		WeatherHelper weatherHelper;
		weatherHelper = new WeatherHelper(holder.weatherStatus,	holder.weatherTemp, holder.weatherConstant);
		weatherHelper.execute(this.contacts.get(position).getZipCode());
		
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView lastName;
		TextView firstName;
		TextView phoneNumber;
		TextView weatherStatus;
		TextView weatherTemp;
		TextView weatherConstant;

	}

}
