package com.pradeya.cast.media;


import com.pradeya.cast.media.R;
import com.google.android.gcm.GCMRegistrar;
import com.pradeya.cast.gcm.ServerUtilities;
import com.pradeya.cast.util.CommonUtilities;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class GCMRegisterService extends IntentService {
	public static Context context;

	public GCMRegisterService() {
		super("GCMRegisterService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
		CommonUtilities.init(PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext()));

		checkNotNull(CommonUtilities.WEB_SERVER_URL, "WEB_SERVER_URL");
		checkNotNull(CommonUtilities.SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(context);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(context);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//GCMRegistrar.register(context, SENDER_ID);
		final String regId = GCMRegistrar.getRegistrationId(context);
		if (regId.equals("")) {
			GCMRegistrar.register(context, CommonUtilities.SENDER_ID);
		} else {
			ServerUtilities.register(context, regId);
        		//context.startActivity(new Intent(context, MediacastMain.class).
        		//		setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	@Override
	public void onDestroy() {
		GCMRegistrar.onDestroy(getApplicationContext());
		super.onDestroy();
	}
}