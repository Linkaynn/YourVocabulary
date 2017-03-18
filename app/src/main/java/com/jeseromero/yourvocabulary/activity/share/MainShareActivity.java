package com.jeseromero.yourvocabulary.activity.share;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.util.IntentHelper;
import com.jeseromero.yourvocabulary.activity.util.JSONHelper;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

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
			}
		};

		View.OnClickListener vocabularyListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!languageManager.selectAll().isEmpty()) {
					int permissionCheck = ContextCompat.checkSelfPermission(MainShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

					if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(MainShareActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, SHARE_VOCABULARY_REQUEST_CODE);
					} else {
						shareAllVocabulary();
					}
				} else {
					Toast.makeText(MainShareActivity.this, "Add a language with vocabulary first", Toast.LENGTH_SHORT).show();
				}
			}
		};

		findViewById(R.id.app_image).setOnClickListener(appListener);
		findViewById(R.id.app_text).setOnClickListener(appListener);

		findViewById(R.id.vocabulary_image).setOnClickListener(vocabularyListener);
		findViewById(R.id.vocabulary_text).setOnClickListener(vocabularyListener);
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
		ArrayList<Language> languages = languageManager.selectAll();

		try {
			startActivity(IntentHelper.createShareFileIntent(new JSONHelper(this).toFile(languages, "Vocabulary"), "Share your vocabulary"));
		} catch (IOException e) {
			Toast.makeText(MainShareActivity.this, "Can't create your vocabulary file to share it", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
