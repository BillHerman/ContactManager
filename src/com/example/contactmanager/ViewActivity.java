package com.example.contactmanager;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ViewActivity extends Activity {

	private static final String TAG = "log";
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	static ContactList contactList;
	ContactAdapter listAdapter;
	SQLiteDatabase db;
	Context context;

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
		listAdapter = new ContactAdapter(contacts, this, "");
		listView.setAdapter(listAdapter);

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> viewParent,
					View view, int position, long id) {

				contactList.DeleteContactById(contacts.get(position).getId());
				contacts = contactList.GetContactList();
				listAdapter = new ContactAdapter(contacts, context, "");
				listView.setAdapter(listAdapter);

				return false;
			}
		});

		EditText searchLastName = (EditText) findViewById(R.id.searchLastName);

		searchLastName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.v(TAG,"onTextChanged = " + s.toString());
				listAdapter = new ContactAdapter(contacts, context, s.toString());
				listView.setAdapter(listAdapter);

			
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				Log.v(TAG,"beforeTextChanged = " + s);
			}

			@Override
			public void afterTextChanged(Editable s) {
				Log.v(TAG,"afterTextChanged = " + s);
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
		Log.v(TAG, "begin1 itemid = " + item.getItemId());
		switch (item.getItemId()) {

		case R.id.addItem:
			Log.v(TAG, "begin2");
			startActivity(new Intent(this, AddActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
