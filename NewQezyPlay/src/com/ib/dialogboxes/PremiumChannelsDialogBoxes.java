package com.ib.dialogboxes;

import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
import android.widget.Toast;
/**
 *PremiumChannelsDialogBoxes  - Implementation of the Dialog boxes for the Premium channels.
 *A dialog box would appear if the clicked  channel is paid  
 * @author Ideabytes
 * @version 1.0.0
 * 
 */
//TODO  :Integration with the Payment Gateway

public class PremiumChannelsDialogBoxes {
 
	private Context context = null;
/**
 * Constructor to pass the Reference for the UserMainScreen.this
 * @param a
 */
	public PremiumChannelsDialogBoxes(Context  a) {
		context= a;
	}

/**
 * PremiumDialogBox(String Message) - Dialog Box for the Paid Channels.Provides an option to buy Premium Channels
 * 
 * @param Message
 */
	public void PremiumDialogBox(String Message){
     
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
         // set title
		alertDialogBuilder.setTitle("Paid Channel");
		// set dialog message
		alertDialogBuilder
		.setMessage(Message)
		.setCancelable(false)
		.setPositiveButton("Buy",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				/****** will be directed to payement Gateway *******/
			  		Toast.makeText(context," Will be directed to Payment Gateway ", Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
 
}






