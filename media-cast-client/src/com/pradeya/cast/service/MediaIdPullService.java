package com.pradeya.cast.service;

import static com.pradeya.cast.util.CommonUtilities.TAG;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pradeya.cast.util.CommonUtilities;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MediaIdPullService extends IntentService {
	public MediaIdPullService() {
		super(MediaIdPullService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Do useful things.

		// After doing useful things...
		Log.i(TAG, "### Polling Exexuting ");
		pollMediaServer();

	}

	private void pollMediaServer() {
		
		String response = makeServiceCall();
		if(response != null){
			try {
				
				//Log.i(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@ Parsing Response as JSON");
				JSONArray medias = new JSONArray(response);
				
				// looping through all medias
                for (int i = 0; i < medias.length(); i++) {
                	
                	JSONObject jsonMedia = medias.getJSONObject(i);
                	
                	String mid = jsonMedia.getString("mediaId");
                    String sid = jsonMedia.getString("id");
                    //String did = jsonMedia.getString("displayId");
                    
                    
                    Log.i(TAG, "### mid "+mid);
                    Log.i(TAG, "### sid "+sid);
                    
                    Intent di = new Intent(getApplicationContext(), MediaDownloadService.class);
                    di.putExtra(CommonUtilities.MEDIA_ID, mid);
                    di.putExtra(CommonUtilities.SCHEDULE_ID, sid);
                    //di.putExtra(CommonUtilities.DISPLAY_ID, did);
                    startService(di);                    
                }
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Log.i(TAG, "@@@@@@@@@@ Response is empty");
		}
	}

	/**
	 * Making service call
	 * 
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public String makeServiceCall() {
		String response = null;
		try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpConnectionParams
	                .setConnectionTimeout(httpClient.getParams(), 15000);

			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			
			Log.i(TAG, "### Polling Server: "+ CommonUtilities.WEBSERVICE_SERVER_URL + "/schedule/display/5");
			HttpGet httpGet = new HttpGet(CommonUtilities.WEBSERVICE_SERVER_URL+ "/schedule/display/5");

			
			httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			Log.i(TAG, "### Polling Server JSON Response: "+ response);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;

	}
}
