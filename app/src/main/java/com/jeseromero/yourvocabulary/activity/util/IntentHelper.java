package com.jeseromero.yourvocabulary.activity.util;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Version 1.0
 */

public class IntentHelper {

	public static Intent createShareTextIntent(String textToShare) {
		return IntentHelper.createShareTextIntent(textToShare, "Sent text to...");
	}

	public static Intent createShareTextIntent(String textToShare, String title) {
		Intent sendIntent = new Intent();

		sendIntent.setAction(Intent.ACTION_SEND);

		sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

		sendIntent.setType("text/plain");

		return Intent.createChooser(sendIntent, title);
	}

	public static Intent createShareFileIntent(File fileToShare, String title) {
		Intent sendIntent = new Intent();

		sendIntent.setAction(Intent.ACTION_SEND);

		sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileToShare));

		sendIntent.setType("application/*");

		return Intent.createChooser(sendIntent, title);
	}

	public static Intent createBrowserIntent(String url) {
		return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	}
}
