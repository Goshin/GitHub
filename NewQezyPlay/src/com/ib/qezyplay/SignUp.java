package com.ib.qezyplay;

 
import java.io.IOException;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity; 
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



public class SignUp extends  Activity     {



	protected static final String TAG = null;
	protected static final String MY_PREFS_NAME = null;
	Button btn_Sign_up,btn_Sign_In,btn_Sign_up_Second_Layout;
	EditText Enter_Email,Enter_User_Name,Enter_Password,Enter_Mobile_Number,Enter_Email_Sign_In,Enter_Password_Sign_In;
	Context context = SignUp.this;
	Boolean EmailFlag = false;
	Boolean EnteredAllFlag = false;
	Boolean PasswordFlag = false;
	Boolean UserNameFlag=false;
	Boolean MobileNumberFlag=false;
	JSONObject	json,json2;
	JSONArray ResponseData ;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	String MobilePattern = "[0-9]{10}";
	//////////////////////////////////

	JSONObject jsonobjectresponsedata;
	String resultFromVerifyExistingUser = null;
	int RememberMe = 0;
	CheckBox Check_Box_Sign_In;

	/*
	 * Device Unique Device ID
	 */
	private String deviceId = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
		setContentView(R.layout.activity_sign_up);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		deviceId = 	Secure.getString(this.getContentResolver(),
					Secure.ANDROID_ID);
		
		btn_Sign_up_Second_Layout = (Button)findViewById(R.id.btnSignUpTwo);

		Enter_Mobile_Number = (EditText)findViewById(R.id.etMobileNumber);		
		Enter_Email = (EditText)findViewById(R.id.etEmail);
		Enter_User_Name = (EditText)findViewById(R.id.etUserName);
		Enter_Password = (EditText)findViewById(R.id.etPass);

		btn_Sign_up_Second_Layout.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * Moving the Layout up   when the keyboard appears	
				 */
				if(Enter_Password.requestFocus())
				{
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(Enter_Password,InputMethodManager.SHOW_IMPLICIT);	
				}
				//Validation done before Signup button Click

				signUpValidations();

				Log.e(TAG, " **************EMAILfLAG  " +EmailFlag+ " EnteredAll flag  " +EnteredAllFlag+ "  passwordflag lalita  " + PasswordFlag);
				if(EmailFlag == true && EnteredAllFlag == true && PasswordFlag == true   && MobileNumberFlag == true )
				{


					try {
						com.ib.services.SignUp.postData((Enter_Mobile_Number.getText().toString()),(Enter_Email.getText().toString()),
								(Enter_User_Name.getText().toString()),
								(Enter_Password.getText().toString())
								,deviceId, getIP());
					} catch (JSONException
							| IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 			  
					Toast.makeText(context, "Sign up Successfull", Toast.LENGTH_LONG).show();

					Intent i = new Intent(SignUp.this,SignIn.class);
					startActivity(i);

				}					
			}
		});		
	}




	/*
	 * Sign Up Validations
	 */
	private void signUpValidations() {
		// TODO Auto-generated method stub	



		if(!Enter_Email.getText().equals(null) &&  !Enter_User_Name.getText().equals(null) &&  !Enter_Password.getText().equals(null)  && !Enter_Mobile_Number.getText().equals(null)  )			
		{
			EnteredAllFlag=true;

			if(Enter_Mobile_Number.getText().toString().length()!= 10 && !Enter_Email.getText().toString().trim().matches(MobilePattern))
			{
				Toast.makeText(context, "Mobile Number Length should be 10 Digits", Toast.LENGTH_LONG).show();
				Enter_Email.setText("");
			} 
			else
			{
				MobileNumberFlag = true;
			}
			if(!Enter_Email.getText().toString().trim().matches(emailPattern))			
			{
				Toast.makeText(context, "Invalid Email Address", Toast.LENGTH_LONG).show();
				Enter_Email.setText("");			
			}
			else
			{
				EmailFlag = true;
			}

			if(Enter_Password.getText().toString().length() < 8 )
			{
				Toast.makeText(context, "Password Length should be more than 7 characters ", Toast.LENGTH_LONG).show();
				Enter_Password.setText("");
			}
			else
			{
				PasswordFlag=true;
			}
		}
		else
		{
			Toast.makeText(context, "Please Enter the Missing Fields ", Toast.LENGTH_LONG).show();		
		}
	}	 


	private String getIP() {
		try {
			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			return String.format(Locale.getDefault(), "%d.%d.%d.%d",
					(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
					(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
		}
		catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
			return null;
		}
	}


	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(SignUp.this,MainActivity.class);
		startActivity(i); 
		super.onBackPressed();  

	}



 
}
