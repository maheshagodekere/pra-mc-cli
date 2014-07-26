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
package com.pradeya.cast.util;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class CommonUtilities {

	//Mediacast production Server details
	//public static String SERVER_IP = "54.255.134.91";
    //public static String SERVER_PORT = "80";
    
	//Mediacast local Server details
	public static String SERVER_IP = "192.168.0.2";
    public static String SERVER_PORT = "9080";

    //Mediacast Server url
    public static String WEB_SERVER_URL = "http://"+SERVER_IP+":"+SERVER_PORT;
    public static String WEBSERVICE_SERVER_URL = "http://"+SERVER_IP+":"+SERVER_PORT+"/mc-webservice";

    //Media download server url
    public static String DOWNLOAD_URL =   WEB_SERVER_URL+"/md?mid=";


    public static  String TAG = "GCMDemo";

    //GCM project number for GCM regiatration
    public static String SENDER_ID = "453135562666";
                                            
    // Tag used on log messages.
    public static  String DISPLAY_MESSAGE_ACTION =
            "com.pradeya.cast.gcm.DISPLAY_MESSAGE";
     // Intent's extra that contains the message to be displayed.
    public static  String EXTRA_MESSAGE = "message";
    
    //Media constants
    public static  String MEDIA_DIR_PATH = "/mnt/sdcard/mediacast/pull";
    public static  String MP4_EXT = ".mp4";
    public static  String JPG_EXT = ".jpg";
    public static  String PNG_EXT = ".png";
    public static  String VIDEO = "VIDEO";
    public static  String IMAGE = "IMAGE";

	public static final String MEDIA_ID = "mid";
	public static final String SCHEDULE_ID = "sid";
	public static final String DISPLAY_ID = "did";

	//Download constants
	public static final int TIMEOUT_CONNECTION = 5000;// 5sec
	public static final int TIMEOUT_SOCKET = 30000;// 30sec
    
	public static void init(SharedPreferences prefs){
    }
    
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
	public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
