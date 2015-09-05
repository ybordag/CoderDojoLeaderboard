package com.example.test2;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by LouisaSeever on 7/30/2015.
 */
public class ApplicationUtilities {
    public static void showMessage(Context context, String message){
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(context);
        dlgBuilder.setTitle("Error Encountered");
        dlgBuilder.setMessage(message);
        dlgBuilder.setCancelable(true);
//	    dlgBuilder.setPositiveButton("Yes",
//	            new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int id) {
//	            dialog.cancel();
//	        }
//	    });
//	    dlgBuilder.setNegativeButton("No",
//	            new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int id) {
//	            dialog.cancel();
//	        }
//	    });

        AlertDialog alert = dlgBuilder.create();
        alert.show();
    }}
