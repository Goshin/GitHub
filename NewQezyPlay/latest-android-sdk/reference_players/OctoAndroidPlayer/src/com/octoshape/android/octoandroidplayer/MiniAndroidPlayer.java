package com.octoshape.android.octoandroidplayer;

// Android OSA
import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.android.StreamPlayer;
import octoshape.osa2.listeners.OctoshapeSystemListener;
import octoshape.osa2.listeners.ProblemListener;
import octoshape.osa2.android.listeners.MediaPlayerListener;
import octoshape.osa2.android.listeners.StreamPlayerListener;
import octoshape.osa2.Problem;


// Android SDK
 
import com.octoshape.android.client.OctoStatic;

// Android Media Player 
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
 

// Android
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;


// Other
import java.util.LinkedList;
import java.util.Locale;

public class MiniAndroidPlayer extends Activity implements OnVideoSizeChangedListener, ProblemListener {

	private static final String LOGTAG = "OctoAndroidPlayer";
	private static final String OCTOLINK = "octoshape://streams.octoshape.net/demo/live/nascar/abr";
	private SurfaceView mSurface;
	private SurfaceHolder mHolder;
	private OctoshapeSystem os;
	private MediaPlayer mMediaPlayer;
	private StreamPlayer mStreamPlayer;
	protected byte currentStatus;
	protected Handler myHandler;

	/**
	 * Called when the activity is first created. Creating views, GUI, setting
	 * listeners. Here the OSA is initialized.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video);
		initOctoshapeSystem();
		myHandler = new Handler();

		// Setup views and holder to be used by MediaPlayer
		mSurface = (SurfaceView) findViewById(R.id.surface);
		mHolder = mSurface.getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * Creating OctoshapeSystem, adding ProblemListener for which we set to the
	 * language of the OS and setting OctoshapePortListener triggering a
	 * callback once the Octoshape service/client has started successfully.
	 */
	public void initOctoshapeSystem() {

		// Create OctoshapeSystem
		os = OctoStatic.create(this, this, null);
		os.setOctoshapeSystemListener(new OctoshapeSystemListener() {
			// called once the Octoshape service/client has started.
			@Override
			public void onConnect(String authId) {
				mStreamPlayer = setupStream(OCTOLINK);
				mStreamPlayer.requestPlay();
			}
		});
		// Adding AndroidMediaPlayer
		os.addPlayerNameAndVersion(OctoshapeSystem.MEDIA_PLAYER_NATIVE, OctoshapeSystem.MEDIA_PLAYER_NATIVE,"" + Build.VERSION.SDK_INT);
		os.open();
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
	public StreamPlayer setupStream(final String stream) {

		Log.d(LOGTAG, "Setting up stream: " + stream);
		myHandler.post(probeStreamPlayer);
		StreamPlayer sp = os.createStreamPlayer(stream);
		sp.setProblemListener(this);
		sp.setListener(new StreamPlayerListener() {
			
			/**
			 * Receiving new URL from the streamplayer object either due to
			 * requesting playback, seeking or experiencing a bitrate/resolution
			 * changes requiring the re-initialization of the Player.
			 * 
			 * @param url
			 *            to be passed to the media player
			 * @param seekOffset
			 *            offset we have seek to in milliseconds
			 */
			@Override
			public void gotUrl(String url, long seekOffset, MediaPlayerListener mpl) {
				playStream(Uri.parse(url), mpl);
			}

			/**
			 * Resets an on-demand file duration previously reported in
			 * resolvedOsaSeek(..) method
			 */
			@Override
			public void gotNewOnDemandStreamDuration(long duration) {
				// TODO:
			}

			/**
			 * Called if the stream is seekable using the media player's own
			 * native seeking functionality (e.g., the Android MediaPlayer does
			 * the seeking for us).
			 */
			@Override
			public void resolvedNativeSeek(boolean isLive, String playerId) {
				// TODO:
			}

			/**
			 * Called if it is not possible to seek in the stream.
			 */
			@Override
			public void resolvedNoSeek(boolean isLive, String playerId) {
				// TODO:
			}

			@Override
			/**
			 * Called when stream support OsaSeek / DVR
			 */
			public void resolvedOsaSeek(boolean isLive, long duration, String playerId) {
				// TODO:
			}
		});
		return sp;
	}
	/**
	 * Setting up and playing a received media URL
	 * 
	 * @param mediaUrl URL which needs to be passed to a media player
	 * @param mpl MediaPlayerListen used for reporting stream start and end
	 */
	protected void playStream(Uri mediaUrl, final MediaPlayerListener mpl) {

		Log.d(LOGTAG, "Now playing: " + mediaUrl);
		if (mMediaPlayer == null)
			mMediaPlayer = new MediaPlayer();
		
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDisplay(mHolder);
			mMediaPlayer.setDataSource(this, mediaUrl);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mpl.onMediaPlaybackCompleted();
				}
			});
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
				mMediaPlayer.setOnInfoListener(new OnInfoListener() {
					@Override
					public boolean onInfo(MediaPlayer mp, int what, int extra) {
						if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
							Log.d("MediaPlayer", "START RENDERING");
							mpl.onMediaPlaybackStarted();
						}
						return false;
					}
				});
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
				mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						Log.d("MediaPlayer", "PLAYERBACK STARTED");
						mpl.onMediaPlaybackStarted();
					}
				});
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					MiniAndroidPlayer.this.gotProblem(OctoStatic.generateMediaPlayerProblem(what, extra));
					Log.d(LOGTAG, "MediaPlayer error:" + what + ":" + extra);
					return true;
				}
			});
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {
			Log.e(LOGTAG, "Error preparing MediaPlayer", e);
			displayError("Error preparing MediaPlayer: " + e.getMessage());
		}
	}
	/**
	 * Handling onVideoSizeChanged called by the MediaPlayer after initiating
	 * playback, scales video according to aspect ratio and display dimensions.
	 */
	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

		RelativeLayout.LayoutParams vLayout = (RelativeLayout.LayoutParams) mSurface.getLayoutParams();
		vLayout.width = getWindowManager().getDefaultDisplay().getWidth();
		vLayout.height = getWindowManager().getDefaultDisplay().getHeight();

		float aspectRatio = (float) width / height;
		float screenRatio = (float) vLayout.width / vLayout.height;
		float topMargin = 0, leftMargin = 0;

		if (screenRatio < aspectRatio)
			topMargin = (float) vLayout.height - ((float) vLayout.width / aspectRatio);
		else if (screenRatio > aspectRatio)
			leftMargin = (float) vLayout.width - (vLayout.height * aspectRatio);

		vLayout.setMargins((int) leftMargin, (int) topMargin, 0, 0);
		mSurface.setLayoutParams(vLayout);
	}
	
	@Override
	public void gotProblem(final Problem p) {
		Log.e(LOGTAG, "Problem: "+p.toString());
		if(p.getMessage()!=null)
			displayError(p.getMessage() + "("+ p.getErrorCode() +")");
	}
	
	private void displayError(final String error) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MiniAndroidPlayer.this, error, Toast.LENGTH_LONG).show();
			}
		});
	}
	Runnable probeStreamPlayer = new Runnable() {
		
		@Override
		public void run() {
			if(mStreamPlayer != null ){
				if(mStreamPlayer.getStatus() != currentStatus){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
						
							String status;
							switch (mStreamPlayer.getStatus()) {
							case StreamPlayer.STATUS_CLOSED:
								status = "STATUS_CLOSED";
								break;
							case StreamPlayer.STATUS_INITIALIZING:
								status = "STATUS_INITIALIZING";
								break;

							case StreamPlayer.STATUS_PLAY_INITIALIZING:
								status = "STATUS_PLAY_INITIALIZING";
								break;

							case StreamPlayer.STATUS_PLAYING:
								status = "STATUS_PLAYING";
								break;

							case StreamPlayer.STATUS_READY:
								status = "STATUS_READY";
								break;

							case StreamPlayer.STATUS_UNINITIALIZED:
								status = "STATUS_UNINITIALIZED";
								break;

							default:
								status = "UNKNOWN";
								break;
							}
							Toast toast = Toast.makeText(MiniAndroidPlayer.this, "StreamPlayer: "+ status, Toast.LENGTH_LONG);
							toast.show();
						}
					});
					currentStatus = mStreamPlayer.getStatus();
				}	
			}
			myHandler.postDelayed(probeStreamPlayer, 500);
		}
	};
	
	@Override
	public void onBackPressed() {
		shutdown();
	}
	@Override
    public void onPause() {
		super.onPause();
		shutdown();
	}
	private void shutdown(){
		if (mMediaPlayer != null)
			mMediaPlayer.release();

		if( mStreamPlayer != null)
			mStreamPlayer.close(null);
		
		myHandler.removeCallbacks(probeStreamPlayer);
		OctoStatic.terminate(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		});
	}
}