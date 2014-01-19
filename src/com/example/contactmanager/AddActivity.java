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
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

public class AddActivity extends Activity {

	protected static final String TAG = "log";
	static final int TAKE_CAMERA_REQUEST = 1;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		context = getApplicationContext();

		// Gender selection
		String[] genderArray = getResources().getStringArray(
				R.array.genderArray);
		Spinner gender_spinner = (Spinner) findViewById(R.id.genderValue);
		ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(this,
				R.layout.row, R.id.entry, genderArray);
		gender_spinner.setAdapter(gender_adapter);

		// State selection
		String[] stateArray = getResources().getStringArray(R.array.stateArray);
		Spinner state_spinner = (Spinner) findViewById(R.id.stateValue);
		ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this,
				R.layout.row, R.id.entry, stateArray);
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

				Intent pictureIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(pictureIntent, TAKE_CAMERA_REQUEST);

				break;

			case R.id.saveButton:

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
				contact.setAge(StringToInteger(age.getText().toString()));
				contact.setAddress(address.getText().toString());
				contact.setCity(city.getText().toString());
				contact.setState(state.getSelectedItem().toString());
				contact.setZipCode(StringToInteger(zipCode.getText().toString()));
				contact.setPhoneNumber(StringToLong(phone.getText().toString()));

				boolean errorFound = false;

				if (!contact.isValidFirstName())
					errorFound = SendError(errorFound,
							"First name can not be blank.");
				if (!contact.isValidLastName())
					errorFound = SendError(errorFound,
							"Last name can not be blank.");
				if (!contact.isValidGender())
					errorFound = SendError(errorFound,
							"Gender must be Male, Female, or Unknown.");
				if (!contact.isValidAge())
					errorFound = SendError(errorFound,
							"Age must be between 0 and 100.");
				if (!contact.isValidAddress())
					errorFound = SendError(errorFound,
							"Address can not be blank.");
				if (!contact.isValidCity())
					errorFound = SendError(errorFound, "City can not be blank.");
				if (!contact.isValidState())
					errorFound = SendError(errorFound, "State is not valid.");
				if (!contact.isValidZipCode())
					errorFound = SendError(errorFound,
							"Zip Code must be five digits long.");
				if (!contact.isValidPhoneNumber())
					errorFound = SendError(errorFound,
							"Phone number must be ten digits long.");

				if (!errorFound) {
					ViewActivity.contactList.AddContact(contact);
					finish();
				}

				break;

			case R.id.cancelButton:
				finish();
				break;

			}
		}
	};

	private boolean SendError(boolean errorFound, String message) {

		if (!errorFound) {
		
			AlertDialog.Builder builder = new AlertDialog.Builder(
					AddActivity.this);
			builder.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Okay",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			errorFound = true;
		}

		return errorFound;

	}

	private Integer StringToInteger(String string) {
		Integer value = -1;
		try {
			value = Integer.parseInt(string);
		} catch (Exception e) {
			value = -1;
		}
		return value;
	}

	private Long StringToLong(String string) {
		Long value = -1L;
		try {
			value = Long.parseLong(string);
		} catch (Exception e) {
			value = -1L;
		}
		return value;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_CAMERA_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Bitmap cameraPic = (Bitmap) data.getExtras().get("data");
				SaveAvatar(cameraPic);
			}
			break;
		}
	}

	private void SaveAvatar(Bitmap avatar) {
		String strAvatarFilename = "avatar.jpg";
		try {
			OutputStream outputStream = openFileOutput(strAvatarFilename,
					MODE_PRIVATE);
			avatar.compress(CompressFormat.JPEG, 100, outputStream);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri imageUri = Uri.fromFile(new File(getFilesDir(), strAvatarFilename));

		ImageView addImage = (ImageView) findViewById(R.id.addImage);
		addImage.setImageDrawable(getResources().getDrawable(R.drawable.camera));
		addImage.setImageURI(imageUri);
	
	}

}
