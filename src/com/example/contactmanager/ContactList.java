package com.example.contactmanager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class ContactList {

	private static final String TAG = "log";
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	SQLiteDatabase db;
	private int maxId = -1;

	public ContactList(SQLiteDatabase db) {
		this.db = db;
	}

	public int getNextId() {
		return maxId;
	}

	public void LoadContactList() {
		Cursor c;
		contacts = new ArrayList<Contact>();
		Contact contact;
		maxId = -1;

		try {
			c = db.rawQuery("select * from contacts;", null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						Log.v(TAG, "selecting id= " + c.getColumnIndex("id"));
						contact = new Contact();
						contact.setId(c.getInt(c.getColumnIndex("id")));
						contact.setFirstName(c.getString(c
								.getColumnIndex("firstName")));
						contact.setLastName(c.getString(c
								.getColumnIndex("lastName")));
						contact.setAddress(c.getString(c
								.getColumnIndex("address")));
						contact.setAge(c.getInt(c.getColumnIndex("age")));
						contact.setCity(c.getString(c.getColumnIndex("city")));
						contact.setGender(c.getString(c
								.getColumnIndex("gender")));
						contact.setPhoneNumber(c.getLong(c
								.getColumnIndex("phoneNumber")));
						contact.setState(c.getString(c.getColumnIndex("state")));
						contact.setZipCode(c.getInt(c.getColumnIndex("zipCode")));
						contacts.add(contact);
						if (contact.getId() > maxId) {
							maxId = contact.getId();
						}
						Log.v(TAG, "setting maxid after check = " + maxId);
					} while (c.moveToNext());
				}
				c.close();
			}
		} catch (Exception e) {
			Log.v(TAG, "error in ContactList = " + e.getMessage());
		}

		Log.v(TAG, "setting maxid after ++ = " + maxId);

	}

	public void PushContact(Contact contact, Bitmap avatar, Context context) {

		ContentValues values = new ContentValues();

		try {

			db.beginTransaction();

			try {
				Cursor c = db.rawQuery("select * from contacts;", null);
			}

			catch (Exception e) {
				db.execSQL("create table contacts (id int, firstName text, lastName text, address text, age int, city text, gender text, phoneNumber int, state text, zipCode int)");
			}

			values.clear();
			maxId++;
			contact.setId(maxId);
			Log.v(TAG, "adding new id = " + maxId);
			values.put("id", contact.getId());
			values.put("firstName", contact.getFirstName());
			values.put("lastName", contact.getLastName());
			values.put("address", contact.getAddress());
			values.put("age", contact.getAge());
			values.put("city", contact.getCity());
			values.put("gender", contact.getGender());
			values.put("phoneNumber", contact.getPhoneNumber());
			values.put("state", contact.getState());
			values.put("zipCode", contact.getZipCode());

			db.insertOrThrow("contacts", null, values);

			db.setTransactionSuccessful();
		} catch (Exception e) {
			maxId--;
		} finally {
			db.endTransaction();
		}

		if (avatar != null) {
			String strAvatarFilename = "contactManager"
					+ ViewActivity.contactList.getNextId() + ".jpg";
			try {
				OutputStream outputStream = context.openFileOutput(
						strAvatarFilename, Context.MODE_PRIVATE);
				avatar.compress(CompressFormat.JPEG, 100, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void DeleteContactById(int id) {

		try {
			Cursor d = db.rawQuery("select * from contacts;", null);
			if (d != null) {
				if (d.moveToFirst()) {
					do {
						Log.v(TAG,
								"index to delete = " + d.getColumnIndex("id"));
					} while (d.moveToNext());
				}
				d.close();
			}
		} catch (Exception e) {
			Log.v(TAG, "error in ContactList = " + e.getMessage());
		}

		Cursor c;

		try {
			String selectString = "delete from contacts where id = " + id + ";";
			c = db.rawQuery(selectString, null);
			int count = c.getCount();
		} catch (Exception e) {
			Log.v(TAG, "error in delete ContactList = " + e.getMessage());
		}

		Cursor f = db.rawQuery("select * from contacts;", null);
		Log.v(TAG, "count after delete = " + f.getCount());

		LoadContactList();
	}

	public ArrayList<Contact> GetContactList() {

		if (contacts.size() == 0) {
			LoadContactList();

		}
		return contacts;
	}

	public void AddContact(Contact contact, Bitmap bitmap, Context context) {
		contacts.add(contact);

		PushContact(contact, bitmap, context);

	}

}
