package com.pradeya.cast.gcm;

import com.pradeya.cast.media.R;
import com.pradeya.cast.util.CommonUtilities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class GcmPrefActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private static final String TAG = "GcmPrefActivity";

	public static final String SERVERIP = "serverip";
	public static final String SERVERPORT = "serverport";
	public static final String GMAILID = "gmailid";
	public static final String SENDERID = "senderid";

	private EditTextPreference serveripPreference;
	private EditTextPreference serverportPreference;
	private EditTextPreference gmailidPreference;
	private EditTextPreference senderidPreference;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.gcmpref);

		serveripPreference = (EditTextPreference) getPreferenceScreen()
				.findPreference(SERVERIP);
		serverportPreference = (EditTextPreference) getPreferenceScreen()
				.findPreference(SERVERPORT);
//		gmailidPreference = (EditTextPreference) getPreferenceScreen()
//		.findPreference(GMAILID);		
//		senderidPreference = (EditTextPreference) getPreferenceScreen()
//		.findPreference(SENDERID);		
       
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Setup the initial values

		serveripPreference.setSummary(getPreferenceScreen()
				.getSharedPreferences().getString(SERVERIP, "Enter server IP"));
		serverportPreference.setSummary(getPreferenceScreen()
				.getSharedPreferences().getString(SERVERPORT,
						"Enter server port"));
//		gmailidPreference.setSummary(getPreferenceScreen()
//				.getSharedPreferences().getString(GMAILID,
//						"Enter gmailid"));
//		senderidPreference.setSummary(getPreferenceScreen()
//				.getSharedPreferences().getString(SENDERID,
//						"Enter GCM Sender ID"));

		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		CommonUtilities.init(PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext()));


	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		// Let's do something a preference value changes
		if (key.equals(SERVERIP)) {
			serveripPreference.setSummary(getPreferenceScreen()
					.getSharedPreferences().getString(SERVERIP,
							"Enter server IP"));
		} else if (key.equals(SERVERPORT)) {
			serverportPreference.setSummary(getPreferenceScreen()
					.getSharedPreferences().getString(SERVERPORT,
							"Enter server port"));
		}else if (key.equals(GMAILID)) {
			gmailidPreference.setSummary(getPreferenceScreen()
					.getSharedPreferences().getString(GMAILID,
							"Enter gmailid"));
		}else if (key.equals(SENDERID)) {
			serverportPreference.setSummary(getPreferenceScreen()
					.getSharedPreferences().getString(SENDERID,
							"Enter GCM senderid"));
		}
		CommonUtilities.init(PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext()));

	}
}
