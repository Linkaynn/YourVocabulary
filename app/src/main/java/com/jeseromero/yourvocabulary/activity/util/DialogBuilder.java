package com.jeseromero.yourvocabulary.activity.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.jeseromero.yourvocabulary.R;

/**
 * Version 1.0
 */

public class DialogBuilder {

	public static void buildChooserDialog(Context context, String title, CharSequence[] actions, DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setItems(actions, onClickListener);

		builder.show();
	}

	public static void buildWarningDialog(Context context, String title, String message, DialogInterface.OnClickListener onClickListener) {
		new AlertDialog.Builder(context)
				.setIcon(R.drawable.warning)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("Yes", onClickListener)
				.setNegativeButton("No", null)
				.show();

	}

	public static void buildWarningDialogWithoutIcon(Context context, String title, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.yes, yesListener)
				.setNegativeButton(android.R.string.no, noListener)
				.show();
	}

	public static void buildInfoDialog(Context context, String title, String message) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.yes, null)
				.show();
	}
}
