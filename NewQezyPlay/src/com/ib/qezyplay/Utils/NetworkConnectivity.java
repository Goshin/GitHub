package com.ib.qezyplay.Utils;


import com.ib.dialogboxes.NeutralDialog;
import android.content.Context; 
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkConnectivity -Implements the Method  isNetworkAvailable() that checks if there is an active Network Connectivity  
 * @author lalita
 *@version 1.0.0
 */


public class NetworkConnectivity  {
 
	private  Context context = null ;

	/**
	 * Contructor for the passing the Reference of the  UserMainScreen(Main Activity)
	 * @param a
	 */
	public NetworkConnectivity(Context a){
		context = a;
	}

	/**
	 * isNetworkAvailable() -  Checks if there is an Active Network Connectivity 
	 * @return
	 */
	public  boolean isNetworkAvailable() {
		ConnectivityManager cn=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nf=cn.getActiveNetworkInfo();
		if(nf != null && nf.isConnected()==true ){
			return true;
		}
		else{
			NeutralDialog neutralDialog = new NeutralDialog(context);
			neutralDialog.displayAlertMessage("Alert","Internet Connection Not Available! Please Connect To Working Internet");		 
			return false;
		}

	}
}








