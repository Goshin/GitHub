package com.ib.qezyplay;

import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import com.ib.qezyplay.Utils.PostFavoritesChannelsResponse;
import com.ib.services.FilteredParamsForAsyncTask;
import com.ib.services.GetFavoritesChannels;
import com.ib.services.PostFavoritesChannels;
import android.content.Context;
 
 
/**
 * Favorites - Implements the favorites section that includes posting/Sending the Favorites channels added by the user  
 * retrieving the updated information 
 * @author Ideabytes
 * @version - 1.0.0 
 *
 */
		
public class Favorites  {
  
	private Context context = null;
	private String  resultFromAsyncTaskFilteredSelection = null;
	 
	/**
	 * Contructor for the passing the Reference of the  UserMainScreen(Main Activity)
	 * @param a
	 */
	public Favorites(Context a){
		context = a; 
	  }

/**
 * postNewlyAddedFavoriteChannels(String uniqueDeviceId, String  ChannelId) - 
 * Post the Favorites channels added by the user  
 * @param uniqueDeviceId
 * @param ChannelId
 * @return
 */
	public   String postNewlyAddedFavoriteChannels(String uniqueDeviceId, String  ChannelId) {
		
		String Result = null;
		FilteredParamsForAsyncTask filteredParamsForAsyncTask =  new  FilteredParamsForAsyncTask(uniqueDeviceId,ChannelId );
     	PostFavoritesChannels favoriteChannels = new PostFavoritesChannels(context);
		try {
			resultFromAsyncTaskFilteredSelection =  favoriteChannels.execute(filteredParamsForAsyncTask).get(); 

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	PostFavoritesChannelsResponse  postFavoritesChannelsResponse = new PostFavoritesChannelsResponse(context);
		try {

			Result =  postFavoritesChannelsResponse.AddToFavorites(resultFromAsyncTaskFilteredSelection);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Result;
	}
/**
 * getUpdatedFavoriteChannels(String uniqueDeviceId) - Gets the updated list of the Favorites Channels 
 * @param uniqueDeviceId
 */

	public void getUpdatedFavoriteChannels(String uniqueDeviceId) {
	 
		FilteredParamsForAsyncTask filteredParamsForAsyncTask =  new  FilteredParamsForAsyncTask(uniqueDeviceId);
     	GetFavoritesChannels getFavoriteChannels = new GetFavoritesChannels(context);		
		try {
			getFavoriteChannels.execute(filteredParamsForAsyncTask).get();

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

}
