/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pradeya.cast.media;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.pradeya.cast.util.CommonUtilities;

/**
 * Main UI for the demo app.
 */
public class MediacastSettings extends Activity {

	TextView mDisplay;
	AsyncTask<Void, Void, Void> mRegisterTask;
	SharedPreferences prefs;
	private Context context;
	static boolean active = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.display);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mDisplay = (TextView) findViewById(R.id.display);
		mDisplay.append(CommonUtilities.WEB_SERVER_URL + "\n");
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				CommonUtilities.DISPLAY_MESSAGE_ACTION));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.gcm_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		 * Typically, an application registers automatically, so options below
		 * are disabled. Uncomment them if you want to manually register or
		 * unregister the device (you will also need to uncomment the equivalent
		 * options on options_menu.xml).
		 */
		case R.string.menu_settings:
			//startActivity(new Intent(this, GcmPrefActivity.class));
			return true;
		case R.string.menu_register:
			//GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
			return true;
		case R.string.menu_unregister:
			//GCMRegistrar.unregister(this);
			return true;
		case R.string.menu_clear:
			mDisplay.setText(null);
			return true;
		case R.string.menu_ping:
			return true;
		case R.string.menu_internet:
			return true;
		case R.string.menu_refresh:
			return true;
		case R.string.menu_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
		} catch (Exception e) {

		}
		//GCMRegistrar.onDestroy(getApplicationContext());
		super.onDestroy();
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
			if ("REGISTER".equals(newMessage)) {
				mDisplay.setText(null);
			} else {
				mDisplay.append(newMessage + "\n");
			}
		}
	};

}