package org.sharedsolar.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyUI {
	public static void showNeutralDialog(Context context, int title,
			int message, int btn) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNeutralButton(btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	public static void showNeutralDialog(Context context, int title,
			String message, int btn) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNeutralButton(btn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	public static void showlDialog(Context context, int title,
			String message, int posBtn, int negBtn, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posBtn, listener);
		builder.setNegativeButton(negBtn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
	} 
}
