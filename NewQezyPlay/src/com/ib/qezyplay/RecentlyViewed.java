package com.ib.qezyplay;

import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import com.ib.qezyplay.Utils.PostRecentlyViewedChannelsResponse;
import com.ib.services.FilteredParamsForAsyncTask;
import com.ib.services.GetRecentlyViewedChannels;
import com.ib.services.PostRecentlyViewedChannels;
import android.app.Activity;
import android.content.Context;
 

public class RecentlyViewed extends Activity {

	private Context context = null;
	private String  resultFromAsyncTaskFilteredSelection= null;	 
	private Boolean ChannelRecentlyViewed;

	/**
	 * Contructor for the passing the Reference of the  UserMainScreen(Main Activity)
	 * @param a
	 */
	
	public RecentlyViewed(Context a) {
		context = a;	 
	}
	
	/**
	 *  Post the Recently Viewed channels  i.e ChannelId  to the Server.
	 *  
	 * @author Ideabytes
	 * @param uniqueDeviceId
	 * @param ChannelId
	 * @param ChannelRecentlyViewed
	 * @result void 
	 * 
	 */
 	public void PostRecentlyViewedChannel(String uniqueDeviceId, String  ChannelId) {
     
		ChannelRecentlyViewed = true;
		FilteredParamsForAsyncTask filteredParamsForAsyncTask =  new  FilteredParamsForAsyncTask(uniqueDeviceId,ChannelId );

		PostRecentlyViewedChannels  postRecentlyViewed = new PostRecentlyViewedChannels (context);
		try {
			resultFromAsyncTaskFilteredSelection =  postRecentlyViewed.execute(filteredParamsForAsyncTask).get(); 

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PostRecentlyViewedChannelsResponse  postRecentlyViewedChannelsResponse = new PostRecentlyViewedChannelsResponse(context);
		try {
           postRecentlyViewedChannelsResponse.AddToRecentlyViewed(resultFromAsyncTaskFilteredSelection);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			

	}
	/**
	 * @author ideabytes
	 * @param uniqueDeviceId
	 * @return void
	 * Get the Recently viewed from the   Service calls  and  will display all the  user's recently viewed channels
	 * 
	 */

	public  void getUpdatedRecentlyViewedChannel(String uniqueDeviceId) {
  
		FilteredParamsForAsyncTask filteredParamsForAsyncTask =  new  FilteredParamsForAsyncTask(uniqueDeviceId);
        GetRecentlyViewedChannels getRecentlyViewedChannels = new GetRecentlyViewedChannels(context);
		try {
			getRecentlyViewedChannels.execute(filteredParamsForAsyncTask).get(); 

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
}
