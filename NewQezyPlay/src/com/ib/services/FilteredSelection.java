package com.ib.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
 
public class FilteredSelection extends AsyncTask<FilteredParamsForAsyncTask, String,Void> {

 
	private String TAG = "AsyncTaskCheckClient";
	private Context context = null;

	JSONArray ResponseChannelInfo ,ResponseCountryInfo,ResponseCategoryInfo ;
	JSONObject json;

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
	public static ArrayList<String>  CountrySelectedId = new ArrayList<String>();	
	public static ArrayList<String> CountryThumbnails =  new ArrayList<String>();
	
	public static ArrayList<String>  CategoryId = new ArrayList<String>();
	public static ArrayList<String>  CategoryName = new ArrayList<String>();	 
	public static ArrayList<String> CategoryThumbnails =  new ArrayList<String>();
	public static ArrayList<String> CategorySelectedId =  new ArrayList<String>();
	
	
	public static ArrayList<String> TempCategoryId  =  new ArrayList<String>();
	public static ArrayList<String> TempCategoryName =  new ArrayList<String>();
	public static ArrayList<String> TempCountryName =  new ArrayList<String>();


	public FilteredSelection(Context a ) {
		 context = a;

	}

	 
	@Override
	protected  Void doInBackground(FilteredParamsForAsyncTask... params) {
		String result = null; 	
		ArrayList<String> CountriesSelection = FilteredParamsForAsyncTask.CountriesSelection;  
		ArrayList<String> CategoriesSelection =  FilteredParamsForAsyncTask.CategoriesSelection;



		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost( Constants.SERVER_URL+"QezyPlay/api/web/MultipleOptionsFilter.json");

			String json = "";

			// 3. build jsonObject

			//			{"countryid":[{"countryid1":"9","countryid2":"NULL","countryid3":"NULL"}],
			//				"categoryid":[{"categoryid1":"3","categoryid2":"4","categoryid3":"NULL"}]
			//				}
		
			 
			JSONObject  CountryId = new JSONObject();
			
			if(CountriesSelection.size() == 0) 
			{
				try {
					CountryId.put("countryid1", "NULL");
					CountryId.put("countryid2", "NULL");
					CountryId.put("countryid3", "NULL");				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if(CountriesSelection.size() == 1) 
			{

				try {
					CountryId.put("countryid1", CountriesSelection.get(0));
					CountryId.put("countryid2", "NULL");
					CountryId.put("countryid3", "NULL");				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if(CountriesSelection.size() == 2) 
			{

				try {
					CountryId.put("countryid1", CountriesSelection.get(0));
					CountryId.put("countryid2", CountriesSelection.get(1));
					CountryId.put("countryid3", "NULL");				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if(CountriesSelection.size() == 3) 
			{
				try {
					CountryId.put("countryid1",CountriesSelection.get(0));
					CountryId.put("countryid2",CountriesSelection.get(1));
					CountryId.put("countryid3",CountriesSelection.get(2));				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		

			JSONObject  CategoryId = new JSONObject();
			if(CategoriesSelection.size() == 0){ 
				try {
					CategoryId.put("categoryid1", "NULL");
					CategoryId.put("categoryid2", "NULL");
					CategoryId.put("categoryid3", "NULL");				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			else if(CategoriesSelection.size() == 1){ 
				try {
					CategoryId.put("categoryid1",CategoriesSelection.get(0));
					CategoryId.put("categoryid2", "NULL");
					CategoryId.put("categoryid3", "NULL");				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if(CategoriesSelection.size() == 2){ 
				try {
					CategoryId.put("categoryid1",CategoriesSelection.get(0));
					CategoryId.put("categoryid2",CategoriesSelection.get(1));
					CategoryId.put("categoryid3","NULL");				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(CategoriesSelection.size() == 3){ 
				try {
					CategoryId.put("categoryid1",CategoriesSelection.get(0));
					CategoryId.put("categoryid2",CategoriesSelection.get(1));
					CategoryId.put("categoryid3",CategoriesSelection.get(2));				 

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			JSONArray CountryJsonArray = new JSONArray();
			CountryJsonArray.put(CountryId);	
			JSONArray CategoryJsonArray = new JSONArray();
			CategoryJsonArray.put(CategoryId);	

			// 4. convert JSONObject to JSON to String
			JSONObject  JSONObj = new JSONObject();

			JSONObj.put("categoryid", CategoryJsonArray);
			JSONObj.put("countryid", CountryJsonArray);




			json = JSONObj.toString();


			Log.e(TAG, " TEST INPUT JSON  TO SERVER LALITA  "+json.toString());


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
			Log.e(TAG, "*******************sdfsfs**************result******sdsgsggg*******************"+result);

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		try {
			getFilteredChannelInfo( result);
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

	public void getFilteredChannelInfo(String ResultDataFroMsERVER) throws JSONException    {
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
		CountrySelectedId.clear();
		CountryThumbnails.clear();
		
		for(int i=0 ; i <ResponseCountryInfo.length();i++)
		{
			JSONObject jsonobjectresponsedata = ResponseCountryInfo.getJSONObject(i);	
			CountryId.add(jsonobjectresponsedata.getString("id"));
			CountryName.add(jsonobjectresponsedata.getString("Country"));
			CountrySelectedId.add(jsonobjectresponsedata.getString("selectedid"));
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
		CategorySelectedId.clear();
		for(int i=0 ; i <ResponseCategoryInfo.length();i++)
		{
			JSONObject jsonobjectresponsedata = ResponseCategoryInfo.getJSONObject(i);
			CategoryId.add(jsonobjectresponsedata.getString("id"));
			CategoryThumbnails.add(jsonobjectresponsedata.getString("Category_Image_Link"));
			CategoryName.add(jsonobjectresponsedata.getString("Category_Type"));
			CategorySelectedId.add(jsonobjectresponsedata.getString("selectedid"));
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
		
		 
		
		ContentType.add( jsonobjectresponsedata.getString("Content_Type")) ;
		ChannelName.add(jsonobjectresponsedata.getString("Channel_Name"));
		ChannelLink.add(jsonobjectresponsedata.getString("Play_Link"));
		}
		 
 
		for(int i = 0 ; i <ChannelThumbnails.size() ; i ++)
		{
			
			Log.e(TAG,"cHANNELS Names)))))))))))))))))))test(((((((((((((((((((((((((((" +ChannelName.get(i));
			Log.e(TAG," STREAM TYPE ID "+ChannelThumbnails.get(i) + " size of the arraylist "+ ChannelThumbnails.size());
			
		}		 

	}


}