package com.ib.qezyplay;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ib.dialogboxes.NeutralDialog;
import com.ib.dialogboxes.NoInternetConnection;
import com.ib.qezyplay.Utils.NetworkConnectivity;
import com.ib.services.VerifyExistingUser;

import android.app.Activity; 
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
 
/**
 *  MainActivity - Implements two buttons sign In and SignUp 
 *  If  the Remember Password is Checked .Then the user is directly directed to the MainScreen i.e UserMainScreen.java
 * @author Ideabytes
 *
 */

public class MainActivity extends  Activity implements OnClickListener   {

	protected static final String TAG = null;
	private Button btn_Sign_up,btn_Sign_In;
	Context context = MainActivity.this;
	JSONObject	json,json2;
	JSONArray ResponseData ;
	private String resultFromVerifyExistingUser = null;
    private String deviceId=null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		/**
		 *   Direct the user To the main page if the User has Login Once in the Application  
		 */

		deviceId =  Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		/**
		 * Checking the Internet Connectivity
		 */
		NetworkConnectivity networkConnectivity = new NetworkConnectivity(MainActivity.this);
		if(networkConnectivity.isNetworkAvailable() ==false)
		{
			NoInternetConnection noInternet = new NoInternetConnection(MainActivity.this);
			noInternet.displayAlertMessage("Please Connect to Working Internet Now ");

		}else{
			/**
			 * Verifying if the user clicked remember password in the last Login
			 */
			VerifyIfUserExisting(deviceId); 	
		}
		/**
		 * Initializing the Variables 
		 */
		setUpVariables() ;

	}
	
	/**
	 * VerifyIfUserExisting(String UniqueDeviceId)  - Verifying if the User already signed up
	 * If the User Signed in check if the remember password is checked 
	 * 
	 * @param UniqueDeviceId
	 */
	private void VerifyIfUserExisting(String UniqueDeviceId) {

		int UserExisting = 0 ;
		int RememberPassword = 0 ;

		VerifyExistingUser verifyExistingUser = new VerifyExistingUser(MainActivity.this); 	
		try {
			resultFromVerifyExistingUser = verifyExistingUser.execute(UniqueDeviceId).get();
         Log.e("LALITA TEST ",resultFromVerifyExistingUser);
       
         if(resultFromVerifyExistingUser.toString() == null)
         {
        	 NeutralDialog neutralDialog = new NeutralDialog(MainActivity.this);
        	 neutralDialog.displayAlertMessage("Alert", "Server Down");
        	 
         }
         
		} catch (InterruptedException | ExecutionException e1) {

			e1.printStackTrace();
		}		

		/**
		 *Json object for parsing the data(Channel Info) 
		 * 
		 */

		NetworkConnectivity networkConnectivity = new NetworkConnectivity(MainActivity.this);
		if(networkConnectivity.isNetworkAvailable() == true){

			try {

				json = new JSONObject(resultFromVerifyExistingUser);
				JSONObject results = json.getJSONObject("results") ;

				UserExisting =  results.getInt("response") ; 
				RememberPassword = results.getInt("autologinresponse") ; 
				Log.e(TAG," test 2UserExisting  " +UserExisting );	

			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(UserExisting == 1 )
			{ 
				if( RememberPassword == 1)
				{
					Intent i = new Intent(MainActivity.this,UserMainScreen.class);
					startActivity(i); 			
				}
			}
			
			
		}

		else
		{
			NoInternetConnection noInternet = new NoInternetConnection(MainActivity.this);
			noInternet.displayAlertMessage("Please Connect to Working Internet Now ");
		}

	}
	/**
	 * Initializing variables 
	 */
	private void setUpVariables() {
		btn_Sign_up = (Button)findViewById(R.id.btnSignUp);	
		btn_Sign_up.setOnClickListener(this);

		btn_Sign_In= (Button)findViewById(R.id.btnSignIn);	
		btn_Sign_In.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if(v.getId()==R.id.btnSignUp){
			Intent i = new Intent(MainActivity.this,SignUp.class);
			startActivity(i);

		}
		if(v.getId()==R.id.btnSignIn){
			Intent i = new Intent(MainActivity.this,SignIn.class);
			startActivity(i);
		}	
	}

	@Override
	public void onBackPressed()
	{
		finish();
		super.onBackPressed();  

	}



}
