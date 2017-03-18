package com.jeseromero.yourvocabulary.activity.share;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.util.IntentHelper;

public class MainShareActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_share);

		View.OnClickListener appListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(IntentHelper.createShareText("https://play.google.com/store/apps/details?id=com.jeseromero.yourvocabulary"));
			}
		};

		View.OnClickListener vocabularyListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		};

		findViewById(R.id.app_image).setOnClickListener(appListener);
		findViewById(R.id.app_text).setOnClickListener(appListener);

		findViewById(R.id.vocabulary_image).setOnClickListener(vocabularyListener);
		findViewById(R.id.vocabulary_text).setOnClickListener(vocabularyListener);
	}
}
