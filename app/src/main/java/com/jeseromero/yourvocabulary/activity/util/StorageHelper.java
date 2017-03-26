package com.jeseromero.yourvocabulary.activity.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.jeseromero.yourvocabulary.activity.intent.ReceiveFileActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Version 1.0
 */

public class StorageHelper {

	private Context context;

	public StorageHelper(Context context) {
		this.context = context;
	}

	public File createFile(String fileName) throws IOException {
		File file;

		if (isExternalStorageWritable()) {
			file = new File(context.getExternalCacheDir(), fileName);
		} else {
			file = new File(context.getFilesDir(), fileName);
		}

		return file;
	}

	private boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	private boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	public static String readFile(Activity activity, Uri data) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getContentResolver().openInputStream(data)));

		StringBuilder stringBuilder = new StringBuilder();

		String line;

		while ((line = reader.readLine()) != null) {

			stringBuilder.append(line);
			stringBuilder.append('\n');
		}

		reader.close();

		return stringBuilder.toString();
	}
}
