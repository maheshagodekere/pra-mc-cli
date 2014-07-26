package com.pradeya.cast.media;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;

import com.pradeya.cast.service.MediaIdPullService;
import com.pradeya.cast.util.CommonUtilities;

public class MediacastMain extends Activity implements OnCompletionListener {
	private static final String TAG = "MediacastMain";

	private VideoView mVideoView;

	// private String current;
	private ArrayList<String> videoList = new ArrayList<String>();
	int currentVideo = 0;
	static boolean active = false;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);

		Calendar cal = Calendar.getInstance();

		// /*********Scheduling GCMRegistration Polling ***********/
		// Intent intent = new Intent(this, GCMRegisterService.class);
		// PendingIntent pintent = PendingIntent.getService(this, 2013, intent,
		// 0);
		//
		// AlarmManager alarm = (AlarmManager)
		// getSystemService(Context.ALARM_SERVICE);
		// alarm.cancel(pintent);
		// // Start every 15 Minutes
		// alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// 15 * 60 * 1000, pintent);

		/********* Scheduling Media Server Polling ***********/
		Intent mediaIdIntent = new Intent(this, MediaIdPullService.class);
		PendingIntent mediaIdPendingIntent = PendingIntent.getService(this,
				2013, mediaIdIntent, 0);

		AlarmManager mediaIdAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mediaIdAlarm.cancel(mediaIdPendingIntent);
		// Start every 15 Seconds
		mediaIdAlarm.setRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(), 30 * 1000, mediaIdPendingIntent);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// videoList.add("/sdcard/ad/apple-80-320-240.mp4");
		// videoList.add("/sdcard/ad/ccalls-60-480-270.mp4");
		loadAllDownlaodedFiles();
		setContentView(R.layout.player);
		// super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// mVideoView.setBackgroundDrawable(getResources().getDrawable(R.drawable.adele));

		MediaController mMedia = new MediaController(this);
		mMedia.setMediaPlayer(mVideoView);
		mMedia.setAnchorView(mVideoView);
		// mVideoView.setMediaController(mMedia);

		MediaPlayer.OnCompletionListener myVideoViewCompletionListener = new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.d(TAG, "### Playback complete...moving to next media");
				// currentVideo++;
				// if (currentVideo > videoList.size() - 1) {
				// currentVideo = 0;
				// // Image can be set as background on the fly by setting
				// // Drawable resource
				// //
				// mVideoView.setBackgroundDrawable(getResources().getDrawable(R.drawable.adele));
				// }
				//
				// // mp.stop();
				// // mp.release();
				// // mp.reset();
				playVideo();
			}
		};
		mVideoView.setOnCompletionListener(myVideoViewCompletionListener);

		runOnUiThread(new Runnable() {
			public void run() {
				playVideo();

			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "### onResume...Resume playing ");
		playVideo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
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
			mVideoView.stopPlayback();
			// startActivity(new Intent(this, GcmPrefActivity.class));
			return true;
		case R.string.menu_diagnostics:
			mVideoView.stopPlayback();
			startActivity(new Intent(this, MediacastSettings.class));
			return true;
		case R.string.menu_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void playVideo() {
		try {
			mVideoView.stopPlayback();
			String path = "/sdcard/mediacast/blank.mp4"; // getResources().getMovie(R.drawable.starting);

			if (MediaStore.getSize() == 0) {
				loadAllDownlaodedFiles();
			}

			MediaBean media = MediaStore.removeFirst();
			if (media == null) {
				mVideoView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.contactus));
			} else {

				if (CommonUtilities.VIDEO.equals(media.getMediaType())) {
					mVideoView.setBackgroundDrawable(null);
					path = media.getMediaLocalPath();
				} else {
					mVideoView.setBackgroundDrawable(Drawable
							.createFromPath(media.getMediaLocalPath()));
					path = "/sdcard/mediacast/blank.mp4";
				}
				MediaStore.addLast(media);
			}

			Log.v(TAG, "Background media path: " + path);
			if (media != null) {
				Log.v(TAG,
						"Foreground media path: " + media.getMediaLocalPath());
			}
			if (path == null || path.length() == 0) {
				Toast.makeText(MediacastMain.this, "File URL/path is empty",
						Toast.LENGTH_LONG).show();
			} else {
				mVideoView.setVideoPath(path);
				mVideoView.start();
				mVideoView.requestFocus();

			}
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			if (mVideoView != null) {
				mVideoView.stopPlayback();
			}
		}
	}

	// private String getDataSource(String path) throws IOException {
	// if (!URLUtil.isNetworkUrl(path)) {
	// return path;
	// } else {
	// URL url = new URL(path);
	// URLConnection cn = url.openConnection();
	// cn.connect();
	// InputStream stream = cn.getInputStream();
	// if (stream == null)
	// throw new RuntimeException("stream is null");
	// File temp = File.createTempFile("mediaplayertmp", "dat");
	// temp.deleteOnExit();
	// String tempPath = temp.getAbsolutePath();
	// FileOutputStream out = new FileOutputStream(temp);
	// byte buf[] = new byte[128];
	// do {
	// int numread = stream.read(buf);
	// if (numread <= 0)
	// break;
	// out.write(buf, 0, numread);
	// } while (true);
	// try {
	// stream.close();
	// } catch (IOException ex) {
	// Log.e(TAG, "error: " + ex.getMessage(), ex);
	// }
	// return tempPath;
	// }
	// }

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	class Pair implements Comparable {
		public long t;
		public File f;

		public Pair(File file) {
			f = file;
			t = file.lastModified();
		}

		public int compareTo(Object o) {
			long u = ((Pair) o).t;
			return t < u ? -1 : t == u ? 0 : 1;
		}
	};

	private void loadAllDownlaodedFiles() {
		// Obtain the array of (file, timestamp) pairs.
		Log.d(TAG, "### Loading from file path /mnt/sdcard/mediacast/pull");
		File dir = new File(CommonUtilities.MEDIA_DIR_PATH);
		File[] files = dir.listFiles();
		Log.d(TAG, "### Filelist from " + CommonUtilities.MEDIA_DIR_PATH + ": "
				+ files.length);
		Pair[] pairs = new Pair[files.length];
		String[] absoluteFilePath = new String[files.length];
		for (int i = 0; i < files.length; i++)
			pairs[i] = new Pair(files[i]);

		// Sort them by timestamp.
		Arrays.sort(pairs);
		Log.d(TAG, "### Filelist sorting complete");
		// Take the sorted pairs and extract only the file part, discarding the
		// timestamp.

		for (int i = 0; i < files.length; i++)
			absoluteFilePath[i] = pairs[i].f.getAbsolutePath();

		Log.d(TAG, "### Loading Mediastore");
		loadMediaStore(absoluteFilePath);
		Log.d(TAG, "### Mediastore after loading: " + MediaStore.getSize());

	}

	private void loadMediaStore(String[] allFiles) {
		if (allFiles != null && allFiles.length > 0) {
			Log.i(TAG, "### Clearing all datastore elements of size: "
					+ MediaStore.getSize());
			MediaStore.clearDataStore();
		} else
			return;

		for (int i = 0; i < allFiles.length; i++) {
			MediaBean dm = new MediaBean();
			if (allFiles[i].contains(CommonUtilities.MP4_EXT)) {
				dm.setMediaType(CommonUtilities.VIDEO);
			} else {
				dm.setMediaType(CommonUtilities.IMAGE);
			}
			dm.setMediaLocalPath(allFiles[i]);
			Log.i(TAG, "### Adding to MediaStore: " + allFiles[i]);
			MediaStore.addLast(dm);
		}
	}

}
