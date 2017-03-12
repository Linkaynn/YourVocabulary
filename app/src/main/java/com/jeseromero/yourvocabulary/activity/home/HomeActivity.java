package com.jeseromero.yourvocabulary.activity.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.language.LanguageActivity;
import com.jeseromero.yourvocabulary.activity.vocabulary.VocabularyActivity;

public class HomeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		View.OnClickListener playListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO
			}
		};

		View.OnClickListener languageListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
			}
		};

		View.OnClickListener vocabularyListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(HomeActivity.this, VocabularyActivity.class));
			}
		};

		findViewById(R.id.play_image).setOnClickListener(playListener);
		findViewById(R.id.play_text).setOnClickListener(playListener);

		findViewById(R.id.languages_image).setOnClickListener(languageListener);
		findViewById(R.id.languages_text).setOnClickListener(languageListener);

		findViewById(R.id.vocabulary_image).setOnClickListener(vocabularyListener);
		findViewById(R.id.vocabulary_text).setOnClickListener(vocabularyListener);
	}
}
