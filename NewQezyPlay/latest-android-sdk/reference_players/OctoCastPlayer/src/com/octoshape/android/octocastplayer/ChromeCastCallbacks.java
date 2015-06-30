package com.octoshape.android.octocastplayer;

import com.google.android.gms.cast.CastDevice;

public interface ChromeCastCallbacks {
	public void onDeviceSelected(CastDevice castdevice);
	public void onDeviceUnselected(CastDevice castdevice);
	public void onFailedConnection(String error);
}
