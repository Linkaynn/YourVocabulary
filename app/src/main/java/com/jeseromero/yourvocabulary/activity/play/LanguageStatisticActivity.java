package com.jeseromero.yourvocabulary.activity.play;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;
import com.jeseromero.yourvocabulary.persistence.StatisticsManager;

import az.plainpie.PieView;

public class LanguageStatisticActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_language_statistic);

		long languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);

		if (languageID == -1) {
			Toast.makeText(this, "Language not found", Toast.LENGTH_SHORT).show();

			finish();
		}

		Language language = new LanguageManager().selectLanguage(languageID);

		Statistic statistics = new StatisticsManager().getStatistics(language);

		TextView languageTextField = (TextView) findViewById(R.id.language);

		languageTextField.setText(language.getName());

		TextView correctAnswers = (TextView) findViewById(R.id.answers_count);

		correctAnswers.setText(String.valueOf(Float.valueOf(statistics.getCorrectAnswers()).intValue()));

		TextView wordsCount = (TextView) findViewById(R.id.words_count);

		wordsCount.setText(String.valueOf(Float.valueOf(statistics.getTries()).intValue()));

		PieView pieView = (PieView) findViewById(R.id.pie);

		pieView.setPercentage(statistics.getPercentage());
	}
}
