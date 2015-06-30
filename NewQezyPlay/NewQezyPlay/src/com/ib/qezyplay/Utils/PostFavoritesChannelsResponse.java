package com.ib.qezyplay.Utils;

import org.json.JSONException;
import org.json.JSONObject; 
import android.content.Context; 
import android.util.Log; 

public class PostFavoritesChannelsResponse  {

	private static final String TAG = null;
	public Context context = null ;	 
	private JSONObject json;
	private String Status = null;


	public PostFavoritesChannelsResponse(Context a ) 
	{
		context= a;		 
	}

	public String  AddToFavorites(String ResultDataFroMsERVER) throws JSONException    {
	 
		String channelInfo =ResultDataFroMsERVER;
		Log.e(TAG,"channel Information   " +channelInfo );	

		/**
		 * Json object for parsing the data(Channel Info)
		 */
		try {
			json = new JSONObject(channelInfo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	


		JSONObject results = json.getJSONObject("results");	

		try {
			Status = results.get("status").toString(); 

			Log.e(TAG," status   " +Status );	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Status;
	}



}





