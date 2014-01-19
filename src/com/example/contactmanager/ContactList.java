package com.example.contactmanager;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class ContactList {

	private static final String TAG = "log";
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	SQLiteDatabase db;
	int maxId = 0;
	
	public ContactList(SQLiteDatabase db)
	{
		this.db = db;
	}
	public void LoadContactList() {
		Cursor c;
		contacts = new ArrayList<Contact>();
		Contact contact;
		
		try {
			c = db.rawQuery("select * from contacts;", null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						contact = new Contact();
						contact.setId(c.getInt(c.getColumnIndex("id")));
						contact.setFirstName(c.getString(c.getColumnIndex("firstName")));
						contact.setLastName(c.getString(c.getColumnIndex("lastName")));
						contact.setAddress(c.getString(c.getColumnIndex("address")));
						contact.setAge(c.getInt(c.getColumnIndex("age")));
						contact.setCity(c.getString(c.getColumnIndex("city")));
						contact.setGender(c.getString(c.getColumnIndex("gender")));
						contact.setPhoneNumber(c.getLong(c.getColumnIndex("phoneNumber")));
						contact.setState(c.getString(c.getColumnIndex("state")));
						contact.setZipCode(c.getInt(c.getColumnIndex("zipCode")));
						contacts.add(contact);
						if (contact.getId() > maxId)
						{
							maxId = contact.getId();
						}
					} while (c.moveToNext());
				}
				c.close();
			}
		}
		catch (Exception e) {
			Log.v(TAG,"error in ContactList = " + e.getMessage());
		}


	}
	
	
	
	
	
	
	
	

	public void PushContact(Contact contact) {

		ContentValues values = new ContentValues();

		try {

		//	db.beginTransaction();

			try {
				Cursor c = db.rawQuery("select * from contacts;", null);
			}

			catch (Exception e) {
				db.execSQL("create table contacts (id int, firstName text, lastName text, address text, age int, city text, gender text, phoneNumber int, state text, zipCode int)");
			}

			values.clear();
			maxId++;
			contact.setId(maxId);
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

	//		db.setTransactionSuccessful();
		} catch (Exception e) {
			
		} finally {
	//		db.endTransaction();
		}

	}
	
	
	public void DeleteContactById(int id)
	{
		
		Cursor c;
		try {
			String selectString = "delete from contacts where id = " + id + ";";
			c = db.rawQuery(selectString, null);
			int count = c.getCount();
		}
		catch (Exception e) {
			Log.v(TAG,"error in delete ContactList = " + e.getMessage());
		}
		
		LoadContactList();
	}
	
	
	public ArrayList<Contact> GetContactList() {
	
		if (contacts.size()==0)
		{
			LoadContactList();
			
		}
		return contacts;
	}
	
	public void AddContact(Contact contact)
	{
		contacts.add(contact);

		PushContact(contact);
		
	}

	
	
	
	
}
