package com.pradeya.cast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

  
  public void onReceive(Context context, Intent callingIntent) {
    Log.d("################################BootReceiver", "onReceived");
	Log.d("BootReceiver", "onReceived");
  //Start App On Boot Start Up
    Intent mediaCastApp = new Intent(context, com.pradeya.cast.media.MediacastMain.class);
    mediaCastApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(mediaCastApp);
  }

}
