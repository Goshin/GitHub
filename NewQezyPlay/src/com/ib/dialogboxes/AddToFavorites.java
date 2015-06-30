
package com.ib.dialogboxes;

import com.ib.displayadapters.Adapterlist;
import com.ib.qezyplay.Favorites;
import com.ib.qezyplay.UserMainScreen;
import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
import android.view.View;
  
/**
 * AddToFavorites - Implementation for the Add to Favorites  button 
 * @author Ideabytes
 * @version 1.0.0
 *
 */

public class AddToFavorites {
	
 private Context context = null;
	/**
	 * Contructor for passing the Reference of UserMainSreen.java
	 * @param a
	 */
	public AddToFavorites(Context  a) {
		context= a;
	}

	/**
	 * Alert box for confirmation if the channel should be added to the Favorites List 
	 */
	public void  addToFavorites(){


		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		// set title
		alertDialogBuilder.setTitle("Add To Favorites");
		// set dialog message
		alertDialogBuilder
		.setMessage("Do you want to add the "+UserMainScreen.CurrentClickedChannelName+" to your Favorites" )
		.setCancelable(false)
		.setPositiveButton("Add",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity

				// API SERVICE CALLS - Post Favorites Service Call
				Favorites favorites = new Favorites(context);

				favorites.postNewlyAddedFavoriteChannels(UserMainScreen.UniqueDeviceId,UserMainScreen.CurrentClickedChannelId);
		
				//Display Fav immediately if the current Selected Item is FAVORITES

				if(UserMainScreen.OptionDisplayButton.getText() == "Favorites")
				{
					// API SERVICE CALLS - get Favorites Service Call
					favorites.getUpdatedFavoriteChannels(UserMainScreen.UniqueDeviceId);							  						  
					
					//Display the updated List 
                 	UserMainScreen.RecentlyViewedHorizontalListView.setVisibility(View.GONE);
					UserMainScreen.FavoritesHorizontalListView.setVisibility(View.VISIBLE);
                 	UserMainScreen. FavoritesHorizontalListView.setAdapter(Adapterlist.FavoritesAdapter);
				}
			}					
		}) 
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});	 
		AlertDialog alertDialog = alertDialogBuilder.create();			 
		alertDialog.show();
	}			 

}






