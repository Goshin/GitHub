package com.octoshape.android.demo.players;

import com.octoshape.android.demo.R;
import com.octoshape.android.demo.Stream;
import com.octoshape.android.demo.util.Misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.os.Build;

public class MediaControllerView extends RelativeLayout {

	private final static String LOGTAG = "MediaControllerView";
	private Paint innerPaint;
	private Paint borderPaint;
	private SeekBar mSeekBar;
	private TextView mTimer;
	private Context context;
	private TextView mMaxDVR;
	private Stream mStream;
	private MediaPlayer mMediaPlayer;
	private Handler mHandler = new Handler();
	private long maxDVR;
	private ImageButton mPauseButton;
	
	public MediaControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	private void init() {
		innerPaint = new Paint();
		innerPaint.setARGB(225, 75, 75, 75); // gray
		innerPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setARGB(225, 233, 33, 33);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);

		LayoutInflater layoutInflater = LayoutInflater.from(context);
	    layoutInflater.inflate(R.layout.media_controller, this);
		
	    mPauseButton = (ImageButton) findViewById(R.id.button_pause);
		mSeekBar = (SeekBar) findViewById(R.id.seek);
		mTimer = (TextView) findViewById(R.id.textTimer);
		mMaxDVR = (TextView) findViewById(R.id.textDVR);
	}

	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}

	public void setBorderPaint(Paint borderPaint) {
		this.borderPaint = borderPaint;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		RectF drawRect = new RectF();
		drawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		super.dispatchDraw(canvas);
	}

	private void updateSeekBar(int duration, int position,
			boolean enableSeeking, boolean isLive) {

		if (!isLive){
			mTimer.setText(Misc.millisToTimeString(position) + " / "
					+ Misc.millisToTimeString(duration));
		
			if (enableSeeking) {
				mSeekBar.setMax(duration);
				mSeekBar.setProgress(position);
			}
		}
		else{
			if(enableSeeking){
				mMaxDVR.setText(Misc.millisToTimeString(duration));
				mSeekBar.setMax(duration);
				mSeekBar.setProgress(position);
			}
			if(position != duration)
				mTimer.setText(Misc.millisToTimeString(position));
			else
				mTimer.setText("LIVE");
		}
	}
	public void addListener(OnSeekBarChangeListener listener){
		mSeekBar.setOnSeekBarChangeListener(listener);
	}
	public void initController (Stream stream, MediaPlayer mediaPlayer){
		
		this.mStream = stream;
				
		mHandler.removeCallbacks(seekBarUpdateOperation);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(mStream.isNativeSeek()) {
					try {
						mMediaPlayer.seekTo(seekBar.getProgress());
					} catch ( IllegalStateException ill ) {
						Log.i(LOGTAG, "Ignoring an attempt to try to seek. " + ill.getMessage() );
					}
				}
				else if(mStream.isOsaSeek()){
					mHandler.removeCallbacks(seekBarUpdateOperation);
					try {
						mMediaPlayer.reset();
					} catch ( IllegalStateException ill ) {
						Log.i(LOGTAG, "Ignoring an attempt to reset. " + ill.getMessage() );
					}
					if(!mStream.isLive() )
						mStream.getStreamPlayer().requestPlayOndemandMediaTimePosition(seekBar.getProgress());
					else{
						mStream.setCurrentDVR((int)maxDVR - seekBar.getProgress());
						mStream.getStreamPlayer().requestPlayLiveWithLatency(maxDVR - seekBar.getProgress());
					}	
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser)
					updateSeekBar(seekBar.getMax(), progress, false, mStream.isLive());
			}
		});
		if(mStream.isLive())
			mPauseButton.setVisibility(View.GONE);
		else
			mPauseButton.setVisibility(View.VISIBLE);
		
		mPauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mMediaPlayer.isPlaying()){
					mMediaPlayer.pause();
					mPauseButton.setImageResource(R.drawable.button_play);
				}
				else{
					mMediaPlayer.start();
					mPauseButton.setImageResource(R.drawable.button_pause);
				}
			}
		});
		
	}
	private Runnable seekBarUpdateOperation = new Runnable() {
		public void run() {
			boolean isPlaying = false;
			int position = 0;
			int duration = 0;
			if( mMediaPlayer != null ) {
				try {
					isPlaying = mMediaPlayer.isPlaying();
				} catch ( IllegalStateException ill ) {
					// It is not documented that isPlaying may throw an exception, but it happens
					isPlaying = false;
					Log.i(LOGTAG, "We can't check isPlaying now. " + ill.getMessage() );
				}
			}
			if ( isPlaying ){
				try {
					position = mMediaPlayer.getCurrentPosition();
					duration = mMediaPlayer.getDuration();
				} catch ( IllegalStateException ill ) {
					// It is not documented that getCurrentPosition may throw an exception, but it happens
					isPlaying = false;
					Log.i(LOGTAG, "We can't get the current position or duration now. " + ill.getMessage() );
				}
			}
			
			if( isPlaying )
				if(mStream.isNativeSeek())
					updateSeekBar(duration, position, true, false);
				else
					// If KITKAT (4.4) or higher and not 4.4.1 or 4.4.2 Android uses 
					// the PTS (Presentation Time Stamp) values in the transport stream 
					// to determine playback in the stream see (redmine #9151).
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !(Build.VERSION.RELEASE.equals("4.4.2") || Build.VERSION.RELEASE.equals("4.4.1")))
						updateSeekBar((int)mStream.getOsaDuration(),(int) (position), mStream.isOsaSeek(), false);
					else
						updateSeekBar((int)mStream.getOsaDuration(),(int) (position + mStream.getOsaSeekOffset()), mStream.isOsaSeek(), false);
			
			mHandler.postDelayed(seekBarUpdateOperation, 500);
		}
	};
	
	public void updateSeekBarDVR() {
		this.maxDVR  = mStream.getMaxDVR();
		updateSeekBar((int)maxDVR, (int)maxDVR - (int)mStream.getCurrentDVR(), maxDVR > 0, true);
	}

	public void start() {

		if(mStream.isNativeSeek() || mStream.isOsaSeek()){
			mHandler.post(seekBarUpdateOperation);
			mSeekBar.setVisibility(View.VISIBLE);
		}
		else
			mSeekBar.setVisibility(View.INVISIBLE);
			
	}

	public void stop() {
		mHandler.removeCallbacks(seekBarUpdateOperation);
	}

	public void reset() {
		// TODO Auto-generated method stub
	}

	public void setMediaPlayer(MediaPlayer mMediaPlayer) {
		this.mMediaPlayer = mMediaPlayer;
	}
}
