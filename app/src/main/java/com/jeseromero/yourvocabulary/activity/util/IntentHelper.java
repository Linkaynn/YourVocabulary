package com.jeseromero.yourvocabulary.activity.util;

import android.content.Intent;

/**
 * Version 1.0
 */

public class IntentHelper {

	public static Intent createShareText(String textToShare) {
		return IntentHelper.createShareText(textToShare, "Sent text to...");
	}

	public static Intent createShareText(String textToShare, String title) {
		Intent sendIntent = new Intent();

		sendIntent.setAction(Intent.ACTION_SEND);

		sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

		sendIntent.setType("text/plain");

		return Intent.createChooser(sendIntent, title);
	}
}
