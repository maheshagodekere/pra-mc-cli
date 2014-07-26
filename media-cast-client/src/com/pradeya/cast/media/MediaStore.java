package com.pradeya.cast.media;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.util.Log;

/**
 * Simple implementation of a data store using standard Java collections.
 * <p>
 * This class is thread-safe but not persistent (it will loose the data when the
 * app is restarted).
 */
public final class MediaStore {
	private static final String TAG = "MediaStore";
	private static final LinkedList<MediaBean> mdll = new LinkedList<MediaBean>();

	private MediaStore() {
		throw new UnsupportedOperationException();
	}

	public static synchronized void addFirst(MediaBean md) {
		Log.i(TAG, "### Adding First: " + md);
		if(!mdll.contains(md))
			mdll.addFirst(md);
		Log.i(TAG, "### Media Store Size: " + mdll.size());
	}

	public static synchronized void addLast(MediaBean md) {
		mdll.addLast(md);
	}

	public static synchronized MediaBean removeFirst() {
		MediaBean md = null;
		Log.i(TAG, "### removeFirst Media Store Size : " + mdll.size());
		try {
			md = mdll.removeFirst();
		} catch (NoSuchElementException nee) {
			Log.i(TAG, "### Remove First: No Such Element: " + md);
			return md;
		}
		Log.i(TAG, "### Remove First: " + md);
		return md;
	}
	
	public static synchronized void clearDataStore() {
		if(mdll!=null && mdll.size()>0)
			mdll.clear();
		
	}
	
	public static synchronized int getSize() {
		if(mdll!=null && mdll.size()>0)
			return mdll.size();
		else return 0;
		
	}

	public static synchronized MediaBean removeLast() {
		MediaBean md = null;
		try {
			md = mdll.removeLast();
		} catch (NoSuchElementException nee) {
			return md;
		}
		return md;
	}

}
