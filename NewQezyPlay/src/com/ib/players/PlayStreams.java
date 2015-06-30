package com.ib.players;


import com.ib.dialogboxes.NeutralDialog;
import com.ib.dialogboxes.PremiumChannelsDialogBoxes;
import com.ib.qezyplay.UserMainScreen;
import com.ib.qezyplay.Utils.NetworkConnectivity;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
 

/**
 * @author ideabytes
 * @version 1.0
 * PlayStreams - Implements Method PlayVideos for playing the Streams 
 */


public class  PlayStreams   {

	private static final String TAG = "PLAYSTREAMS.JAVA";
	public static  Activity  activity= null ;
	public static Context context; 

/**
 * 
 * @author Ideabytes
 * Interface  IPlayStreamCallBack -Defined an Interface  To access the Methods in the UserMainScreen  
 *
 */
	public interface IPlayStreamCallBack{
		
		void octoshpeValidations() ;
		void playHlsStreams(String  HLSLink);
		 
 	}
	
	//Create the reference  for the Interface IPlayStreamCallBack 
	private  IPlayStreamCallBack iPlayStreamCallBack   ;
 
	//Typecasting the Activity in the Constructor
	public PlayStreams(Activity a) {
		activity = a ;
	 	iPlayStreamCallBack = (IPlayStreamCallBack) a;
	}
	/**
	 * 
	 * @param ChannelStatus
	 * @param ContentType
	 * @param ChannelID
	 * @param ChannelName
	 * @param VideoType
	 * @param ChannelLink
	 * return void 
	 * 
	 * PlayVideo Class will check the - if the Channel status are active 
	 *                                - if network is Available
	 *                                - Content type is Free 
	 *  And then it will play the streams as the per the  video type they belong to i.e HLS ,Octoshape, youtube,Rtmp
	 * 
	 */

	public void PlayVideo(String ChannelStatus,String ContentType,String ChannelID,String ChannelName,String VideoType,String ChannelLink){

		NetworkConnectivity networkConnectivity = new NetworkConnectivity(activity );
     	if(networkConnectivity.isNetworkAvailable() == true){
         	if(ChannelStatus.equals("active")){

			if(ContentType.equals("free"))
			{
				UserMainScreen.spinner.setVisibility(View.VISIBLE);

				/*******Add to favorites***********/
				Log.e(TAG," CurrentClickedChannelId   "  +ChannelID);	
				UserMainScreen.CurrentClickedChannelId = ChannelID;
				Log.e(TAG,"  CurrentClickedChannelName   "  +ChannelName);	
				UserMainScreen.CurrentClickedChannelName = ChannelName;
				UserMainScreen.CurrentClickedChannelLink = ChannelLink; 

				/*******Recently Viewed***********/	
				UserMainScreen.ChannelRecentlyViewed = false;

				/*
				 *  HLS Streams
				 */
				if(VideoType.equals("hls")) {	
					//PreviewFrameRtmp.setVisibility(View.GONE);
					//PreviewFrame.setVisibility(View.VISIBLE);
					 
					UserMainScreen.PreviewFrame.setVisibility(View.VISIBLE);
					iPlayStreamCallBack.playHlsStreams(ChannelLink);
				}
				else if (VideoType.equals( "octoshape"))
				{ 
				 
					UserMainScreen.PreviewFrame.setVisibility(View.VISIBLE);
					UserMainScreen.OCTOLINK = ChannelLink;
					iPlayStreamCallBack.octoshpeValidations();
				}

				else if( VideoType.equals("youtube")){
					UserMainScreen.spinner.setVisibility(View.GONE);

					//
					//						userMainScreen.PreviewFrame.setVisibility(View.GONE);
					//						userMainScreen.PreviewFrameRtmp.setVisibility(View.VISIBLE);
					//
					//						userMainScreen.spinner.setVisibility(View.GONE); 
					//						Toast.makeText(context,  " Youtube under Progress ", Toast.LENGTH_LONG).show();
					//						Log.e(TAG,"   $$$$$$$$$$$$    youtube   $$$$$$$ "  +ChannelLink);
					//						//
					//						//													String YoutubeChannelLink = ChannelLink ; 
					//						//													String VideoId = YoutubeChannelLink.substring(YoutubeChannelLink.lastIndexOf("=")+1);
					//						//													Log.e(TAG,"    video id in usermainScreen.java "  +VideoId);
					//						//													UtubePlayer uTubePlayer = new UtubePlayer(UserMainScreen.this);
					//						//													uTubePlayer.playYoutube(API_KEY, VideoId);
					//
					//						//		PlayRtmpStreams(ChannelLink);


				}

				else if( VideoType.equals("rtmp")){
					UserMainScreen.spinner.setVisibility(View.GONE);
					
					//
					//						userMainScreen.PreviewImage.setVisibility(View.VISIBLE);
					//						userMainScreen.PreviewFrame.setVisibility(View.GONE);
					//
					//						Toast.makeText(context,  " RTMP PLAYER NOW AVAILABLE(HAVE TO BUY THE LICENSE) ", Toast.LENGTH_LONG).show();
					//						userMainScreen.PlayRtmpStreams(ChannelLink);
				}

			} /* End of  content type = free   */

			else if (ContentType.equals("paid"))				
			{ 
				Log.e(TAG,"  Content Paid " +ContentType);	
				PremiumChannelsDialogBoxes  premiumDialogBox = new PremiumChannelsDialogBoxes(activity);
				premiumDialogBox.PremiumDialogBox(ChannelName+" is paid Channel.Please Pay $3/month to avail the service");
			}

		}/********** End of  Channel Status Active  ********/
		else {
			NeutralDialog neutralDialog = new NeutralDialog(context);
			neutralDialog.displayAlertMessage("Alert","Stream Not Active");
		}
				}
				/********** End of   Internet Connection Active  ********/
		
				else
				{
					NeutralDialog neutralDialog = new NeutralDialog(activity);
					neutralDialog.displayAlertMessage("Alert","Please Connect to Working Internet");
				}

	}
 }
