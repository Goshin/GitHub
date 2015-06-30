package com.ib.listeners;

import com.ib.players.PlayStreams;
import com.ib.services.GetRecentlyViewedChannels;
import android.app.Activity;
import android.content.Context; 
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Ideabytes
 * @version 1.0.0
 *RecentlyViewedChannelListener  - This implements the OnitemClickListener for  RecentlyViewed Horizontal List View   
 *and Onclick of each item in the list View the Recently Viewed Channels are played
 *
 */
public class RecentlyViewedChannelListener implements OnItemClickListener{ 

    public Context context;
	public  static Activity activity;
 
	/**
	 * Contructor for the passing the Reference of the  UserMainScreen(Main Activity)
	 * @param a
	 */
	public  RecentlyViewedChannelListener(Activity a) {
		activity = a;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PlayStreams  playStreams = new PlayStreams(activity);
		playStreams.PlayVideo(GetRecentlyViewedChannels.ChannelStatus.get(position),
				GetRecentlyViewedChannels.ContentType.get(position),
				GetRecentlyViewedChannels.ChannelID.get(position), 
				GetRecentlyViewedChannels.ChannelName.get(position), 
				GetRecentlyViewedChannels.VideoType.get(position),GetRecentlyViewedChannels.ChannelLink.get(position) );
	}
 };







