package com.octoshape.android.demo.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class Misc {
	
	/* The bitmask sent in USERTYPE_GRID_MULTICAST. */
	public static final int MULTICAST_AMT_LISTEN = 1;
	public static final int MULTICAST_AMT_RECV = 2;
	public static final int MULTICAST_NATIVE_LISTEN = 4;
	public static final int MULTICAST_NATIVE_RECV = 8;
	
	
	public static String millisToTimeString(int millis) {
		return String.format("%02d:%02d", ((millis / 1000) / 60),
				((millis / 1000) % 60));
	}
	
	public static void showMessage(final String error, final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast
						.makeText(activity, error, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}
}
