package com.octoshape.android.octocastplayer;

import octoshape.osa2.Problem;
import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.listeners.ProblemListener;

import com.google.android.gms.cast.CastMediaControlIntent;
import com.octoshape.android.client.OctoStatic;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.v7.media.MediaRouteSelector;
import android.util.Log;

public class CastApplication extends Application {

	static private OctoshapeSystem mOs;
	static private MediaRouteSelector mMediaRouteSelector;
	
	
	public static OctoshapeSystem getOctoshapeSystem(Context ctx){
		
		if(null == mOs){
			mOs = OctoStatic.create(ctx, new ProblemListener() {
				@Override
				public void gotProblem(Problem p) {
					Log.e("CastApplication", "OctoshapeSystem Problem: " + p.getMessage());
				}
			}, null);
			mOs.addPlayerNameAndVersion(OctoshapeSystem.MEDIA_PLAYER_NATIVE, OctoshapeSystem.MEDIA_PLAYER_NATIVE, ""+Build.VERSION.SDK_INT);
		}
		return mOs;
	}
		
	public static MediaRouteSelector getMediaRouteSelector(){
		if(null == mMediaRouteSelector )
			mMediaRouteSelector = new MediaRouteSelector.Builder().addControlCategory(CastMediaControlIntent.categoryForCast(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)).build();
		return mMediaRouteSelector;
	}
	
	public static void shutdownOctoshape(){
		OctoStatic.terminate(null);
		mOs = null;
	}
}
