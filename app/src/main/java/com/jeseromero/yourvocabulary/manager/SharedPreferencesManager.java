package com.jeseromero.yourvocabulary.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Version 1.0
 */

public class SharedPreferencesManager {

	private static SharedPreferences sharedPref;

	public static void init(Activity activity) {
		sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
	}

	public static void putInt(Key key, Integer integer) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(key.name, integer);
		editor.apply();
	}

	public static void putBoolean(Key key, Boolean aBoolean) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(key.name, aBoolean);
		editor.apply();
	}

	public static int getInt(Key key) {
		return sharedPref.getInt(key.name, key.defaultIntValue);
	}

	public static boolean getBoolean(Key key) {
		return sharedPref.getBoolean(key.name, key.defaultBooleanValue);
	}

}
