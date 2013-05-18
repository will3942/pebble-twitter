package com.definedcode.pebble_twitter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertManager {
	public void showAlertDialog(Context context, String title, String message) {
		AlertDialog alerter = new AlertDialog.Builder(context).create();
		
		alerter.setTitle(title);
		alerter.setMessage(message);
		
		alerter.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//should probably do some swag here
			}
		});
		
		alerter.show();
	}
}
