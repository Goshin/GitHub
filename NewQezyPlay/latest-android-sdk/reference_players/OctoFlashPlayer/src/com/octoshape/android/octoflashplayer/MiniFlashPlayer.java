package com.octoshape.android.octoflashplayer;

import java.util.LinkedList;

import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.android.StreamPlayer;
import octoshape.osa2.android.listeners.StreamPlayerListener;
import octoshape.osa2.android.listeners.MediaPlayerListener;
import octoshape.osa2.Problem;
import octoshape.osa2.listeners.ProblemListener;

import com.octoshape.android.client.OctoshapePortListener;
import com.octoshape.android.client.OctoStatic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MiniFlashPlayer extends Activity {
    
	private static final String LOGTAG = "MINIFLASHPLAYER";
	private static String OCTOLINK = "octoshape://streams.octoshape.net/demo/live/nascar/500";
	private StreamPlayer mStream;
	private WebView mWebView;
	private OctoshapeSystem os;
	private MediaPlayerListener playerListener = null;
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.video);
		
		mWebView = (WebView) findViewById(R.id.surface);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.addJavascriptInterface(new JavaScriptInterface(), "ANDROID");
		mWebView.setWebChromeClient(new WebChromeClient() {
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d(LOGTAG , cm.message() + " -- From line " + cm.lineNumber()
						+ " of " + cm.sourceId());
				return true;
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		initOctoshapeSystem();
	}
    /**
     * Creating OctoshapeSystem, adding ProblemListener for which we set to the language of the
     * OS and setting OctoshapePortListener triggering a callback once 
     * the Octoshape service/client has started successfully.	
     */
    public void initOctoshapeSystem(){
		
    	// Create OctoshapeSystem
		os = OctoStatic.create(this, problemListener, new OctoshapePortListener() {
			
			// called once the Octoshape service/client has started.		
			@Override
			public void onPortBound(String ip, int port) {
						mStream = setupStream(OCTOLINK);
						mWebView.loadUrl("http://localhost:" + port	+ "/player.html");
					}
				});
		// We set ProblemListener
				
		// Adding FlashPlayer
		try {
			String flashVersion = this.getPackageManager().getPackageInfo(
					"com.adobe.flashplayer", 0).versionName;
			Log.d("OCTOPLAYER", "Flash is installed (version: " + flashVersion + ")");
			os.addPlayerNameAndVersion(OctoshapeSystem.MEDIA_PLAYER_FLASH, OctoshapeSystem.MEDIA_PLAYER_FLASH, "" + flashVersion);
		} catch (NameNotFoundException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("This application requires Adobe Flash Player. This applciation" +
					"will now terminate")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   MiniFlashPlayer.this.finish();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show(); 
			return;
		}
		// Launching OctoshapeSystem
		os.open();
    }
    /**
     * Adding a stream to the OctoMediaPlayer. Creates an StreamPlayer the
     * StreamPlayer instance is initiated with it's own UrlListener and
     * ProblemListener.
     * 
     * @param stream link (e.g., octoshape://ond.octoshape.com/demo/ios/bbb.mp4)
     * @return StreamPlayer on which we can request playback.
     */
    public StreamPlayer setupStream(final String stream) {

		Log.d(LOGTAG, "Setting up stream: " + stream);
		StreamPlayer sp = os.createStreamPlayer(stream);
		sp.setProblemListener(problemListener);
		sp.setListener(new StreamPlayerListener() {
			private String playerId;
			
			/**
			 * Receiving new URL from the streamplayer object either due to
			 * requesting playback, seeking or experiencing a bitrate/resolution
			 * changes requiring the re-initialization of the Player.
			 * 
			 * @param url
			 *            to be passed to the media player
			 * @param seekOffset
			 *            offset we have seek to in milliseconds
			 * @param playAfterBuffer
			 *            if true the URL should be added to a list and played
			 *            upon completion of the current URL.
			 */
			@Override
			public void gotUrl(String url, long seekOffset, MediaPlayerListener mpListener) {
				playerListener = mpListener;
				Log.i(LOGTAG, "gotUrl");
				playStream(Uri.parse(url), playerId);
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
				Log.i(LOGTAG, "resolvedNativeSeek");
				this.playerId = playerId;
			}

			/**
			 * Called if it is not possible to seek in the stream.
			 */
			@Override
			public void resolvedNoSeek(boolean isLive, String playerId) {
				Log.i(LOGTAG, "resolvedNoSeek");
				this.playerId = playerId;
			}

			@Override
			/**
			 * Called when stream support OsaSeek / DVR
			 */
			public void resolvedOsaSeek(boolean isLive, long duration,
					String playerId) {
				Log.i(LOGTAG, "resolvedOsaSeek");
				this.playerId = playerId;
			}
		});
		sp.initialize(false);
		return sp;
	}
    protected void playStream(Uri mediaUrl, final String playerId) {
		Log.d(LOGTAG, playerId + " now plays: "+mediaUrl);
		mWebView.loadUrl("javascript:start('"+mediaUrl.toString()+"')");
	}
    
    
    ProblemListener problemListener = new ProblemListener() {
		@Override
		public void gotProblem(Problem p) {
			Log.d(LOGTAG, "Problem: "+p.toString());
			if(p.getMessage()!=null)
				error("Problem:" + p.getMessage());
		}
	};
	protected void error(final String error) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(MiniFlashPlayer.this, error, Toast.LENGTH_LONG);
				toast.show();
			}
		});
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
		if (mWebView != null)
			mWebView.destroy();
		
		if ( mStream != null)
			mStream.close(null);
		
		OctoStatic.terminate(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		});
	}
	
	class JavaScriptInterface {

		// CALLBACKS FROM JAVA-SCRIPT
		public void showToast(String toast) {
			Toast.makeText(MiniFlashPlayer.this, toast, Toast.LENGTH_SHORT).show();
		}
		public void onStop() {
			// TODO
		}
		public void onCompletion() {
			if(playerListener != null)
				playerListener.onMediaPlaybackCompleted();
		}
		public void onDuration(int duration) {
			// TODO
		}
		public void onCurrent(int current) {
			// TODO
		}
		public void onDebug(String dbg) {
			Log.d("ACTIONSCRIPT", dbg);
		}
		public void onPlay(){
			if(playerListener != null)
				playerListener.onMediaPlaybackStarted();
		}
		public void onPause(){
			// TODO
		}
		public void onError(String error){
			Log.e(LOGTAG, "SWF Player Error: " +error);
		}
		public void onPlayerReady(){
			runOnUiThread(new Runnable() {
				public void run() {
					mWebView.loadUrl("javascript:enableDebug('true')");
					mStream.requestPlay();
				}
			});
		}
	}
}