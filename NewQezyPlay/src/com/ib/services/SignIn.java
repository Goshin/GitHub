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

public class SignIn    {   


	protected static final String TAG = null;
	public static InputStream inputStream = null;
	public static HttpResponse httpResponse = null;
	 
	
	public static  String postData(String  SignInEmailId,String SignInPassword,int RememberMe,String SignInUniqueDeviceId)  throws  JSONException, ClientProtocolException, IOException
	{


		HttpClient httpclient = new DefaultHttpClient();
		 HttpPost httpPost = new HttpPost(Constants.SERVER_URL+"QezyPlay/api/web/Login.json");
		String json = "";
		String result = null;
		Log.e(TAG, " Email ##### " +SignInEmailId );
		Log.e(TAG, "Password########  " +SignInPassword);
		Log.e(TAG,"  RememberMe########  " +RememberMe);
		Log.e(TAG,"  UniqueDeviceId #######" +SignInUniqueDeviceId);
 
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.accumulate("email",SignInEmailId);					 
			jsonObject.accumulate("password",SignInPassword);			 
			jsonObject.accumulate("deviceid",SignInUniqueDeviceId);
			jsonObject.accumulate("autologin",Integer.toString(RememberMe));	


		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
 
		json = jsonObject.toString();
		Log.e(TAG, "***************** post parameters**************" +json.toString());
  
		StringEntity se = new StringEntity(json);  
	 
		httpPost.setEntity(se);
 
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
 
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
  
		if(inputStream != null)
		{
			result = convertStreamToString(inputStream);			 
		}

		else
		{
			result = "Did not work!";
		}		
	
		return result;

	}

	private static String convertStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		Log.e(TAG, "*****************sResult Input Response* &&&&&&&&&&s*************"  + result.toString());
		return result;

	}   
}
