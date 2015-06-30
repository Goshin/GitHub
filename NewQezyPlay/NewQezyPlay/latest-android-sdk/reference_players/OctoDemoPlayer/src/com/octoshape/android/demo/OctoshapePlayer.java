package com.octoshape.android.demo;

import octoshape.osa2.Problem;
import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.android.StreamPlayer;
import octoshape.osa2.listeners.OctoshapeSystemListener;
import octoshape.osa2.listeners.ProblemListener;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.octoshape.android.client.OctoStatic;
import com.octoshape.android.demo.players.MediaControllerView;
import com.octoshape.android.demo.players.OctoVideoView;

public class OctoshapePlayer extends Activity implements OctoshapeSystemListener, OnItemClickListener{

	private ListView mListView;
	private StreamAdapter mStreamAdapter;
	private OctoshapeSystem os;
	private Stream currentStream;
	protected ImageButton mFullScreenButton;
	protected MediaControllerView mControlView;
	protected OctoVideoView mVideoView;
	protected ImageView mImageView;
	protected byte currentStatus;
	protected Handler myHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);
		myHandler = new Handler();
		
		// Initialize Octoshape
		OctoStatic.enableLog(true, null);
		os = OctoStatic.create(this, mProblemListener, null);
		os.setOctoshapeSystemListener(this);
		
		// Add available players
		os.addPlayerNameAndVersion(OctoshapeSystem.MEDIA_PLAYER_NATIVE,
				OctoshapeSystem.MEDIA_PLAYER_NATIVE, "" + Build.VERSION.SDK_INT );
		
		os.open();
	}
	Runnable probeStreamPlayer = new Runnable() {
		
		@Override
		public void run() {
			if(currentStream != null && currentStream.getStreamPlayer()!= null){
				if(currentStream.getStreamPlayer().getStatus() != currentStatus){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
						
							String status;
							switch (currentStream.getStreamPlayer().getStatus()) {
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
							Toast toast = Toast.makeText(OctoshapePlayer.this, "StreamPlayer: "+ status, Toast.LENGTH_LONG);
							toast.show();
						}
					});
					currentStatus = currentStream.getStreamPlayer().getStatus();
				}	
			}
			myHandler.postDelayed(probeStreamPlayer, 500);
		}
	};
	
	@Override
	public void onConnect(String authID) {
		Log.d(OctoshapeApplication.LOGTAG, "Connected to Octoshape system");
		// Connected to Octoshape Client, so initialize GUI
		runOnUiThread(new Runnable() {
			public void run() {
				
				// Initialize Stream List View
				mListView = (ListView) findViewById(R.id.streamListView);
				mListView.setTextFilterEnabled(true);
				mStreamAdapter = new StreamAdapter(OctoshapePlayer.this, R.layout.stream_list, OctoshapeApplication.getPlayList());
				mListView.setAdapter(mStreamAdapter);
				mListView.setOnItemClickListener(OctoshapePlayer.this);
			
				// Initialize Controll View
				mControlView = (MediaControllerView) findViewById(R.id.controlLayout);
				mVideoView = (OctoVideoView) findViewById(R.id.videoView);
				mVideoView.setProblemListener(mProblemListener);
				mImageView = (ImageView) findViewById(R.id.channel_image);
				
				mFullScreenButton = (ImageButton) findViewById(R.id.button_fullscreen);
				mFullScreenButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(mVideoView.isShown()){
							if(mListView.isShown()){
								mListView.setVisibility(View.GONE);
								mVideoView.setFullscreen(true);
							}
							else{
								mListView.setVisibility(View.VISIBLE);
								mVideoView.setFullscreen(false);
							}
							if(mControlView.isShown())
								mControlView.setVisibility(View.GONE);
							else
								mControlView.setVisibility(View.VISIBLE);
						}
					}
						
				});
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.demo_player, menu);
		return true;
	}
	
	private ProblemListener mProblemListener = new ProblemListener() {
		public void gotProblem(final Problem p) {
			Log.d(OctoshapeApplication.LOGTAG, "Problem: " + p.toString());
			if(p.getMessage()!=null)
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						Toast toast = Toast.makeText(OctoshapePlayer.this, "Problem: "+p.getMessage(),
								Toast.LENGTH_LONG);
						toast.show();
					}
				});
		}
	};
	
	// A Stream was selected
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		myHandler.removeCallbacks(probeStreamPlayer);
		myHandler.post(probeStreamPlayer);
		if( currentStream != null)
			currentStream.close();
		
		currentStream = OctoshapeApplication.getPlayList().get(position);
		Log.d(OctoshapeApplication.LOGTAG, "Requesting stream: " + currentStream.getName());
		mImageView.setVisibility(View.GONE);
		mVideoView.reset();
		mVideoView.setVisibility(View.VISIBLE);
		mVideoView.setController(mControlView);
		mVideoView.playStream(os, currentStream, mProblemListener);
	}
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
		if (mVideoView != null)
			mVideoView.reset();
		if( currentStream != null)
			currentStream.close();
		myHandler.removeCallbacks(probeStreamPlayer);
		OctoStatic.terminate(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		});
	}
}
