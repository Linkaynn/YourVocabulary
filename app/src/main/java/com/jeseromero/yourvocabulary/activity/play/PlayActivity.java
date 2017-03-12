package com.jeseromero.yourvocabulary.activity.play;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeseromero.yourvocabulary.R;

public class PlayActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
	}
}
