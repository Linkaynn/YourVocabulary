package com.jeseromero.yourvocabulary.activity.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jeseromero.yourvocabulary.R;

import java.util.zip.Inflater;

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
}
