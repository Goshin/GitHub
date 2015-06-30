package com.ib.menuOptions;

import com.ib.dialogboxes.NeutralDialog;
import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
 
public class UserImageList  implements android.view.View.OnClickListener {
    
	public  Context context = null ;
	CharSequence[] items = {"Profile","About Us","Help" ,"Subscriptions","Organise","Contact Us" };

	public UserImageList(Context  a) 
	{
		context = a;		 
	}
	@Override
	public void onClick(View v) {

		/************ ListView for the User Items in the Menu ************/

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				callDialogListItem(item);

			}
		});

		AlertDialog dialog = builder.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

		wmlp.gravity = Gravity.TOP | Gravity.END;
		wmlp.x =0;   //x position
		wmlp.y = 100;   //y position

		dialog.show();
		dialog.getWindow().setLayout(400,LayoutParams.WRAP_CONTENT);

	}


	private void callNeutralDialog(String Title, String Message) {

		NeutralDialog neutralDialog = new NeutralDialog(context);
		neutralDialog .displayAlertMessage(Title,Message);

	}

	private void callDialogListItem(int item) {

		/******** Profile ************/
		switch(item) {
		case 0:{

		}
		break;

		/************ About Us ************/
		case 1: {

			callNeutralDialog("About Us","Ideabytes is a Canadian Corporation, Headquartered in Ottawa Canada with Development Center in Hyderabad India with additional offices in San Jose and Kuala Lumpur."
					+ "\n\nIdeabytes leadership comprises Global Management team from Canada, Sweden and India."
					+"Broad experience from diverse Industry segments driven with high passion and Innovation"
					+"Key concept “ Innovation is Business TM ”");
		}
		break;
		/************Help*************/
		case 2: {


		}
		break;
		/******User Subscriptions***********/
		case 3:
		{

		}
		break;
		/***********Organise********/
		case 4:
		{

		}
		break;
		/***********Contact Us************/ 
		case 5: 
		{

			callNeutralDialog("Contact Us","INDIA"
					+"\nIdeabytes Software India Pvt Ltd"
					+"\n#50, Jayabheri Enclave, Gachibowli"
					+"\nHyderabad – 500 032"
					+"\nPhone: +91-40-6453 5959"
					+"\nMail to: contact@ideabytes.com");
		}
		break;
		default:
			break;
		}
	}



}





