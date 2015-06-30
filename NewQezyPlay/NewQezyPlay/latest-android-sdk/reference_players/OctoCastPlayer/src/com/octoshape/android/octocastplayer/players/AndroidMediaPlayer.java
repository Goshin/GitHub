package com.octoshape.android.octocastplayer.players;

import com.octoshape.android.client.OctoStatic;

import octoshape.osa2.android.listeners.MediaPlayerListener;
import octoshape.osa2.listeners.ProblemListener;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.widget.VideoView;

public class AndroidMediaPlayer {

	public static final String LOGTAG = "AndroidPlayer";
	private VideoView mMediaPlayer;
	private Uri mMediaUrl;
	private MediaPlayerListener mMediaPlayerListener;
	private ProblemListener mProblemListener;
	
	public AndroidMediaPlayer(Activity activity, VideoView vv, ProblemListener problemListener){
		mMediaPlayer = vv;
		mProblemListener = problemListener;
	}
	/**
	 * Setting up and playing a received media URL
	 * 
	 * @param mediaUrl URL which needs to be passed to a media player
	 * @param mpl MediaPlayerListen used for reporting stream start and end
	 * @param surfaceView 
	 */
	public void playStream(final Uri mediaUrl, final MediaPlayerListener mpl) {

		Log.d(LOGTAG, "Now playing: " + mediaUrl);
		mMediaUrl = mediaUrl;
		mMediaPlayerListener = mpl;
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(null != mMediaPlayerListener)
					mMediaPlayerListener.onMediaPlaybackCompleted();
			}
		});
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1  && null != mMediaPlayerListener)
			mMediaPlayer.setOnInfoListener(new OnInfoListener() {
				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
						Log.d("MediaPlayer", "START RENDERING");
						mMediaPlayerListener.onMediaPlaybackStarted();
					}
					return false;
				}
			});
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 && null != mMediaPlayerListener)
			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					Log.d("MediaPlayer", "PLAYERBACK STARTED");
					mMediaPlayerListener.onMediaPlaybackStarted();
				}
			});
		mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				mProblemListener.gotProblem(OctoStatic.generateMediaPlayerProblem(what, extra));
				Log.d(LOGTAG, "MediaPlayer error:" + what + ":" + extra);
				return true;
			}
		});

		try{
			mMediaPlayer.setVideoURI(mediaUrl);
			mMediaPlayer.start();
		} catch (Exception e) {
			Log.e(LOGTAG, "Error preparing MediaPlayer", e);
		}
	}
	
	public void release(){
		if (mMediaPlayer != null){
			stopPlayback();
		}
	}
	public Uri getMediaUrl() {
		return mMediaUrl;
	}

	public boolean isPlaying() {
		return (mMediaPlayer != null && mMediaPlayer.isPlaying());
	}

	public void stopPlayback() {
		if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
			mMediaPlayer.stopPlayback();
		}
	}
	public void pause() {
		mMediaPlayer.pause();
		
	}
	public void resume() {
		mMediaPlayer.start();
	}
}
