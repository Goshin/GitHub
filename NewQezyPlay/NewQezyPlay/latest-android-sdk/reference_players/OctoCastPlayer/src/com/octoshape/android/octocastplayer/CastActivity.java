package com.octoshape.android.octocastplayer;

import octoshape.osa2.Problem;
import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.android.StreamPlayer;
import octoshape.osa2.android.listeners.MediaPlayerListener;
import octoshape.osa2.android.listeners.StreamPlayerListener;
import octoshape.osa2.listeners.OctoshapeSystemListener;
import octoshape.osa2.listeners.ProblemListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.VideoView;

import com.google.android.gms.cast.CastDevice;
import com.octoshape.android.octocastplayer.R;
import com.octoshape.android.octocastplayer.players.AndroidMediaPlayer;
import com.octoshape.android.octocastplayer.players.ChromeCastPlayer;
import com.octoshape.android.octocastplayer.util.Util;

public class CastActivity extends ActionBarActivity implements ProblemListener {

	private static final String LOGTAG = "OctoAndroidPlayer";
	//private static final String OCTOLINK = "octoshape://streams.octoshape.net/demoplayer/mobile/sintel/auto_flv";
	private static final String OCTOLINK = "octoshape://streams.octoshape.net/demo/live/nba/abr";
	protected Handler mHandler;
	
	// ChromeCast
	private ChromeCastPlayer mChromeCast;
	private MediaRouter mMediaRouter;
	
	// Octoshape
	private StreamPlayer mStreamPlayer;
	private OctoshapeSystem mOs;
	
	// MediaPlayerEvents
	private AndroidMediaPlayer mAndroidPlayer;
	protected MediaPlayerListener mMediaPlayerListener;
	
	private boolean playingLocal = true;
	private VideoView mVideoView;

	
	/**
	 * Called when the activity is first created. Creating views, GUI, setting
	 * listeners. Here the OSA is initialized.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mVideoView = new VideoView(this);
		setContentView(mVideoView);
		
		//setContentView(R.layout.video);
		mHandler = new Handler();
				
		// initialize ChromeCast 
		mMediaRouter = MediaRouter.getInstance(getApplicationContext());
		mChromeCast = new ChromeCastPlayer(this, new ChromeCastCallbacks() {
			
			@Override
			public void onFailedConnection(String errorString) {
				Util.showErrorDialog(CastActivity.this, errorString);
				playingLocal = true;
			}
			@Override
			public void onDeviceUnselected(CastDevice castdevice) {
				Log.d(LOGTAG, "ChromeCast :"+ castdevice.getFriendlyName() +" disconnected");
				if(mChromeCast.isPlaying())
					mAndroidPlayer.playStream(Uri.parse(mChromeCast.getMediaUrl()), mMediaPlayerListener);
				playingLocal = true;
			}
			@Override
			public void onDeviceSelected(CastDevice castdevice) {
				if(mAndroidPlayer.isPlaying()){
					mChromeCast.playStream(Util.createMediaInfoFromUrl(mAndroidPlayer.getMediaUrl().toString(), OCTOLINK), mMediaPlayerListener);
					mAndroidPlayer.stopPlayback();
				}
				Log.d(LOGTAG, "ChromeCast :"+ castdevice.getFriendlyName() +" selected");
				playingLocal = false;
			}
		});
		
		mOs = CastApplication.getOctoshapeSystem(this.getApplicationContext());
		mOs.setOctoshapeSystemListener(new OctoshapeSystemListener() {
			@Override
			public void onConnect(String autId) {
				prepareStream(mOs.createStreamPlayer(OCTOLINK));
			}
		});
		mOs.open();
		
		// initalize Android player
		mAndroidPlayer = new AndroidMediaPlayer(this, mVideoView, this);
	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
		MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
		mediaRouteActionProvider.setRouteSelector(CastApplication.getMediaRouteSelector());
		return true;
	}
	
	/**
	 * Adding a stream to the OctoMediaPlayer. Creates an StreamPlayer the
	 * StreamPlayer instance is initiated with it's own UrlListener and
	 * ProblemListener.
	 * 
	 * @param stream
	 *            link (e.g., octoshape://ond.octoshape.com/demo/ios/bbb.mp4)
	 * @return StreamPlayer on which we can request playback.
	 */
	public void prepareStream(StreamPlayer sp) {

		mStreamPlayer = sp;
		mStreamPlayer.setOctolinkOption("airplay", "true");
		mStreamPlayer.setProblemListener(this);
		mStreamPlayer.setListener(new StreamPlayerListener() {
			
			@Override
			public void gotUrl(final String url, long seekOffset, final MediaPlayerListener mpl) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mMediaPlayerListener = mpl;
						if(playingLocal)
							mAndroidPlayer.playStream(Uri.parse(url), mMediaPlayerListener);
						else
							mChromeCast.playStream(Util.createMediaInfoFromUrl(url, OCTOLINK), mMediaPlayerListener);
						
					}
				});
			}
			@Override
			public void gotNewOnDemandStreamDuration(long duration) {}
			@Override
			public void resolvedNativeSeek(boolean isLive, String playerId) {}
			@Override
			public void resolvedNoSeek(boolean isLive, String playerId) {}
			@Override
			public void resolvedOsaSeek(boolean isLive, long duration, String playerId) {}
		});
		mStreamPlayer.requestPlay();
	}
	
	
	@Override
	public void gotProblem(final Problem p) {
		Log.e(LOGTAG, "Problem: "+p.toString());
		if(p.getMessage()!=null)
			Util.showErrorDialog(this, p.getMessage() + "("+ p.getErrorCode() +")");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mMediaRouter.addCallback(CastApplication.getMediaRouteSelector(), mChromeCast, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(playingLocal && mAndroidPlayer.isPlaying())
			mAndroidPlayer.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(playingLocal && mAndroidPlayer.getMediaUrl() != null)
			mAndroidPlayer.resume();
	}
	
	@Override
	public void onBackPressed() {
		Util.showShutdownDialog(this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mMediaRouter.removeCallback(mChromeCast);
		mChromeCast.shutdown();
		mAndroidPlayer.release();
		if( mStreamPlayer != null)
			mStreamPlayer.close(null);
		
		CastApplication.shutdownOctoshape();
	}
}