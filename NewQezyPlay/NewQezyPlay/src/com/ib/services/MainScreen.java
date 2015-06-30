package com.ib.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
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
 * @author Lalita 
 */

public class MainScreen extends AsyncTask<String, String, Void> {
	
	private ProgressDialog progressDialog = null;
	private String TAG = "AsyncTaskCheckClient";
	private Context context = null;

	public static ArrayList<String> ChannelID = new ArrayList<String>();
	public static ArrayList<String> ChannelStatus =  new ArrayList<String>();

	public static ArrayList<String> ChannelTypeId =  new ArrayList<String>();
	public static ArrayList<String> ChannelType = new ArrayList<String>();
	public static ArrayList<String> VideoTypeId = new ArrayList<String>();
	public static ArrayList<String> VideoType = new ArrayList<String>();
	public static ArrayList<String> ChannelName = new ArrayList<String>();
	public static ArrayList<String>  ChannelLink= new ArrayList<String>();
	public static ArrayList<String>  ContentType = new ArrayList<String>();	 
	public static ArrayList<String> ChannelThumbnails =  new ArrayList<String>();
	
	public static ArrayList<String>  CountryId = new ArrayList<String>();
	public static ArrayList<String>  CountryName = new ArrayList<String>();	 
	public static ArrayList<String> CountryThumbnails =  new ArrayList<String>();
	
	public static ArrayList<String>  CategoryId = new ArrayList<String>();
	public static ArrayList<String>  CategoryName = new ArrayList<String>();	 
	public static ArrayList<String> CategoryThumbnails =  new ArrayList<String>();

	 
	public static ArrayList<String> TempCategoryId  =  new ArrayList<String>();
	public static ArrayList<String> TempCategoryName =  new ArrayList<String>();
	public static ArrayList<String> TempCountryName =  new ArrayList<String>();

	JSONArray ResponseChannelInfo ,ResponseCountryInfo,ResponseCategoryInfo ;
	JSONObject json;

	
	public MainScreen(Context a) {
		context = a;
	}

	 
 
	@Override
	protected   Void  doInBackground(String... params) {
		String result = null; 		 
		String UniqueDeviceId = params[0];
		Log.e(TAG, "  LALITA &&&&&&&&&&&& TESTING %%%%%%%%%%%%%Unique Device Id  lalita test "+ UniqueDeviceId);

		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(Constants.SERVER_URL+"QezyPlay/api/web/Chanellist.json");

			String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("uuid", UniqueDeviceId );


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
		//	Log.e(TAG, " ************ Result Input Response    lolztest  **********        "+inputStream.toString());

			result = convertInputStreamToString(inputStream); 
			 
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
 
		try {
			getChannelInfo(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
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
	 
	public void getChannelInfo(String ResultDataFroMsERVER) throws JSONException    {
		// TODO Auto-generated method stub	

		String channelInfo =ResultDataFroMsERVER;
		Log.e(TAG,"channel Information   " +channelInfo );	

		/*
		 * Json object for parsing the data(Channel Info)
//				 */
		try {
			json = new JSONObject(channelInfo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	


		JSONObject results = json.getJSONObject("results");	

		try {
			ResponseCountryInfo= results.getJSONArray("Country_List");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CountryId.clear();
		CountryName.clear();
	//	CountrySelectedId.clear();
		CountryThumbnails.clear();
		for(int i=0 ; i <ResponseCountryInfo.length();i++)
		{
			JSONObject jsonobjectresponsedata = ResponseCountryInfo.getJSONObject(i);	
			CountryId.add(jsonobjectresponsedata.getString("id"));
			CountryName.add(jsonobjectresponsedata.getString("Country"));
			CountryThumbnails.add(jsonobjectresponsedata.getString("Country_Image_Link"));

		}


		try {
			ResponseCategoryInfo= results.getJSONArray("Channel_Category_Info");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CategoryId.clear();
		CategoryThumbnails.clear();
		CategoryName.clear();
	 
		for(int i=0 ; i <ResponseCategoryInfo.length();i++)
		{
			JSONObject jsonobjectresponsedata = ResponseCategoryInfo.getJSONObject(i);	
			CategoryId.add(jsonobjectresponsedata.getString("id"));
			CategoryThumbnails.add(jsonobjectresponsedata.getString("Category_Image_Link"));
			CategoryName.add(jsonobjectresponsedata.getString("Category_Type"));

		}
		try {
			ResponseChannelInfo= results.getJSONArray("Channel_List");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ChannelID.clear();
		TempCategoryName.clear();
		TempCategoryId.clear();
		ChannelTypeId.clear();
		ChannelThumbnails.clear();
		VideoType.clear();
		ChannelStatus.clear();		
		TempCountryName.clear();
		ChannelType.clear();
		VideoTypeId.clear();		 
		ChannelName.clear();
		ContentType.clear();
		ChannelLink.clear();
		 
		
		for(int i=0 ; i <ResponseChannelInfo.length();i++)
		{
			 
			JSONObject jsonobjectresponsedata = ResponseChannelInfo.getJSONObject(i);			 
			
			ChannelID.add( jsonobjectresponsedata.getString("Channel_Id")) ;
			TempCategoryName.add( jsonobjectresponsedata.getString("Channel_Category_Name")) ;
			TempCategoryId.add( jsonobjectresponsedata.getString("Channel_Category_Id")) ;

			ChannelTypeId.add( jsonobjectresponsedata.getString("Channel_Type_Id")) ;
			ChannelThumbnails.add( jsonobjectresponsedata.getString("Channel_Image_Link")) ;
			VideoType.add(jsonobjectresponsedata.getString("Stream_Type"));
			ChannelStatus.add(jsonobjectresponsedata.getString("Stream_Status"));
			
			TempCountryName.add(jsonobjectresponsedata.getString("Country"));
			ChannelType.add(jsonobjectresponsedata.getString("Channel_Type"));
			VideoTypeId.add(jsonobjectresponsedata.getString("Stream_Type_Id"));			
			 
			ChannelName.add(jsonobjectresponsedata.getString("Channel_Name"));
			ContentType.add( jsonobjectresponsedata.getString("Content_Type")) ;
			ChannelLink.add(jsonobjectresponsedata.getString("Play_Link"));			

		}
 
		for(int i = 0 ; i <ChannelThumbnails.size() ; i ++)
		{
			Log.e(TAG," STREAM TYPE ID "+ChannelThumbnails.get(i) + " size of the arraylist "+ ChannelThumbnails.size());
		}		 

	}



	 
}