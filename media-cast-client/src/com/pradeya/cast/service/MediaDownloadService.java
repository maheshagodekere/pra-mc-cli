package com.pradeya.cast.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.pradeya.cast.media.MediaBean;
import com.pradeya.cast.media.MediaStore;
import com.pradeya.cast.util.CommonUtilities;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MediaDownloadService extends IntentService {
	private static final String TAG = "MediaDownloadService";
	private int result = Activity.RESULT_CANCELED;


	public MediaDownloadService() {
		super("MediaDownload service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String mid = intent.getStringExtra(CommonUtilities.MEDIA_ID);
		String sid = intent.getStringExtra(CommonUtilities.SCHEDULE_ID);
		Log.i(TAG, "Media id received: " + mid);
		String fileName = "media.jpg";

		try {
			URL url = new URL(CommonUtilities.WEB_SERVER_URL + "/md?mid=" + mid+"&sid="+sid);
			long startTime = System.currentTimeMillis();
			Log.i(TAG, "image download beginning: " + url);

			// Open a connection to that URL.
			URLConnection ucon = url.openConnection();

			String cd = ucon.getHeaderField("Content-Disposition");
			Log.i(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Content-Disposition ....: " + cd);
			// cd = "attachment; filename=abc.jpg"
			if (cd != null && cd.indexOf("=") != -1) {
				fileName = cd.split("=")[1];
			} else {
				fileName = "media.jpg";
			}

			File mfile = new File(CommonUtilities.MEDIA_DIR_PATH);
			File output = new File(mfile, fileName);
			if (output.exists()) {
				output.delete();
			}

			// this timeout affects how long it takes for the app to realize
			// there's a connection problem
			ucon.setReadTimeout(CommonUtilities.TIMEOUT_CONNECTION);
			ucon.setConnectTimeout(CommonUtilities.TIMEOUT_SOCKET);

			// Define InputStreams to read from the URLConnection.
			// uses 3KB download buffer

			Log.i(TAG, "Downloaded path ....: " + output);
			InputStream is = ucon.getInputStream();
			BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
			FileOutputStream outStream = new FileOutputStream(output);
			byte[] buff = new byte[5 * 1024];

			// Read bytes (and store them) until there is nothing more to
			// read(-1)
			int len;
			while ((len = inStream.read(buff)) != -1) {
				outStream.write(buff, 0, len);
			}

			// clean up
			outStream.flush();
			outStream.close();
			inStream.close();

			Log.i(TAG, "### download completed in "
					+ ((System.currentTimeMillis() - startTime) / 1000)
					+ " sec");

			MediaBean dm = new MediaBean();
			if (output.getPath().contains(CommonUtilities.MP4_EXT)) {
				dm.setMediaType(CommonUtilities.VIDEO);
			} else {
				dm.setMediaType(CommonUtilities.IMAGE);
			}
			dm.setMediaLocalPath(output.getPath());
			Log.i(TAG,
					"### Adding to MediaStore: " + output.getPath());
			MediaStore.addFirst(dm);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// //@Override
	// protected void onHandleIntentOld(Intent intent) {
	// InputStream stream = null;
	// FileOutputStream fos = null;
	// Log.i(TAG, "######################## Intent: " + intent);
	// String mid = intent.getStringExtra(GCMIntentService.MEDIA_ID);
	// String sid = intent.getStringExtra(GCMIntentService.SCHEDULE_ID);
	// Log.i(TAG, "Media id received: " + mid);
	// String fileName = "media.jpg";
	//
	// try {
	// URL url = new URL(CommonUtilities.DOWNLOAD_URL + mid);
	// Log.i(TAG, "Download url: " + url);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	//
	// String cd = conn.getHeaderField("Content-Disposition");
	// // cd = "attachment; filename=abc.jpg"
	// if (cd != null && cd.indexOf("=") != -1) {
	// fileName = cd.split("=")[1];
	// } else {
	// fileName = "media.jpg";
	// }
	//
	// File mfile = new File(CommonUtilities.MEDIA_DIR_PATH);
	// File output = new File(mfile,fileName);
	// if (output.exists()) {
	// output.delete();
	// }
	//
	// stream = conn.getInputStream();
	// InputStreamReader reader = new InputStreamReader(stream);
	// fos = new FileOutputStream(output.getPath());
	// Log.i(TAG, "device local path: " + output.getPath());
	// int next = -1;
	// while ((next = reader.read()) != -1) {
	// fos.write(next);
	// }
	// // Sucessful finished
	// result = Activity.RESULT_OK;
	//
	// MediaBean dm = new MediaBean();
	// if(output.getPath().contains("mp4")){
	// dm.setMediaType("VIDEO");
	// dm.setMediaLocalPath("/mnt/sdcard/ad/spotify-30-480-360.mp4");
	// }else{
	// dm.setMediaType("IMAGE");
	// dm.setMediaLocalPath(output.getPath());
	// }
	// dm.setMediaLocalPath(output.getPath());
	// Log.i(TAG, "%%%%%%%%%%%%% Adding to MediaStore: " + output.getPath());
	// MediaStore.addFirst(dm);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (stream != null) {
	// try {
	// stream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (fos != null) {
	// try {
	// fos.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// if (result == Activity.RESULT_OK) {
	// URL url;
	// try {
	// url = new URL(CommonUtilities.SERVER_URL + "/md?"
	// + GCMIntentService.MEDIA_ID + "=" + mid + "&"
	// + GCMIntentService.SCHEDULE_ID + "=" + sid + "&"
	// + "&result=success");
	//
	// Log.i(TAG, "Download success...inform ad server : " + url);
	// HttpURLConnection connection = (HttpURLConnection) url
	// .openConnection();
	// connection.setRequestMethod("GET");
	// connection.connect();
	//
	// int code = connection.getResponseCode();
	// Log.i(TAG, "Response code from serever : " + code);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
}