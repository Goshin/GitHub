package com.ib.dialogboxes;

import com.ib.displayadapters.Adapterlist;
import com.ib.qezyplay.Favorites;
import com.ib.qezyplay.RecentlyViewed;
import com.ib.qezyplay.UserMainScreen;
import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
import android.view.View;
 
/**
 * ShowOptions - Implementation to show the list of options (RecentlyViewed and Favorites)
 * @author lalita
 *@version 1.0.0
 */


public class ShowOptions{
 
	private Context context = null;
/**
 * Constructor to pass the Reference  of the UserMainScreen.java 
 * @param a
 */
	public ShowOptions(Context  a) {
		context= a;
	}

/**
 * showAllOptions() -  Implementation of the List View for two options Recently viewed and Favorites 
 */
	public void   showAllOptions(){

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Make your selection");
		builder.setItems(UserMainScreen.Options, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// Do something with the selection
				UserMainScreen.OptionDisplayButton.setText(UserMainScreen.Options[item]); 
				RecentlyViewed recentlyViewed = new RecentlyViewed(context);

				if(UserMainScreen.Options[item] == "Recently Viewed"){

					recentlyViewed.getUpdatedRecentlyViewedChannel(UserMainScreen.UniqueDeviceId);
					UserMainScreen.FavoritesHorizontalListView.setVisibility(View.GONE);
					UserMainScreen.RecentlyViewedHorizontalListView.setVisibility(View.VISIBLE);
					UserMainScreen.RecentlyViewedHorizontalListView.setAdapter(Adapterlist.RecentlyViewedAdapter);

				}
				else if(UserMainScreen.Options[item] == "Favorites") {
					// API SERVICE CALLS - get Favorites Service Call
					Favorites favorites = new Favorites(context);
					favorites.getUpdatedFavoriteChannels(UserMainScreen.UniqueDeviceId);
                    UserMainScreen.RecentlyViewedHorizontalListView.setVisibility(View.GONE);
					UserMainScreen.FavoritesHorizontalListView.setVisibility(View.VISIBLE);
					UserMainScreen. FavoritesHorizontalListView.setAdapter(Adapterlist.FavoritesAdapter);
				}

			}});
		AlertDialog alert = builder.create();
		alert.show();
 	}

}




