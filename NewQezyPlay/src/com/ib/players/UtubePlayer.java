package com.ib.players;
 
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.ib.dialogboxes.NeutralDialog;
import com.ib.qezyplay.UserMainScreen;
 
public class  UtubePlayer implements YouTubePlayer.OnInitializedListener {

	private static final String TAG = null;
	Context context = null ;
	String VideoId= null;



	public UtubePlayer(Context context ) {
		context=this.context;		 
	}

	public void  playYoutube(String API_KEY, String a){
		VideoId = a;
		Log.e(TAG," $$$$$$$$$$$$$$$$$$$ YoutubeStreams ***********  "  +VideoId);	
		UserMainScreen.youTubePlayerView.initialize(API_KEY,this);
	}


	@Override
	public void onInitializationFailure(Provider arg0,YouTubeInitializationResult arg1) {
	  	Toast.makeText(context, "Failured to Initialize!", Toast.LENGTH_LONG).show();
	}


	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {

		/** add listeners to YouTubePlayer instance **/
		player.setPlayerStateChangeListener(playerStateChangeListener);
		player.setPlaybackEventListener(playbackEventListener);
		player.setPlayerStyle(PlayerStyle.CHROMELESS);

		/** Start buffering **/
		if (!wasRestored) {
			player.cueVideo(VideoId);
		}
	}

	private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

		@Override
		public void onBuffering(boolean arg0) {
		}

		@Override
		public void onPaused() {
		}

		@Override
		public void onPlaying() {
		}

		@Override
		public void onSeekTo(int arg0) {
		}

		@Override
		public void onStopped() {
		}

	};

	private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {

		@Override
		public void onAdStarted() {

		}

		@Override
		public void onError(ErrorReason arg0) {


			NeutralDialog neutralDialog = new NeutralDialog(context); 		 

			neutralDialog.displayAlertMessage("Alert","Stream Not Available");
			UserMainScreen.spinner.setVisibility(View.GONE);

		}
		@Override
		public void onLoaded(String arg0) {
		}

		@Override
		public void onLoading() {
		}

		@Override
		public void onVideoEnded() {
		}

		@Override
		public void onVideoStarted() {
			UserMainScreen.spinner.setVisibility(View.GONE);
		}
	};

}


