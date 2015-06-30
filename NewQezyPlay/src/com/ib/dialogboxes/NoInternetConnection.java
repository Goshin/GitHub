package com.ib.dialogboxes;
 
import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
 



public class NoInternetConnection  {

	 
	public  Context context = null ;

	public NoInternetConnection(Context  a) 
	{
		context = a;		 
	}

	public   void  displayAlertMessage(final String Message){
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(	context);
		// set title
		alertDialogBuilder.setTitle("Alert");
		// set dialog message
		alertDialogBuilder
		.setMessage(Message)
		.setCancelable(false)
		.setNeutralButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				 	alertDialogBuilder.create().cancel();
				System.exit(1);
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
 
}





