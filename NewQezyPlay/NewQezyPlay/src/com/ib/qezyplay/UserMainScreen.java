package com.ib.qezyplay;


import io.vov.vitamio.LibsChecker;
//import io.vov.vitamio.widget.VideoView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import octoshape.osa2.Problem;
import octoshape.osa2.listeners.ProblemListener; 

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ib.services.MainScreen;

import android.annotation.SuppressLint;
import android.app.ActionBar; 
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener; 
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;  
import android.util.DisplayMetrics;
import android.util.Log; 
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView; 
import android.view.View.OnClickListener; 
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;  
import android.widget.ImageView;
import android.widget.ProgressBar; 
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast; 

import com.ib.displayadapters.Adapterlist; 
import com.ib.listeners.ChannelListener;
import com.ib.listeners.CountryCategoryListeners;
import com.ib.listeners.FavoritesChannelListener;
import com.ib.listeners.RecentlyViewedChannelListener;
import com.ib.menuOptions.UserImageList;
import com.ib.players.PlayStreams;
import com.ib.players.VideoControllerView; 
import com.ib.players.PlayStreams.IPlayStreamCallBack;
import com.ib.qezyplay.Utils.HorizontalListView; 
import com.ib.qezyplay.Utils.NetworkConnectivity;
import com.ib.dialogboxes.AddToFavorites;
import com.ib.dialogboxes.NeutralDialog;
import com.ib.dialogboxes.ShowOptions;  

import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.android.StreamPlayer;
import octoshape.osa2.listeners.OctoshapeSystemListener; 
import octoshape.osa2.android.listeners.MediaPlayerListener;
import octoshape.osa2.android.listeners.StreamPlayerListener;

import com.octoshape.android.client.OctoStatic;

import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.provider.Settings.Secure;

import com.ib.displayadapters.CategoryAdapter;
import com.ib.displayadapters.CountryAdapter;
/**
 *  UserMainScreen - Activity that 
 *  
 * @author Ideabytes
 * @version 1.0.0
 */
@SuppressLint("InflateParams")
public class UserMainScreen extends YouTubeBaseActivity   implements SurfaceHolder.Callback,   MediaPlayer.OnPreparedListener, ProblemListener, OnClickListener, OctoshapeSystemListener, OnVideoSizeChangedListener, VideoControllerView.MediaPlayerControl, OnTouchListener,IPlayStreamCallBack   {

	public static final String TAG = null;
	public static String OCTOLINK = null;
	public static	HorizontalListView ChannelListView,CountryListView,CategoryListView,FavoritesHorizontalListView,RecentlyViewedHorizontalListView ;
	public static String UniqueDeviceId = null;
	public static FrameLayout  PreviewFrame;
	public static FrameLayout PreviewFrameRtmp;
	private TextView  Copy_Right_Text;
	private ImageButton OptionsListImage;	 
	public static   TextView OptionDisplayButton;
	public static	String[] Options = {"Recently Viewed","Favorites"};
	public static  String CurrentClickedChannelId = null;
	public static String CurrentClickedChannelName = null;

	/** 
	 * Youtube Variables  
	 */
	public static final String API_KEY = "AIzaSyCUxraW3TbAL3MBdRDIeo_-7AI60tIj-XA";
	public static YouTubePlayerView youTubePlayerView; 

	/**
	 * Octoshape Variables
	 */
	private static String StreamStatus;
	private CachePackageDataObserver mClearCacheObserver; 
	private static String active;
	private static final long CACHE_APP = Long.MAX_VALUE;	 
	private static final String LOGTAG = "OctoAndroidPlayer";
	private SurfaceView mSurface;
	private SurfaceHolder mHolder;
	public  OctoshapeSystem os;
	private MediaPlayer mMediaPlayer;
	private StreamPlayer mStreamPlayer;
	protected byte currentStatus;
	protected Handler myHandler;
	public  static VideoControllerView controller;
	public static  ProgressBar spinner;
	public static Boolean ChannelRecentlyViewed = false;
	public static ImageView  CountriesImage,CategoriesImage;
	public static String CurrentClickedChannelLink = null;
	public Boolean isFullScreen = false; 
	//public static VideoView mVideoViewRtmp;
	/**
	 * Full Screen Varaibles
	 */
	private int widthPixels =  0;
	private int heightPixels = 0;
	private  float widthDpi= 0;
	private  float heightDpi = 0;
	private  float widthInches =0;
	private  float heightInches = 0;
	private double diagonalInches  = 0.0;

	/**
	 * Action Bar Variables
	 */
	public EditText SearchChannels;
	private SearchView ChannelSearchView;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
 
		 //TODO: Uncheck if RTMP is Implemented 
		
//		if (!LibsChecker.checkVitamioLibs(this))
//			return;

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getIntent();
		UniqueDeviceId =  Secure.getString(this.getContentResolver(),
				 				Secure.ANDROID_ID); ;
		
		setContentView(R.layout.user_main_screen);
		/**
		 * Get the screen size 
		 */
		
		getScreenSize();
 
		/**
		 * Making the Service call for all the Countries,Categories and Channels 
		 */
		getCountriesCategoriesChannels(UniqueDeviceId);

		/**
		 * Enabling the Action Bar that contains CheckBoxes, Search and User List View 
		 */
		enableActionBar();
 
		/** 
		 * Initializing the Variables and Widgets 
		 */
		setUpVariables();

		 
		/**
		 * Horizontal list views for Countries,Categories and Channels
		 */
		setUpHorizontalListViews();
	}	

	/**
	 * @param null
	 * @return void
	 * Initializing Horizontal List Views for Country Category  and Channels
	 */
	private void setUpHorizontalListViews() {

		CountryCategoryListeners channelCategoryListeners = new CountryCategoryListeners(UserMainScreen.this);
		Adapterlist adapterList = new Adapterlist(getApplicationContext());

		CategoryAdapter categoryAdapter = new CategoryAdapter(UserMainScreen.this);
		CountryAdapter  countryAdapter = new CountryAdapter(UserMainScreen.this);

		CategoryListView = (HorizontalListView) findViewById(R.id.categories_listview);		 
		CategoryListView.setOnItemClickListener(channelCategoryListeners.categoryListItemListener);
		CategoryListView.setAdapter(categoryAdapter); 

		CountryListView = (HorizontalListView) findViewById(R.id.countries_listview);
		CountryListView.setOnItemClickListener(channelCategoryListeners.countryListItemListener);
		CountryListView .setAdapter(countryAdapter);

		ChannelListView = (HorizontalListView) findViewById(R.id.Channel_listview);
		ChannelListView.setAdapter(adapterList.ChannelAdapter);
		ChannelListener channelListener = new ChannelListener(UserMainScreen.this);
		ChannelListView.setOnItemClickListener( channelListener );

		FavoritesHorizontalListView = (HorizontalListView) findViewById(R.id.favorites_horizontal_list_view);
		FavoritesChannelListener favChannelListener = new FavoritesChannelListener(UserMainScreen.this);
		FavoritesHorizontalListView.setOnItemClickListener(favChannelListener);

		RecentlyViewedHorizontalListView = (HorizontalListView) findViewById(R.id.recently_viewed_horizontal_list_view);
		RecentlyViewedChannelListener recentlyViewedChannelListener  = new RecentlyViewedChannelListener(UserMainScreen.this); 
		RecentlyViewedHorizontalListView.setOnItemClickListener(recentlyViewedChannelListener);
		RecentlyViewed  recentyViewed = new RecentlyViewed(UserMainScreen.this);

		if(OptionDisplayButton.getText() == "Recently Viewed"){
			recentyViewed.getUpdatedRecentlyViewedChannel(UniqueDeviceId);
			FavoritesHorizontalListView.setVisibility(View.GONE);
			RecentlyViewedHorizontalListView.setVisibility(View.VISIBLE);
			RecentlyViewedHorizontalListView.setAdapter(Adapterlist.RecentlyViewedAdapter);
		}
	}

	/**
	 * @param null
	 * @return void
	 * 
	 *  Enabling custom Action Bar that contains Check Boxes (Live and VOD'S) ,Search and User List View
	 */

	private void enableActionBar() {

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);
		ImageView userImage = (ImageView)mCustomView.findViewById(R.id.imageButton);
		UserImageList userImageList = new UserImageList(UserMainScreen.this);
		userImage.setOnClickListener(userImageList);
		ChannelSearchView = (SearchView)mCustomView.findViewById(R.id.searchChannelView);
		ChannelSearchView.setQueryHint("Channel Search");


		ChannelSearchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				Log.e(TAG,"Set on Query Text Focus Changed  "+ String.valueOf(hasFocus));
				Toast.makeText(getApplicationContext(), String.valueOf(hasFocus), Toast.LENGTH_SHORT).show();

			}
		});

		ChannelSearchView.setOnQueryTextListener( new OnQueryTextListener() {

			/**
			 * Called after the search button is pressed in the soft keyBoard
			 */
			@Override
			public boolean onQueryTextSubmit(String query) {
				Log.e(TAG,"  On query text submit " +query);
				Boolean isChannelAvailable = false;
				for(int i = 0 ; i<MainScreen.ChannelName.size() ;i++)
				{
					/************ Searches the Queries(Channels ) if even the entry has no spaces   **********/
					String ChannelName = MainScreen.ChannelName.get(i).replaceAll("\\s+", "");
					; 
					if(ChannelName.equalsIgnoreCase(query) || (MainScreen.ChannelName.get(i).equalsIgnoreCase(query))  )
					{
						isChannelAvailable = true;
						PlayStreams playStreams = new PlayStreams(UserMainScreen.this);
						playStreams.PlayVideo(MainScreen.ChannelStatus.get(i),
								MainScreen.ContentType.get(i),
								MainScreen.ChannelID.get(i),
								MainScreen.ChannelName.get(i),
								MainScreen.VideoType.get(i),
								MainScreen.ChannelLink.get(i));	
					}
				}
				if(isChannelAvailable != true)
				{
					NeutralDialog  neutralDialog = new NeutralDialog(UserMainScreen.this);
					neutralDialog.displayAlertMessage("Alert","Channel Cannot be found ");
				}

				return false;
			}
			/**
			 * Called Everytime while user is Querying 
			 * i.e Till before the search button in the Soft KeyBoard is pressed
			 */

			@Override
			public boolean onQueryTextChange(String newText) {
				Log.e(TAG,"  On Query Text Change  " +newText);

				return false;
			}
		});
	}

	/**
	 * setUpVariables() - Initializing the Variables and Widgets
	 * @return void
	 * 
	 */
	private void setUpVariables() {
		if (diagonalInches >= 10) {
			Copy_Right_Text = (TextView)findViewById(R.id.copy_right_text);
		}
		PreviewFrame = (FrameLayout)findViewById(R.id.PreviewFrame);
		PreviewFrameRtmp = (FrameLayout)findViewById(R.id.PreviewFrameRtmp);
		PreviewFrame.setOnTouchListener(this);
		PreviewFrameRtmp.setOnTouchListener(this);
		spinner = (ProgressBar)findViewById(R.id.progressBar1);
		spinner.setVisibility(View.GONE);
		OptionsListImage = (ImageButton)findViewById(R.id.options);
		OptionsListImage.setOnClickListener(this);	
		OptionDisplayButton = (TextView)findViewById(R.id.options_btn);
		OptionDisplayButton.setText("Recently Viewed");
		ImageButton  AddFavorites = (ImageButton) findViewById(R.id.add_to_favorites);
		AddFavorites.setOnClickListener(this);
		myHandler = new Handler();
		controller = new VideoControllerView(this);	
		mSurface = (SurfaceView) findViewById(R.id.surface_main);
		mHolder = mSurface.getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);	
		myHandler =  new Handler(); 
		//TODO: Uncheck if RTMP is Implemented 
		//mVideoViewRtmp = (VideoView) findViewById(R.id.rtmp_player);


		PreviewFrameRtmp.setVisibility(View.GONE);

		/** Initializing YouTube player view **/
		//	youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_player);

	}

	/**
	 * getCountriesCategoriesChannels(String uniqueDevId)  -  Async task to make a service call to get all the 
	 *  Categories,Countries and Channels  on the Main Screen as sson its Launched 
	 * @param uniqueDevId
	 * 
	 */
	private void getCountriesCategoriesChannels(String uniqueDevId) {

		MainScreen mainScreen = new MainScreen(UserMainScreen.this); 
		try {
			mainScreen.execute(UniqueDeviceId).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 * @return void
	 * PlayRtmpStreams(String url) - Plays all the RTMP Streams Using Vitamio SDK
	 */
//	public void PlayRtmpStreams(String url)
//	{
//
//		PreviewFrameRtmp.setVisibility(View.VISIBLE);
//		spinner.setVisibility(View.GONE);
//
//		/* Resetting the Media Player for each Click  */
//		reset(); 
//
//		if(mMediaPlayer == null)
//			mMediaPlayer =  new MediaPlayer();
//
//		controller.setMediaPlayer(this);			 
//		controller.setAnchorView(PreviewFrameRtmp);	
//		mVideoViewRtmp.setVideoPath(url);
//		mVideoViewRtmp.requestFocus();
//		mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//			@Override
//			public void onPrepared(MediaPlayer mediaPlayer) {
//				// optional need Vitamio 4.0
//				//   mediaPlayer.setPlaybackSpeed(1.0f);
//			}
//		});
//	}

	/**
	 * @param HLS Channel Link 
	 * @return void
	 * Play HLS Streams.Sets the image  to invisible for the media player to play the video
	 * 
	 */
	public void playHlsStreams(String  HLSLink) {
		reset();
		if(mMediaPlayer == null)
			mMediaPlayer =  new MediaPlayer();

		try {
			controller.setMediaPlayer(this);			 
			controller.setAnchorView(PreviewFrame);
			mMediaPlayer.setDisplay(mHolder);			 
			mMediaPlayer.setDataSource( HLSLink);		
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
				mMediaPlayer.setOnInfoListener(new OnInfoListener() {
					@Override
					public boolean onInfo(MediaPlayer mp, int what, int extra) {

						return false;
					}
				});
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)	 	
				mMediaPlayer.setOnPreparedListener( new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {

					}

				});

			mMediaPlayer.setOnErrorListener( new  OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stubs
					displayError();

					return true;
				}
			});		 

			mMediaPlayer.prepare();	
			mMediaPlayer.start();	
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Log.e(TAG, "MediaPlayer  on completion" );
				}
			});


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	/**
	 * Reseting and Releasing the MediaPlayer on every click of the Channel
	 * Hiding the Controller for Every click of the Channel
	 */
	public void reset()
	{
		if(controller.isShown()){
			controller.hide();;
		}
		if( mMediaPlayer != null  &&  mMediaPlayer.isPlaying()){
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			try {
				mMediaPlayer.release();
			} catch (IllegalStateException e){
				Log.i(TAG, "reset got exception " + e.getMessage() );
			}				
			mMediaPlayer = null;
		}
	}

	/**
	 * initOctoshapeSystem() - Initializing  and Creating the Octoshape system 
	 * And Attaching the Android Media Player
	 * @return void 
	 */
	public void initOctoshapeSystem() {

		PreviewFrame.setVisibility(View.VISIBLE);

		// Create OctoshapeSystem
		os = OctoStatic.create(this, this, null);
		os.setOctoshapeSystemListener (this);

		// Adding AndroidMediaPlayer
		os.addPlayerNameAndVersion(OctoshapeSystem.MEDIA_PLAYER_NATIVE, OctoshapeSystem.MEDIA_PLAYER_NATIVE,"" + Build.VERSION.SDK_INT);
		os.open();
	}

	@Override
	public void onConnect(String authId) {			
		playOctoStreams();
	}

	/**
	 * playOctoStreams() - Setting up the Octoshape Streams 
	 * @return void
	 * 
	 */
	public void playOctoStreams(){

		;
		PreviewFrame.setVisibility(View.VISIBLE);
		myHandler.removeCallbacks(probeStreamPlayer);				
		myHandler.post(probeStreamPlayer);
		if(mStreamPlayer != null)
			mStreamPlayer.close(null);
		reset();
		mStreamPlayer = setupStream(OCTOLINK);
		mStreamPlayer.requestPlay();
	}

	/**
	 * Adding a stream to the OctoMediaPlayer. Creates an StreamPlayer the
	 * StreamPlayer instance is initiated with it's own UrlListener and
	 * ProblemListener.

	 * @param stream
	 *            link (e.g., octoshape://ond.octoshape.com/demo/ios/bbb.mp4)
	 * @return StreamPlayer on which we can request playback.
	 */
	public StreamPlayer setupStream(final String stream) {

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
				Log.e(LOGTAG, "Setting up stream:  3");
				Log.e(LOGTAG, "Setting up stream: " + stream);
			}

			/**
			 * Resets an on-demand file duration previously reported in
			 * resolvedOsaSeek(..) method
			 */
			@Override
			public void gotNewOnDemandStreamDuration(long duration) {
				// TODO:
				Log.e(LOGTAG, "Setting up stream:  4");

			}

			/**
			 * Called if the stream is seekable using the media player's own
			 * native seeking functionality (e.g., the Android MediaPlayer does
			 * the seeking for us).
			 */
			@Override
			public void resolvedNativeSeek(boolean isLive, String playerId) {
				// TODO:
				Log.e(LOGTAG, "Setting up stream:  5");
			}

			/**
			 * Called if it is not possible to seek in the stream.
			 */
			@Override
			public void resolvedNoSeek(boolean isLive, String playerId) {
				// TODO:
				Log.e(LOGTAG, "Setting up stream:  6");
			}

			@Override
			/**
			 * Called when stream support OsaSeek / DVR
			 */
			public void resolvedOsaSeek(boolean isLive, long duration, String playerId) {
				// TODO:
				Log.e(LOGTAG, "Setting up stream:  7");
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

		if(mMediaPlayer == null)
			mMediaPlayer =  new MediaPlayer();
		try {
			controller.setMediaPlayer(this);			 
			controller.setAnchorView(PreviewFrame);			 
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
							Log.e(LOGTAG, "Setting up stream:  15");

							Log.e(LOGTAG, "Setting up stream:  16");



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
						//Toast.makeText(getApplicationContext(), "videoStartocotshpe wait ended ", Toast.LENGTH_SHORT).show(); 

						mpl.onMediaPlaybackStarted();
					}
				});
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Log.e(LOGTAG, "MediaPlayer lalita liata  error:" + what + ":" + extra);
					UserMainScreen.this.gotProblem(OctoStatic.generateMediaPlayerProblem(what, extra));					 
					reset();
					return true;
				}
			});
			mMediaPlayer.prepare();
			mMediaPlayer.start();

		} catch (Exception e) {
			Log.e(LOGTAG, "Error preparing MediaPlayer", e);
			Toast.makeText(getApplicationContext(),"", Toast.LENGTH_SHORT).show();
			//displayError("Error preparing MediaPlayer: " + e.getMessage());
		}
	}

	/**
	 * Handling onVideoSizeChanged called by the MediaPlayer after initiating
	 * playback, scales video according to aspect ratio and display dimensions.
	 */
	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.e(LOGTAG, "Entered  Video Size changed");		 
		/***** set  Progress Bar Invisible after the video starts **/

		if(spinner.getVisibility() == View.VISIBLE){
			spinner.setVisibility(View.GONE);
		}
		/***** send the Recently Viewed ChannelId to the Server   **/
		RecentlyViewed recentlyViewed = new RecentlyViewed(UserMainScreen.this);
		recentlyViewed.PostRecentlyViewedChannel(UniqueDeviceId,CurrentClickedChannelId);

		if(UserMainScreen.OptionDisplayButton.getText() == "Recently Viewed")
		{
			// API SERVICE CALLS - get Recently Viewed Service Call
			recentlyViewed.getUpdatedRecentlyViewedChannel(UserMainScreen.UniqueDeviceId);							  						  
			//Display the updated List 
			FavoritesHorizontalListView.setVisibility(View.GONE);
			RecentlyViewedHorizontalListView.setVisibility(View.VISIBLE);
			RecentlyViewedHorizontalListView.setAdapter(Adapterlist.RecentlyViewedAdapter);
		}

		//FrameLayout.LayoutParams vLayout = (FrameLayout.LayoutParams) mSurface.getLayoutParams();
		//mSurface.setLayoutParams(vLayout);
		//dialog.dismiss();
		StreamStatus="Stream Ok";
		Log.i("Stream Stattus", "Stream Ok");
		active="true";
	}

	@Override
	public void gotProblem(final Problem p) {
		Log.e(LOGTAG, "Problem: "+p.toString());
		if(p.getMessage()!=null)
			//	displayError(p.getMessage() + "("+ p.getErrorCode() +")");
			displayError();

	}

	private void displayError() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//	dialog.dismiss();
				try {

					NetworkConnectivity networkConnectivity = new NetworkConnectivity(UserMainScreen.this);

					NeutralDialog neutralDialog = new NeutralDialog(UserMainScreen.this);

					if( networkConnectivity.isNetworkAvailable() == true){
						neutralDialog.displayAlertMessage("Alert","Stream Not Available");
						spinner.setVisibility(View.GONE);
						reset();


					}
					else  {				 

						neutralDialog.displayAlertMessage("Alert","Please Check your Internet connection");
						spinner.setVisibility(View.GONE);
						reset();


					}					  			
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}            
			}
		});
	}


	Runnable probeStreamPlayer = new Runnable() {

		@Override
		public void run() {
			if(mStreamPlayer != null ){
				if(mStreamPlayer.getStatus() != currentStatus){

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
						//						Toast toast = Toast.makeText( getApplicationContext(), "StreamPlayer1115555: "+ status, Toast.LENGTH_LONG);
						//						toast.show();

						break;
					}
				}

				currentStatus = mStreamPlayer.getStatus();
			}
			myHandler.postDelayed(probeStreamPlayer, 500);
		}
	};
	/**
	 * @return void 
	 * onBackPressed() - On Back Button Press on the Full screen - It should revert back to the Preview Screen 
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onBackPressed() {
		if(isFullScreen == true)
		{
			isFullScreen = false;
			getActionBar().show();
			if (diagonalInches >= 10) {
				Copy_Right_Text.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1900,1080);
				PreviewFrame.setLayoutParams(lp); 
			}
			else if (diagonalInches >= 6.7){

				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(700,350);
				PreviewFrame.setLayoutParams(lp); 
			}

		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//shutdown();
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
				finish();			}
		});
	}	

	/**
	 * Scheduler  for clearing the cache 
	 */
	class SayHello extends TimerTask {
		public void run() {
			System.out.println("Catche clear method scheduled");
			clearCache();
		}
	}
	/**
	 * @return void
	 * clearCache() - Clear the catch on equal intervals of time
	 */
	//TODO 
	void clearCache() 
	{
		Log.d(LOGTAG, "clearCache() clled");
		if (mClearCacheObserver == null) 
		{
			mClearCacheObserver=new CachePackageDataObserver();
		}

		PackageManager mPM=getPackageManager();

		@SuppressWarnings("rawtypes")
		final Class[] classes= { Long.TYPE, IPackageDataObserver.class };

		Long localLong=Long.valueOf(CACHE_APP);

		try 
		{
			Method localMethod=
					mPM.getClass().getMethod("freeStorageAndNotify", classes);


			try 
			{
				localMethod.invoke(mPM, localLong, mClearCacheObserver);
			}
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}

		}
		catch (NoSuchMethodException e1)
		{

			e1.printStackTrace();
		}
	}

	private class CachePackageDataObserver extends IPackageDataObserver.Stub 
	{
		public void onRemoveCompleted(String packageName, boolean succeeded) 
		{

		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder mHolder) {
		mMediaPlayer.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.options  )
		{
			ShowOptions showOptions = new ShowOptions(UserMainScreen.this);
			showOptions.showAllOptions();
		}	


		else if(v.getId() == R.id.add_to_favorites)
		{
			if(CurrentClickedChannelName != null){
				AddToFavorites addtofavFavorites = new AddToFavorites(UserMainScreen.this);
				addtofavFavorites.addToFavorites();
			}
			else
			{
				Toast.makeText(getApplicationContext(),"Please Select a Channel to Add",Toast.LENGTH_LONG).show();
			}
		} 
	}


	@Override
	public boolean canPause() {
		return true;
	}
	@Override
	public boolean canSeekBackward() {
		return true;
	}
	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		//Log.e(TAG, "GET CURRENT POSITION " +mMediaPlayer.getCurrentPosition());
		return mMediaPlayer.getCurrentPosition();
	}

	@Override
	public int getDuration() {

		return mMediaPlayer.getDuration(); 
	}

	@Override
	public boolean isPlaying() {
		return mMediaPlayer.isPlaying();
	}

	@Override
	public void pause() {
		mMediaPlayer.pause();
	}

	@Override
	public void seekTo(int i) {
		mMediaPlayer.seekTo(i);
	}
	@Override
	public void start() {
		mMediaPlayer.start();
	}


	@Override
	public boolean isFullScreen() {
		return false;
	}

	/**
	 * toggleFullScreen - Handles the Full Screen for the MediaPlayer and
	 *  Hides the CopyRight Text on the Full Screen
	 * @return void
	 * 
	 * 
	 */
	public void toggleFullScreen() {

		controller.hide(); 
		if(isFullScreen == false){
			isFullScreen = true;
			getActionBar().hide();
			//	Copy_Right_Text.setVisibility(View.GONE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			PreviewFrame.setLayoutParams(lp); 
			PreviewFrameRtmp.setLayoutParams(lp);
		}
		else{
			isFullScreen = false;
			getActionBar().show();


			if (diagonalInches >= 10) {
				//Device is a 10" tablet
				Copy_Right_Text.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(1900,1080);
				PreviewFrame.setLayoutParams(lp);  	
				PreviewFrameRtmp.setLayoutParams(lp);
			} 
			else if (diagonalInches >= 6.7) {
				Log.e("SIZES 7 INCHES ", ""  +diagonalInches);

				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(700,350);
				PreviewFrame.setLayoutParams(lp);  	
				PreviewFrameRtmp.setLayoutParams(lp);

			}
		}
	}

	/**
	 * @params View
	 * @param MotionEvent
	 * @return boolean
	 * This activates the Media Controller if the touch event occurs in the Frame Layouts (PreviewFrame and PreviewFrameRtmp) and Media Player
	 *  
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(( v.getId() == R.id.PreviewFrame || v.getId() == R.id.PreviewFrameRtmp )&&( mMediaPlayer !=null  )){
			if(!controller.isShown()) {
				controller.show();
			}
			else if(controller.isShown()) {
				controller.hide();
			}
		}	 
		return false;
	}

	/**
	 * @return void
	 * This Method checks if  Octoshape system os is null - then its Initializes the os or directly plays if initialized
	 */
	public  void octoshpeValidations() {

		if(os == null){
			/*Initializing  Octoshape System */
			initOctoshapeSystem();
		}
		else{
			/*Play the Octoshape Streams */
			playOctoStreams();
		}

	}

	public void getScreenSize(){

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		widthPixels = metrics.widthPixels;
		heightPixels = metrics.heightPixels;
		Log.e("UserMainScreen", "" +widthPixels);
		Log.e("UserMainScreen", "" +heightPixels);
		widthDpi = metrics.xdpi;
		heightDpi = metrics.ydpi;
		Log.e("UserMainScreen", "" +widthDpi);
		Log.e("UserMainScreen", "" +heightDpi );
		widthInches = widthPixels / widthDpi;
		heightInches = heightPixels / heightDpi;
		Log.e("UserMainScreen", "" +	widthInches);
		Log.e("UserMainScreen", "" +heightInches );

		//The size of the diagonal in inches is equal to the square root of the height in inches squared plus the width in inches squared.
		diagonalInches = Math.sqrt(
				(widthInches * widthInches) 
				+ (heightInches * heightInches));

		Log.e("UserMainScreen", "" +diagonalInches );
	}

}



