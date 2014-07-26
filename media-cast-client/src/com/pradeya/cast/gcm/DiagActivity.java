package com.pradeya.cast.gcm;

import com.pradeya.cast.media.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class DiagActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// bundle = savedInstanceState
		
		setContentView(R.layout.diag);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

}
