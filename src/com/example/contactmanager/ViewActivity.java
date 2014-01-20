package com.example.contactmanager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ViewActivity extends Activity {

	ArrayList<Contact> contacts = new ArrayList<Contact>();
	static ContactList contactList;
	ContactAdapter listAdapter;
	SQLiteDatabase db;
	Context context;
	String searchString = "";
	String strAvatarFilename = "avatar.jpg";
	
	@Override
	protected void onResume() {
		super.onResume();
		listAdapter.notifyDataSetChanged();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		context = this;
		
		db = openOrCreateDatabase("contactdata.db", MODE_PRIVATE, null);

		contactList = new ContactList(db);
		contacts = contactList.GetContactList();

		final ListView listView = (ListView) findViewById(R.id.list);
		searchString = "";
		listAdapter = new ContactAdapter(contacts, this, searchString);
		listView.setAdapter(listAdapter);

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> viewParent,
					View view, int position, long id) {
				contactList.DeleteContactById((Integer)view.getTag());
				contacts = contactList.GetContactList();
				listAdapter = new ContactAdapter(contacts, context, searchString);
				listView.setAdapter(listAdapter);

				return false;
			}
		});

		EditText searchLastName = (EditText) findViewById(R.id.searchLastName);

		searchLastName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				searchString = s.toString();
				listAdapter = new ContactAdapter(contacts, context, searchString);
				listView.setAdapter(listAdapter);

			
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.addItem:
			
			
			Bitmap avatar = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.personal);
			
			try {
				OutputStream outputStream = openFileOutput(
						strAvatarFilename, MODE_PRIVATE);
				avatar.compress(CompressFormat.JPEG, 100, outputStream);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			startActivity(new Intent(this, AddActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
