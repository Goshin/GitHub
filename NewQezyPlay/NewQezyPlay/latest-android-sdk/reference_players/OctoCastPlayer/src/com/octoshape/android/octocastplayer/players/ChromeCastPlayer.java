package com.octoshape.android.octocastplayer.players;

import java.io.IOException;

import octoshape.osa2.android.listeners.MediaPlayerListener;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.Listener;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.RemoteMediaPlayer;
import com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.octoshape.android.octocastplayer.ChromeCastCallbacks;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.support.v7.media.MediaRouter;
import android.util.Log;

public class ChromeCastPlayer extends MediaRouter.Callback implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

	private static final String LOGTAG = "ChromeCastConnection";
	private final Context ctx;
	private CastDevice mSelectedDevice;
	private GoogleApiClient mApiClient;
	private Listener mCastClientListener;
	private RemoteMediaPlayer mRemoteMediaPlayer;
	private MediaInfo mMediaInfo;
	private String mSessionId;
	private boolean mApplicationStarted;
	private ChromeCastCallbacks mChromeCastPlayerCallback;
	private MediaPlayerListener mMediaPlayerListener;
	private MediaStatus mMediaStatus;
	
	public ChromeCastPlayer(Context ctx, ChromeCastCallbacks ccc){
		super();
		this.ctx = ctx;
		this.mChromeCastPlayerCallback = ccc;
		
		mCastClientListener = new Cast.Listener() {
			  @Override
			  public void onApplicationStatusChanged() {
			    if (mApiClient != null) {
			      Log.d(LOGTAG, "onApplicationStatusChanged: "
			       + Cast.CastApi.getApplicationStatus(mApiClient));
			    }
			  }

			  @Override
			  public void onVolumeChanged() {
			    if (mApiClient != null) {
			      Log.d(LOGTAG, "onVolumeChanged: " + Cast.CastApi.getVolume(mApiClient));
			    }
			  }

			  @Override
			  public void onApplicationDisconnected(int errorCode) {
				  mChromeCastPlayerCallback.onFailedConnection("onApplicationDisconnected() :"+errorCode);
				  shutdown();
			  }
		};
	}
	
	@Override
	public void onRouteSelected(MediaRouter router, RouteInfo info) {
		mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
		mChromeCastPlayerCallback.onDeviceSelected(mSelectedDevice);
		info.getId();
	}
	@Override
	public void onRouteUnselected(MediaRouter router, RouteInfo info) {
		mChromeCastPlayerCallback.onDeviceUnselected(mSelectedDevice);
		mSelectedDevice = null;
		shutdown();
	}
	
	public void playStream(MediaInfo mediainfo, MediaPlayerListener mpl){
		
		this.mMediaInfo = mediainfo;
		this.mMediaPlayerListener = mpl;
		
		mRemoteMediaPlayer = new RemoteMediaPlayer();
		mRemoteMediaPlayer.setOnStatusUpdatedListener(new RemoteMediaPlayer.OnStatusUpdatedListener() {
		  @Override
		  public void onStatusUpdated() {
		    mMediaStatus = mRemoteMediaPlayer.getMediaStatus();
		    
		    if(mMediaStatus != null ){
		    	Log.d(LOGTAG, "RemotePlayerState:" + mMediaStatus.getPlayerState());
		    	if(mMediaStatus.getPlayerState()== MediaStatus.PLAYER_STATE_PLAYING)
		    		mMediaPlayerListener.onMediaPlaybackStarted();
		    	
		    	else if(mMediaStatus.getPlayerState() == MediaStatus.PLAYER_STATE_IDLE && mMediaStatus.getIdleReason() == MediaStatus.IDLE_REASON_FINISHED)
		    		mMediaPlayerListener.onMediaPlaybackCompleted();
		    }
		  }
		});
		Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions.builder(mSelectedDevice, mCastClientListener);
		mApiClient = new GoogleApiClient.Builder(ctx).addApi(Cast.API, apiOptionsBuilder.build()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		mApiClient.connect();
	}
	
	private void connectChannel() {

		try {Cast.CastApi.setMessageReceivedCallbacks(mApiClient, mRemoteMediaPlayer.getNamespace(), mRemoteMediaPlayer);
		} catch (IOException e) {
			mChromeCastPlayerCallback.onFailedConnection("Exception while creating media channel");
			return;
		}
		mRemoteMediaPlayer.requestStatus(mApiClient).setResultCallback(
			new ResultCallback<RemoteMediaPlayer.MediaChannelResult>() {
				@Override
				public void onResult(MediaChannelResult result) {
					Log.d(LOGTAG, result.getStatus().toString());
					if (!result.getStatus().isSuccess()) {
						mChromeCastPlayerCallback.onFailedConnection("onResult() failed: "+ result.getStatus().getStatusMessage());
					} else {
						try {
							mRemoteMediaPlayer.load(mApiClient, mMediaInfo, true).setResultCallback(new ResultCallback<RemoteMediaPlayer.MediaChannelResult>() {
								@Override
								public void onResult(MediaChannelResult result) {
									if (result.getStatus().isSuccess())
										Log.d(LOGTAG, "Media loaded successfully");
								}
							});
						} catch (IllegalStateException e) {
							mChromeCastPlayerCallback.onFailedConnection("onResult() failed: "+ "Problem occurred with media during loading");
						} catch (Exception e) {
							mChromeCastPlayerCallback.onFailedConnection("onResult() failed: "+ "Problem opening media during loading");
						}
					}
				}
			});
	}
	
	@Override
	public void onConnected(Bundle bundle) {
		try {
			Cast.CastApi.launchApplication(mApiClient,CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID, false).setResultCallback(new ResultCallback<Cast.ApplicationConnectionResult>() {
				@Override
				public void onResult(Cast.ApplicationConnectionResult result) {
					Status status = result.getStatus();
					if (status.isSuccess()) {
						mSessionId = result.getSessionId();
						mApplicationStarted = true;
						connectChannel();
					} else {
						mChromeCastPlayerCallback.onFailedConnection("onConnected() failed:"+ status.getStatusMessage());
						shutdown();
					}
				}
			});
		} catch (Exception e) {
			Log.e(LOGTAG, "Failed to launch application", e);
		}
	}
	public void shutdown() {
		  Log.d(LOGTAG, "ChromecastPlayer shutdown");
		  if (mApiClient != null) {
		      if (mApplicationStarted) {
		        if (mApiClient.isConnected() || mApiClient.isConnecting()) {
		          Cast.CastApi.stopApplication(mApiClient, mSessionId);
		          
		          if(mRemoteMediaPlayer!=null)
		        	  mRemoteMediaPlayer.stop(mApiClient);
		          mApiClient.disconnect();
		        }
		        mApplicationStarted = false;
		      }
		      mApiClient = null;
		    }
		  mSelectedDevice = null;
		  mSessionId = null;
		}
	
	@Override
	public void onConnectionSuspended(int code) {
		mChromeCastPlayerCallback.onFailedConnection("onConnectionSuspended():"+ code);
		shutdown();
	}
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mChromeCastPlayerCallback.onFailedConnection("onFailedConnection():"+ result.getErrorCode());
		shutdown();
	}

	public String getMediaUrl() {
		return mMediaInfo.getContentId();
	}

	public boolean isPlaying() {
		return mMediaStatus!= null && mMediaStatus.getPlayerState() == MediaStatus.PLAYER_STATE_PLAYING;
	}
}