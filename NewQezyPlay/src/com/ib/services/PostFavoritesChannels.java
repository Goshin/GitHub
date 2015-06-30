package com.ib.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient; 
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ib.qezyplay.Utils.Constants;

/**
 * @author Ideabytes 
 */

 
public class PostFavoritesChannels extends AsyncTask<FilteredParamsForAsyncTask, String, String> {

	 
	private String TAG = "AsyncTaskCheckClient";
	private Context context = null;


	public PostFavoritesChannels(Context a ) {
		 context = a;

	}

	 
	@Override
	protected  String doInBackground(FilteredParamsForAsyncTask... params) {
		String result = null; 	
		 String UniqueDeviceId = FilteredParamsForAsyncTask.UniqueDeviceId;  
		 String ChannelId =  FilteredParamsForAsyncTask.ChannelId;
		 Log.e(TAG, " ************  added channel id  **********  "+ChannelId);


		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
		 

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(Constants.SERVER_URL+"QezyPlay/api/web/Favorites.json");
		   	String json = "";

				// 3. build jsonObject
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("uuid", UniqueDeviceId );
				jsonObject.accumulate("channelid",ChannelId ); 


				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();
	 
				// 5. set json to StringEntity
				StringEntity se = new StringEntity(json);

				// 6. set httpPost Entity
				httpPost.setEntity(se);

				// 7. Set some headers to inform server about the type of the content   
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				// 8. Execute POST request to the given URL
				HttpResponse httpResponse = httpclient.execute(httpPost);

				// 9. receive response as inputStream
				InputStream inputStream = httpResponse.getEntity().getContent();
				Log.e(TAG, " ************ Result Input Response    lolztest  ********** "+inputStream.toString());

				result = convertInputStreamToString(inputStream);
				Log.e(TAG, "*********** LALITA FOR POSTING ***********************   "+result);
				
			} 
			
			catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}
	 
			 return result;
		}


		private static String convertInputStreamToString(InputStream inputStream) throws IOException{
			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
			String line = "";
			String result = "";
			while((line = bufferedReader.readLine()) != null)
				result += line;

			inputStream.close();
			return result;

		}   



}