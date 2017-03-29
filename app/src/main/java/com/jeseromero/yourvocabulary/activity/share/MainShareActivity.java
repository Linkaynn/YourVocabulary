package com.jeseromero.yourvocabulary.activity.share;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.language.ManageLanguageActivity;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.activity.util.IntentHelper;
import com.jeseromero.yourvocabulary.activity.util.JSONHelper;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.manager.LanguageManager;

import java.io.IOException;
import java.util.ArrayList;

public class MainShareActivity extends AppCompatActivity {

	public static final int SHARE_VOCABULARY_REQUEST_CODE = 1;

	private LanguageManager languageManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_share);

		languageManager = new LanguageManager();

		View.OnClickListener appListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(IntentHelper.createShareTextIntent("https://play.google.com/store/apps/details?id=com.jeseromero.yourvocabulary"));

				Answers.getInstance().logShare(new ShareEvent().putMethod("App"));
			}
		};

		View.OnClickListener paypalListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(IntentHelper.createBrowserIntent("https://www.paypal.me/JeseRomeroArbelo"));

				Answers.getInstance().logShare(new ShareEvent().putMethod("Donate"));
			}
		};

		View.OnClickListener vocabularyListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!languageManager.getAllLanguages().isEmpty()) {
					int permissionCheck = ContextCompat.checkSelfPermission(MainShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

					if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(MainShareActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, SHARE_VOCABULARY_REQUEST_CODE);
					} else {
						shareAllVocabulary();
						Answers.getInstance().logShare(new ShareEvent().putMethod("Vocabulary"));
					}
				} else {
					DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							startActivity(new Intent(MainShareActivity.this, ManageLanguageActivity.class));
						}
					};
					DialogBuilder.buildWarningDialogWithoutIcon(MainShareActivity.this, "You need a language", "No language detected. Do you want to create one?", yesListener, null);
				}
			}
		};

		findViewById(R.id.app_image).setOnClickListener(appListener);
		findViewById(R.id.app_text).setOnClickListener(appListener);

		findViewById(R.id.vocabulary_image).setOnClickListener(vocabularyListener);
		findViewById(R.id.vocabulary_text).setOnClickListener(vocabularyListener);

		findViewById(R.id.paypal_image).setOnClickListener(paypalListener);
		findViewById(R.id.paypal_text).setOnClickListener(paypalListener);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

		switch (requestCode) {
			case SHARE_VOCABULARY_REQUEST_CODE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					shareAllVocabulary();
				}
			}
		}
	}

	private void shareAllVocabulary() {
		ArrayList<Language> languages = languageManager.getAllLanguages();

		try {
			startActivity(IntentHelper.createShareFileIntent(new JSONHelper(this).toFile(languages, "Vocabulary"), "Share your vocabulary"));
		} catch (IOException e) {
			Crashlytics.logException(e);
			Toast.makeText(MainShareActivity.this, "Can't create your vocabulary file", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
