package com.ib.qezyplay.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context; 
 
 
public class PostRecentlyViewedChannelsResponse  {

 
	public Context context = null ;
	private JSONObject json;
	private String Status = null;

	public PostRecentlyViewedChannelsResponse(Context a ) 
	{
		context = a ;		 
	}

	public String  AddToRecentlyViewed(String ResultDataFroMsERVER) throws JSONException    {
		 
		String channelInfo = ResultDataFroMsERVER;
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
 		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Status;
	}
 
}





