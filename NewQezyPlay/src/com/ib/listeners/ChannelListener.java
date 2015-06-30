package com.ib.listeners;

import com.ib.players.PlayStreams;
import com.ib.services.FilteredSelection;
import com.ib.services.MainScreen;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ChannelListener - This implements the OnitemClickListener for
 *          Channel Horizontal List View
 * @author Ideabytes
 * @version 1.0.0 
 *
 */

public class ChannelListener implements OnItemClickListener {
 
	public Context context;
	public static Activity activity;

	/**
	 * Contructor for the passing the Reference of the UserMainScreen(Main
	 * Activity)
	 * 
	 * @param a
	 */

	public ChannelListener(Activity a) {
		activity = a;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
 
		PlayStreams playStreams = new PlayStreams(activity);
     	if (CountryCategoryListeners.SelectedCategoryCount == 0
				&& CountryCategoryListeners.SelectedCountryCount == 0) {

			playStreams.PlayVideo(MainScreen.ChannelStatus.get(position),
					MainScreen.ContentType.get(position),
					MainScreen.ChannelID.get(position),
					MainScreen.ChannelName.get(position),
					MainScreen.VideoType.get(position),
					MainScreen.ChannelLink.get(position));
		} else {
			playStreams.PlayVideo(
					FilteredSelection.ChannelStatus.get(position),
					FilteredSelection.ContentType.get(position),
					FilteredSelection.ChannelID.get(position),
					FilteredSelection.ChannelName.get(position),
					FilteredSelection.VideoType.get(position),
					FilteredSelection.ChannelLink.get(position));
		}
	}
};
