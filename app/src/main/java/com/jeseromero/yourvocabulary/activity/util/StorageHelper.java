package com.jeseromero.yourvocabulary.activity.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

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
}
