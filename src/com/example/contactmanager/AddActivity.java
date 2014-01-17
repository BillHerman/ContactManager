package com.example.contactmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddActivity extends Activity {

	protected static final String TAG = "log";
	static final int TAKE_CAMERA_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		// Gender selection
		String[] genderArray =  getResources().getStringArray(R.array.genderArray);    
		Spinner gender_spinner = (Spinner) findViewById(R.id.genderValue);
		ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(this,R.layout.row, R.id.entry, genderArray);
		gender_spinner.setAdapter(gender_adapter);
	    
		// State selection
		String[] stateArray =  getResources().getStringArray(R.array.stateArray);    
		Spinner state_spinner = (Spinner) findViewById(R.id.stateValue);
		ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this,R.layout.row, R.id.entry, stateArray);
		state_spinner.setAdapter(state_adapter);
			    
	    
	    

		
		
		
		// add camera picture
		findViewById(R.id.addButton).setOnClickListener(onClickListener);
		
		// save or exit
		findViewById(R.id.saveButton).setOnClickListener(onClickListener);
		findViewById(R.id.cancelButton).setOnClickListener(onClickListener);
			
	}



	final OnClickListener onClickListener = new OnClickListener() {
		public void onClick(final View v) {
			switch (v.getId()) {

			case R.id.addButton:
				Log.v(TAG,"short addButton pressed");
	
				Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(pictureIntent, TAKE_CAMERA_REQUEST);
				
				break;

			case R.id.saveButton:
				Log.v(TAG,"saveButton pressed");
		
				EditText firstName = (EditText) findViewById(R.id.firstNameValue);
				EditText lastName = (EditText) findViewById(R.id.lastNameValue);
				Spinner gender = (Spinner) findViewById(R.id.genderValue);
				EditText age = (EditText) findViewById(R.id.ageValue);
				EditText address = (EditText) findViewById(R.id.homeAddressValue);
				EditText city = (EditText) findViewById(R.id.cityNameValue);
				Spinner state = (Spinner) findViewById(R.id.stateValue);
				EditText zipCode = (EditText) findViewById(R.id.zipCodeValue);
				EditText phone = (EditText) findViewById(R.id.phoneNumberValue);
				
				Contact contact = new Contact();
				contact.setFirstName(firstName.getText().toString());
				contact.setLastName(lastName.getText().toString());
				contact.setGender(gender.getSelectedItem().toString());
				contact.setAge(Integer.parseInt(age.getText().toString()));
				contact.setAddress(address.getText().toString());
				contact.setCity(city.getText().toString());
				contact.setState(state.getSelectedItem().toString());
				contact.setZipCode(Integer.parseInt(zipCode.getText().toString()));
				contact.setPhoneNumber(Long.parseLong(phone.getText().toString()));
				ViewActivity.contactList.AddContact(contact);
				
				finish();
				
				break;

			case R.id.cancelButton:
				finish();
				break;

			}
		}
	};
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode) {
		case TAKE_CAMERA_REQUEST:
			if (resultCode == Activity.RESULT_OK)
			{
				Bitmap cameraPic = (Bitmap) data.getExtras().get("data");
				Log.v(TAG,"camera result okay");
				SaveAvatar(cameraPic);
			}
		break;	
		}
	}
	
	private void SaveAvatar(Bitmap avatar)
	{
	Log.v(TAG,"running SAVE AVATAR");	
	String strAvatarFilename = "avatar.jpg";
	try {
		OutputStream outputStream = openFileOutput(strAvatarFilename, MODE_PRIVATE);
		avatar.compress(CompressFormat.JPEG, 100, outputStream);
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
			Log.v(TAG,"i/o exception");
			e.printStackTrace();
	}
	Uri imageUri = Uri.fromFile(new File(getFilesDir(), strAvatarFilename));
	 
	ImageView addImage = (ImageView) findViewById(R.id.addImage);
	addImage.setImageDrawable(getResources().getDrawable(R.drawable.camera));
	addImage.setImageURI(imageUri);
	Log.v(TAG,"finishedSAVE AVATAR");	
	
	}
	
	/*

	public void Pull(String url) {

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpResponse response;

		try {
			HttpGet get = new HttpGet(url); 
			response = client.execute(get);

			
			if (response != null) {
	
				HttpEntity entity = response.getEntity();
				String responseText = EntityUtils.toString(entity);
				
				JSONObject json = new JSONObject();
				JSONArray ja = new JSONArray(responseText);
				
				

					

					
					for (int i = 0; i < ja.length(); i++) {
						json = ja.getJSONObject(i);
						values.clear();
						String oid = json.get("oid").toString();
						values.put("cid", json.get("cid").toString());
						values.put("date", json.get("date").toString());
						values.put("oid", oid);
						db.insertOrThrow("orderHeader", null, values); 
						JSONArray jaProduct = new JSONArray(json.get("products").toString());
						for (int j = 0; j < jaProduct.length(); j++) {
							json = jaProduct.getJSONObject(j);
							values.clear();
							values.put("oid", oid);
							values.put("upc", json.get("upc").toString());
							values.put("quantity", json.get("quantity").toString());
							values.put("detail", json.get("detail").toString());
							values.put("leadTime", json.get("leadTime").toString());
							values.put("location", json.get("location").toString());
							values.put("name", json.get("name").toString());
							values.put("owner", json.get("owner").toString());
							JSONObject jsonSupplier = new JSONObject(json.get("supplier").toString());
							values.put("supplierId", jsonSupplier.get("id").toString());
							values.put("supplierAddress", jsonSupplier.get("address").toString());
							values.put("supplierBusiness", jsonSupplier.get("business").toString());
							values.put("supplierName", jsonSupplier.get("name").toString());
							values.put("supplierPrice", jsonSupplier.get("price").toString());
							db.insertOrThrow("orderDetail", null, values);
						}
					}
					db.setTransactionSuccessful();
				} catch (Exception e) {
					Log.v("log", "Exception: OrderList: Pull(): values = "
							+ e.toString());
				}finally {
					db.endTransaction();
				}

			}
		} catch (Exception e) {
			Log.v("log",
					"Exception: orderList: Pull(): insert = "
							+ e.toString());
		} 
		
	}

	
	*/
	
}
