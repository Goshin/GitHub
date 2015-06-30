package com.octoshape.android.demo;

import java.util.LinkedList;
import java.util.Queue;

import octoshape.osa2.android.StreamPlayer;
import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.listeners.ProblemListener;
import octoshape.osa2.android.listeners.MediaPlayerListener;
import octoshape.osa2.android.listeners.StreamPlayerListener;
import android.net.Uri;
import android.util.Log;

public class Stream {

	private String octolink;
	private String bitrate;
	private String name;
	private StreamPlayer sp;
	private long osaSeekOffset;
	private long osaDuration;
	private boolean isLive;
	private boolean nativeSeek;
	private String selectedPlayerId;
	private boolean osaSeek;
	private int offsetDVR;
	public Queue<Uri> urlQueue = new LinkedList<Uri>();
	protected long maxDVR;
	public MediaPlayerListener mediaPlayerListener = null;
	
	public Stream(String octolink, String bitrate, String name) {
		super();
		this.octolink = octolink;
		this.bitrate = bitrate;
		this.name = name;
	}

	public String getOctolink() {
		return octolink;
	}

	public String getBitrate() {
		return bitrate;
	}

	public String getName() {
		return name;
	}

	public void playback(OctoshapeSystem os, ProblemListener mProblemListener, final StreamListener callback) {

		sp = os.createStreamPlayer(octolink);
		sp.setProblemListener(mProblemListener);
		sp.setListener(new StreamPlayerListener() {

			@Override
			public void gotUrl(final String url, final long seekOffset, MediaPlayerListener mpListener) {

				Log.d(OctoshapeApplication.LOGTAG, "gotUrl: " + url);
				Log.d(OctoshapeApplication.LOGTAG, "seekOffset: " + seekOffset);
				mediaPlayerListener = mpListener; 
				osaSeekOffset = seekOffset;
				callback.onPrepared(Uri.parse(url), mpListener);

			}

			@Override
			public void gotNewOnDemandStreamDuration(long duration) {
				osaDuration = duration;
			}

			@Override
			public void resolvedNativeSeek(boolean live, String playerId) {
				Log.d(OctoshapeApplication.LOGTAG, name
						+ ": resolvedNativeSeek: ");
				isLive = live;
				nativeSeek = true;
				selectedPlayerId = playerId;
				callback.onInit(osaSeek, nativeSeek,live,-1);
			}

			@Override
			public void resolvedNoSeek(boolean live, String playerId) {
				Log.d(OctoshapeApplication.LOGTAG, name + ": resolvedNoSeek: ");
				isLive = live;
				selectedPlayerId = playerId;
				callback.onInit(osaSeek, nativeSeek,live, -1);
			}

			@Override
			public void resolvedOsaSeek(boolean live, long duration,
					String playerId) {
				Log.d(OctoshapeApplication.LOGTAG, name + ": resolvedOsaSeek: ");
				isLive = live;
				nativeSeek = false;
				osaSeek = true;
				if (!isLive)
					osaDuration = duration;
				else {
					maxDVR = duration;
				}
				selectedPlayerId = playerId;
				callback.onInit(osaSeek, nativeSeek,live, duration);
			}
		});
		sp.requestPlay();
	}
	public long getOsaSeekOffset() {
		return osaSeekOffset;
	}

	public long getOsaDuration() {
		return osaDuration;
	}

	public boolean isLive() {
		return isLive;
	}

	public boolean isNativeSeek() {
		return nativeSeek;
	}

	public String getSelectedPlayerId() {
		return selectedPlayerId;
	}

	public boolean isOsaSeek() {
		return osaSeek;
	}
	public StreamPlayer getStreamPlayer() {
		return sp;
	}
	public long getMaxDVR(){
		return maxDVR;
	}
	public int getCurrentDVR() {
		return this.offsetDVR;
	}
	public void setCurrentDVR(int progress) {
		this.offsetDVR = progress;
	}
	public void close(){
		if( sp !=null )
			sp.close(null);
	}
}
