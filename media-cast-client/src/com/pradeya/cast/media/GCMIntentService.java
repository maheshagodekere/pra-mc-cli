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

import static com.pradeya.cast.util.CommonUtilities.SENDER_ID;
import static com.pradeya.cast.util.CommonUtilities.displayMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pradeya.cast.media.R;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.pradeya.cast.gcm.ServerUtilities;
import com.pradeya.cast.service.MediaDownloadService;
import com.pradeya.cast.util.CommonUtilities;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {
	
    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "### Device registered on Google GCM server ### : regId = " + registrationId);
        displayMessage(context, "### Device registered on Google GCM server ###");
        ServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.e(TAG, "### Device unregistered on Google GCM Server ###");
        displayMessage(context, "### Device unregistered on Google GCM Server ###");
//        if (GCMRegistrar.isRegisteredOnServer(context)) {
//            ServerUtilities.unregister(context, registrationId);
//        } else {
//            // This callback results from the call to unregister made on
//            // ServerUtilities when the registration to the server failed.
//            Log.i(TAG, "Ignoring unregister callback");
//        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "### Message Received from Google GCM Server ###");
        
//        String mid = intent.getStringExtra(CommonUtilities.MEDIA_ID);
//        String sid = intent.getStringExtra(CommonUtilities.SCHEDULE_ID);
//        String did = intent.getStringExtra(CommonUtilities.DISPLAY_ID);
//        
//        Log.i(TAG, "### Got mediaid :: "+mid);
//        //String message = getString(R.string.gcm_message);
//        //displayMessage(context, message);
//        // notifies user
//        
//        Intent di = new Intent(getApplicationContext(), MediaDownloadService.class);
//        di.putExtra(CommonUtilities.MEDIA_ID, mid);
//        di.putExtra(CommonUtilities.SCHEDULE_ID, sid);
//        di.putExtra(CommonUtilities.DISPLAY_ID, did);
//        startService(di);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "### Messages deleted on Google GCM Server ###");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "### Error received from Google GCM Server ###: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "### Received recoverableError from Google GCM Server ###: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MediacastSettings.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

}
