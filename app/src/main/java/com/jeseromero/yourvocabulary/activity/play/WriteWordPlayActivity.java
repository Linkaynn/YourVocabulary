package com.jeseromero.yourvocabulary.activity.play;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;
import com.jeseromero.yourvocabulary.persistence.StatisticsManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import az.plainpie.PieView;

public class WriteWordPlayActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	private Language language;

	private ArrayList<Word> remainingWords;

	private Statistic statistics;

	private PieView pieView;

	private TextView wordTextView;

	private Word lastWord;

	private Word rightWord;
	private EditText answerEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_word_play);

		final long languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);

		if (languageID == -1) {
			Toast.makeText(this, "Language not found", Toast.LENGTH_SHORT).show();

			finish();
		}

		language = new LanguageManager().selectLanguage(languageID);

		remainingWords = new ArrayList<>();

		for (Word word : language.getWords()) {
			remainingWords.add(word);
		}

		setTitle("You can with this");

		statistics = new StatisticsManager().getStatistic(language);

		statistics = new Statistic();

		statistics.setLanguage(language);

		pieView = (PieView) findViewById(R.id.pie);

		wordTextView = (TextView) findViewById(R.id.word);

		answerEditText = (EditText) findViewById(R.id.answer);

		answerEditText.setImeActionLabel("Check it!", KeyEvent.KEYCODE_ENTER);

		answerEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

				if (textView.getText().toString().equalsIgnoreCase(rightWord.getTranslation())) {
					statistics.newCorrectAnswer();

					remainingWords.remove(rightWord);

					textView.setText(null);

					if (remainingWords.isEmpty()) {
						statistics.setDate(new Date(System.currentTimeMillis()));

						statistics.save();

						Intent intent = new Intent(WriteWordPlayActivity.this, LanguageStatisticActivity.class);

						intent.putExtra(LanguageStatisticActivity.LANGUAGE_ID, languageID);

						startActivity(intent);

					} else {
						play();
					}

				} else {
					statistics.addTries();

					textView.setText(null);
				}

				pieView.setPercentage(statistics.getPercentage());

				return true;
			}
		});

		statistics.setCorrectAnswers(0);

		statistics.setTries(0);

		pieView.setPercentage(100);

		lastWord = null;

		play();
	}

	private void play() {

		Random random = new Random();

		rightWord = remainingWords.get(random.nextInt(remainingWords.size()));

		if (lastWord == null) {
			lastWord = rightWord;
		} else if (lastWord.equals(rightWord)) {
			play();
			return;
		}

		lastWord = rightWord;

		wordTextView.setText(rightWord.getValue());
	}
}
