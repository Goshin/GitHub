package com.octoshape.android.demo.players;

import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.android.listeners.MediaPlayerListener;
import octoshape.osa2.listeners.ProblemListener;

import com.octoshape.android.demo.Stream;
import com.octoshape.android.demo.StreamListener;
import com.octoshape.android.client.OctoStatic;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class OctoVideoView extends SurfaceView implements
		OnVideoSizeChangedListener {

	public static String LOGTAG = "OctoAndroidView";
	
	private int measuredWidth, measuredHeight;
	private int fullscreenWidth, fullscreenHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceHolder mHolder;
	private Context mContext;
	private Stream stream;
	private MediaControllerView controller;
	private int videoHeight;
	private int videoWidth;
	private boolean fullscreen;
	protected boolean live;
	Handler uiHandler;

	public OctoVideoView(Context context) {
		super(context);
		mContext = context;
		initMediaPlayer();
	}

	public OctoVideoView(Context context, AttributeSet attr) {
		super(context, attr);
		mContext = context;
		initMediaPlayer();
	}

	public OctoVideoView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		mContext = context;
		initMediaPlayer();
	}

	public void setProblemListener(final ProblemListener problemListener) {
		if (mMediaPlayer != null)
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					problemListener.gotProblem(OctoStatic
							.generateMediaPlayerProblem(what, extra));
					return true;
				}
			});
	}

	@SuppressWarnings("deprecation")
	private void initMediaPlayer() {

		uiHandler = new Handler(mContext.getMainLooper());
		mHolder = getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mHolder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.v(LOGTAG, "surfaceDestroyed");
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Log.v(LOGTAG, "surfaceCreated");
				mMediaPlayer.setDisplay(holder);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				Log.v(LOGTAG, "surfaceChanged");
			}
		});

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);

		this.fullscreenWidth = metrics.widthPixels;
		this.fullscreenHeight = metrics.heightPixels;

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnVideoSizeChangedListener(OctoVideoView.this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN && controller != null) {
			if (controller.isShown())
				controller.setVisibility(View.GONE);
			else
				controller.setVisibility(View.VISIBLE);
		}
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (!fullscreen) {
			this.measuredWidth = getDefaultSize(0, widthMeasureSpec);
			this.measuredHeight = getDefaultSize(0, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		fixAspectRatio();
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		videoWidth = width;
		videoHeight = height;
		fixAspectRatio();
	}

	private void fixAspectRatio() {

		if (videoWidth <= 0 && videoHeight <= 0)
			return;

		float ratio_width, ratio_height, aspectratio;
		int width, height;

		if (fullscreen) {
			width = fullscreenWidth;
			height = fullscreenHeight;
		} else {
			width = measuredWidth;
			height = measuredHeight;
		}
		ratio_width = (float) width / videoWidth;
		ratio_height = (float) height / videoHeight;
		aspectratio = (float) videoWidth / videoHeight;

		LayoutParams layoutParams = getLayoutParams();

		if (ratio_width > ratio_height) {
			layoutParams.width = (int) (height * aspectratio);
			layoutParams.height = height;
		} else {
			layoutParams.width = width;
			layoutParams.height = (int) (width / aspectratio);
		}
		setLayoutParams(layoutParams);
	}

	public void setController(MediaControllerView mControlView) {
		this.controller = mControlView;
	}

	public void setFullscreen(boolean isFullscreen) {
		this.fullscreen = isFullscreen;
		fixAspectRatio();
	}

	public void playStream(OctoshapeSystem os, Stream currentStream,
			ProblemListener mProblemListener) {

		stream = currentStream;
		stream.playback(os, mProblemListener, new StreamListener() {

			@Override
			public void onPrepared(Uri url, MediaPlayerListener mpListener) {
				playUrl(url, mpListener);
			}
			@Override
			public void onInit(final boolean osaSeek, boolean nativeSeek,
					final boolean isLive, final long duration) {

				live = isLive;
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						controller.initController(stream, mMediaPlayer);
						if (isLive && osaSeek)
							controller.updateSeekBarDVR();
					}
				});
			}

			@Override
			public void DVRSeekOffsetUpdate(final int offSet) {
				uiHandler.post(new Runnable() {
					@Override
					public void run() {
						controller.updateSeekBarDVR();
					}
				});
			}
		});
	}

	private void playUrl(final Uri url, final MediaPlayerListener mpListener) {

		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				if( mMediaPlayer != null ) {
					try {
						if( isPlaying() )
							mMediaPlayer.stop();
					} catch ( IllegalStateException ill ) {
						Log.d(LOGTAG, "Can't stop mediaplayer right now " + ill.getMessage() );
					}
					try {
						mMediaPlayer.reset();
					} catch ( IllegalStateException ill ) {
						Log.d(LOGTAG, "Can't reset mediaplayer right now " + ill.getMessage() );
					}
					
					try {
						mMediaPlayer.release();
					} catch ( IllegalStateException ill ) {
						Log.d(LOGTAG, "Can't release mediaplayer right now " + ill.getMessage() );
					}
					mMediaPlayer = null;
				}				
				mMediaPlayer = MediaPlayer.create(getContext(), url, mHolder);
				controller.setMediaPlayer(mMediaPlayer);
				
				try {
					mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mpListener.onMediaPlaybackCompleted();
						}
					});
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
						mMediaPlayer.setOnInfoListener(new OnInfoListener() {
							@Override
							public boolean onInfo(MediaPlayer mp, int what,
									int extra) {
								if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
									Log.d("MediaPlayer", "START RENDERING");
									mpListener.onMediaPlaybackStarted();
								}
								return false;
							}
						});

					mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mp) {
							{
								Log.d("MediaPlayer","PLAYERBACK STARTED");
								uiHandler.post(new Runnable() {
									@Override
									public void run() {
										mMediaPlayer.start();
									}
								});
								if (!live)
									controller.start();
								if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
									mpListener.onMediaPlaybackStarted();
							}
						}
					});
				} catch (Exception e) {
					Log.e(LOGTAG, "Error preparing MediaPlayer", e);
				}
			}
		});
	}
	public boolean isPlaying(){
		try{
			return (mMediaPlayer!= null && mMediaPlayer.isPlaying());
		}catch (IllegalStateException e){
			Log.i(LOGTAG, "IsPlaying got exception " + e.getMessage() + ". Will return false.");
			return false;
		}
	}
	public void reset() {
		if(isPlaying()){
			mMediaPlayer.stop();
			try {
				mMediaPlayer.release();
			} catch (IllegalStateException e){
				Log.i(LOGTAG, "reset got exception " + e.getMessage() );
			}
			
			mMediaPlayer = null;
		}
		if (controller != null)
			controller.stop();
	}
}