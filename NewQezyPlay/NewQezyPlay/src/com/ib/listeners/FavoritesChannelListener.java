package com.ib.listeners;

import com.ib.players.PlayStreams;
import com.ib.services.GetFavoritesChannels;
import android.app.Activity;
import android.content.Context; 
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * FavoritesChannelListener  - This implements the OnitemClickListener for  Favorites Horizontal List View   
 *and Onclick of each item in the list View the channel are played
 * @author Ideabytes
 * @version 1.0.0
 *
 *
 */
public class FavoritesChannelListener implements OnItemClickListener{ 

	public Context context;
	public  static Activity activity;

	/**
	 * Contructor for the passing the Reference of the  UserMainScreen(Main Activity)
	 * @param a
	 */
	public  FavoritesChannelListener(Activity a) {
		activity = a;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PlayStreams  playStreams = new PlayStreams(activity);
		playStreams.PlayVideo(GetFavoritesChannels.ChannelStatus.get(position),GetFavoritesChannels.ContentType.get(position),GetFavoritesChannels.ChannelID.get(position), 
				GetFavoritesChannels.ChannelName.get(position), GetFavoritesChannels.VideoType.get(position),GetFavoritesChannels.ChannelLink.get(position) );
	}	 

};







