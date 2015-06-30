package com.octoshape.android.octocastplayer.util;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaInfo.Builder;
import com.google.android.gms.cast.MediaMetadata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Util {
	/**
	 * Shows an error dialog with a given text message.
	 * 
	 * @param activity
	 * @param errorString
	 */
	public static final void showErrorDialog(final Activity activity, final String errorString) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(activity).setTitle("Error")
				.setMessage(errorString)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).create().show();
			}
		});
	}
	
	public static final void showShutdownDialog(final Activity activity) {
        new AlertDialog.Builder(activity).setTitle("Shutdown")
                .setMessage("Do you want to exit the application and stop all streaming sessions?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    	activity.finish();
                    	dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }
	
	public static MediaInfo createMediaInfoFromUrl(String mediaUrl, String OctoLink){
		MediaMetadata mediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
		mediaMetadata.putString(MediaMetadata.KEY_TITLE, OctoLink);
		Builder b = new MediaInfo.Builder(mediaUrl.toString());
		if(mediaUrl.endsWith("m3u8"))
			b.setContentType("application/x-mpegURL");
		else
			b.setContentType("video/MP2T");
		
		b.setStreamType(MediaInfo.STREAM_TYPE_LIVE);
	    b.setMetadata(mediaMetadata)
	              .build();
		return b.build();
	}
}
