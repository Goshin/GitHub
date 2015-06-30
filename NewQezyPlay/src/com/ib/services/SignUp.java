package com.ib.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.ib.qezyplay.Utils.Constants;

import android.util.Log;

public class SignUp    {   


	protected static final String TAG = null;
	public static InputStream inputStream = null;
	public static HttpResponse httpResponse = null;
	// Create GetText Metod
	public static  void  postData(String MobileNumber,String Email,String Username,String Password,String UniqueDeviceId,String IpAddress)  throws  JSONException, ClientProtocolException, IOException
	{
		 
		Log.e(TAG, "MobileNumber  " +MobileNumber);
		Log.e(TAG, " Email  " + Email);
		Log.e(TAG, " Username  " + Username);
		Log.e(TAG, "Password   " +Password);
		Log.e(TAG," UniqueDeviceId  " +UniqueDeviceId);
		Log.e(TAG," IpAddress  " +IpAddress);


		HttpClient httpclient = new DefaultHttpClient();
		
	 

		// 2. make POST request to the given URL
		HttpPost httpPost = new HttpPost(Constants.SERVER_URL+"QezyPlay/api/web/Signup.json");
		String json = "";
		String result = null;

		// 3. build jsonObject 
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.accumulate("email",Email);					 
			jsonObject.accumulate("username",Username); 
			jsonObject.accumulate("password",Password);		 
			jsonObject.accumulate("mobileno",MobileNumber);
			jsonObject.accumulate("address",IpAddress);
			
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	
		// 4. convert JSONObject to JSON to String
		json = jsonObject.toString();
		json = jsonObject.toString();
		Log.e(TAG, "***************** post parameters**************" +json.toString());
		// 5. set json to StringEntity
		StringEntity se = new StringEntity(json);

		// 6. set httpPost Entity
		httpPost.setEntity(se);

		// 7. Set some headers to inform server about the type of the content   
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		// 8. Execute POST request to the given URL
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpPost);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 9. receive response as inputStream
		InputStream inputStream = null;
		try {
			inputStream = httpResponse.getEntity().getContent();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 10. convert inputstream to string
		if(inputStream != null)
		{
			result = convertStreamToString(inputStream);
		
		}

		else
		{
			result = "Did not work!";
		}				
		    Log.e(TAG, "*****************sResult Input Response**************" +result);
		 //   return result;

	}
  
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append((line + "\n"));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
}

