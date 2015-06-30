package com.ib.dialogboxes;

import org.json.JSONArray;
import org.json.JSONObject; 
import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
 

/**
 * NeutralDialog - This is the Generic DialogBox class for all the  Messages supported by one neutral button  "OK"
 * @author Ideabytes 
 * @version 1.0.0S
 * 
 */

public class NeutralDialog  {

 
	public  Context context = null ;
	JSONArray ResponseChannelInfo ,ResponseCountryInfo,ResponseCategoryInfo ;
	JSONObject json;
	/**
	 * Contructor for passing the Reference of UserMainSreen.java
	 * @param a
	 */
	public NeutralDialog(Context  a) 
	{
		context = a;		 
	}
	/**
	 *displayAlertMessage(final String Title,final String Message) - A generic message for the dialog boxes 
	 * @param Title
	 * @param Message
	 */
	public   void  displayAlertMessage(final String Title,final String Message){
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(	context);
		// set title
		alertDialogBuilder.setTitle(Title);
		// set dialog message
		alertDialogBuilder
		.setMessage(Message)
		.setCancelable(false)
		.setNeutralButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				alertDialogBuilder.create().cancel();
			}
		});


		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}



}





