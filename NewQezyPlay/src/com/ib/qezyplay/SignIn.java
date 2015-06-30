package com.ib.qezyplay;


import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity; 
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * SignIn -Implementation for the SignIn Function along with the Validations 
 * @author Ideabytes
 * @version 1.0.0
 *
 */

public class SignIn extends  Activity implements OnClickListener   {

	protected static final String TAG = null; 
	private Button Btn_Sign_In_Sign_Screen;
	private EditText Enter_Email_Sign_In,Enter_Password_Sign_In;
	private Context context = SignIn.this; 
	private JSONObject	json;
	private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
     private int RememberMe = 0;
	private CheckBox Check_Box_Sign_In;

	/*
	 * Device Unique Device ID
	 */
	String deviceId=null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_sign_in_screen);

		deviceId =  Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setUpVariables();
	}

	/**
	 * Initializing the Variables 
	 */

	private void setUpVariables() {

		Enter_Email_Sign_In = (EditText)findViewById(R.id.et_email_sign_in);		
		Enter_Password_Sign_In = (EditText)findViewById(R.id.et_password_sign_in);		
		Btn_Sign_In_Sign_Screen = (Button)findViewById(R.id.btn_SignIn);
		Btn_Sign_In_Sign_Screen.setOnClickListener(this);
		Check_Box_Sign_In = (CheckBox)findViewById(R.id.ch_rememberme);	
	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.btn_SignIn){

			if(Check_Box_Sign_In.isChecked())
				RememberMe = 1;

			int StatusCode = 0;
			String AccessToken = null;
			String StatusMessage= null; 
			String SignInEmailId = Enter_Email_Sign_In.getText().toString();
			String SignInPassWord  = Enter_Email_Sign_In.getText().toString();

			/**
			 * Checking the Entered Email Id Format and Password Not null
			 */
			SignInValidations(SignInEmailId, SignInPassWord, AccessToken,StatusMessage ) ;
			String DataFromSignInServiceCall = null;
			try {
				DataFromSignInServiceCall = com.ib.services.SignIn.postData((Enter_Email_Sign_In.getText().toString()),(Enter_Password_Sign_In.getText().toString()),RememberMe,deviceId);
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				json = new JSONObject(DataFromSignInServiceCall);
				JSONObject results = json.getJSONObject("results");					
				StatusCode=  results.getInt("status") ; 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(StatusCode == 200)
			{
				Intent i = new Intent(SignIn.this,UserMainScreen.class);
				startActivity(i);
			}
			else
			{
				Toast.makeText(getApplicationContext(),"Invalid Credentials,Please Re-enter",Toast.LENGTH_LONG ).show();
			}
		}
	}
	/**
	 * Validations for the SignIn 
	 * @param SignInEmailId
	 * @param SignInPassWord
	 * @param AccessToken
	 * @param Message
	 */
	private  void  SignInValidations(String SignInEmailId,String SignInPassWord,String AccessToken, String Message) {
		if(Message != null){
			Toast.makeText(getApplicationContext(),  Message, Toast.LENGTH_LONG ).show();
		}
		if(!SignInEmailId.trim().matches(emailPattern) )			
		{
			Toast.makeText(context, "Invalid Email Address", Toast.LENGTH_LONG).show();
			Enter_Email_Sign_In.setText("");			
		}
		if(SignInPassWord == null || SignInEmailId == null)
		{
			Toast.makeText(getApplicationContext(), "Please Enter the Empty Fields", Toast.LENGTH_LONG ).show();
		}
	}

	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(SignIn.this,MainActivity.class);
		startActivity(i); 
		super.onBackPressed();  

	}




}


