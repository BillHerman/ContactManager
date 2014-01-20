package com.example.contactmanager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log; 
import android.view.View;
import android.widget.TextView;

	public class WeatherHelper extends
			AsyncTask<Integer, Void, Integer> {
		TextView textView1;
		TextView textView2;
		TextView textView3;
		Integer tempature = -999;
		String weatherText = "";
		Integer zipCode = 0;
		
		public WeatherHelper(TextView textView1, TextView textView2, TextView textView3) { 
		this.textView1 = textView1;
		this.textView2 = textView2;
		this.textView3 = textView3;
			}

		@Override
		protected Integer doInBackground(Integer... params) {
			zipCode = params[0];
			Pull();
			return zipCode;
		}

	
	
	     protected void onPostExecute(Integer result) {
	
	    		if (textView1 != null) {
					textView1.setText(weatherText);
				}

	    		if (textView2 != null) {
					textView2.setText(tempature.toString());
				}

	    		if (textView3 != null) {
					textView3.setVisibility(View.VISIBLE);
				}
	     
	     }

	     
	     

	 	public void Pull() {
	 		
	 		HttpClient client = new DefaultHttpClient();
	 		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
	 		HttpResponse response;

	 		try {
	 			HttpGet get = new HttpGet("http://www.myweather2.com/developer/forecast.ashx?query=" + zipCode + "&output=json&temp_unit=f&ws_unit=mps&uac=kczEvmrdbN"); 
	 			response = client.execute(get);

	 	
	 			if (response != null) {
	 	
	 				HttpEntity entity = response.getEntity();
	 				String responseText = EntityUtils.toString(entity);
	 				JSONObject json = new JSONObject(responseText);
	 				JSONObject json2 = json.getJSONObject("weather");
	 				JSONArray ja = json2.getJSONArray("curren_weather");
	 				JSONObject json3 = ja.getJSONObject(0);
	 				tempature = json3.getInt("temp");
	 				weatherText = json3.getString("weather_text");
	 							
	 			}
	 		} catch (Exception e) {
	 			Log.v("log",
	 					"Exception: download message = "
	 							+ e.toString());
	 		} 
	 	}
	     
	     
	     
	     
	
	}


