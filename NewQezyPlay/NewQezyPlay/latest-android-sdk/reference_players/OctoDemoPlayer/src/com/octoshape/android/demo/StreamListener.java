package com.octoshape.android.demo;

import octoshape.osa2.android.listeners.MediaPlayerListener;
import android.net.Uri;

public interface StreamListener {
	public abstract void onPrepared(Uri url, MediaPlayerListener mpListener);
	public abstract void onInit(boolean osaSeek, boolean nativeSeek, boolean isLive, long duration);
	public abstract void DVRSeekOffsetUpdate(int osaDVROffset);
}
