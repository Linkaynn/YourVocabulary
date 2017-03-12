package com.jeseromero.yourvocabulary.activity.play;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;
import com.jeseromero.yourvocabulary.persistence.StatisticsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import az.plainpie.PieView;

public class PlayActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	private PieView pieView;

	private TextView wordTextView;

	private Button answer1;

	private Button answer2;

	private Button answer3;

	private Button answer4;

	private Language language;

	private Statistic statistics;

	private Word rightWord;

	private Word lastWord;

	private int repetitions = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		final long languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);

		if (languageID == -1) {
			Toast.makeText(this, "Language not found", Toast.LENGTH_SHORT).show();

			finish();
		}

		language = new LanguageManager().selectLanguage(languageID);

		statistics = new StatisticsManager().getStatistic(language);

		statistics = new Statistic();

		statistics.setLanguage(language);

		pieView = (PieView) findViewById(R.id.pie);

		wordTextView = (TextView) findViewById(R.id.word);

		answer1 = (Button) findViewById(R.id.answer_1);
		answer2 = (Button) findViewById(R.id.answer_2);
		answer3 = (Button) findViewById(R.id.answer_3);
		answer4 = (Button) findViewById(R.id.answer_4);

		final View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				repetitions++;

				Word word = (Word) view.getTag();

				if (word.equals(rightWord)) {
					statistics.newCorrectAnswer();

					play();

					answer1.setOnClickListener(this);
					answer2.setOnClickListener(this);
					answer3.setOnClickListener(this);
					answer4.setOnClickListener(this);

					answer1.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorAccent));
					answer2.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorAccent));
					answer3.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorAccent));
					answer4.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorAccent));
				} else {
					statistics.addTries();

					view.setBackgroundColor(ContextCompat.getColor(PlayActivity.this, R.color.colorPrimaryDark));

					view.setOnClickListener(null);
				}

				pieView.setPercentage(statistics.getPercentage());

				if (repetitions >= language.getWords().size() * 3) {
					statistics.setDate(new Date(System.currentTimeMillis()));

					statistics.save();

					Intent intent = new Intent(PlayActivity.this, LanguageStatisticActivity.class);

					intent.putExtra(LanguageStatisticActivity.LANGUAGE_ID, languageID);

					startActivity(intent);
				}
			}
		};

		answer1.setOnClickListener(onClickListener);
		answer2.setOnClickListener(onClickListener);
		answer3.setOnClickListener(onClickListener);
		answer4.setOnClickListener(onClickListener);

		statistics.setCorrectAnswers(0);

		statistics.setTries(0);

		pieView.setPercentage(1);

		lastWord = null;

		play();

		repetitions++;
	}

	private void play() {

		ArrayList<Word> words = language.getWords();

		Random random = new Random();

		Word word1 = words.get(random.nextInt(words.size()));
		Word word2 = words.get(random.nextInt(words.size()));
		Word word3 = words.get(random.nextInt(words.size()));
		Word word4 = words.get(random.nextInt(words.size()));

		if (someEquals(word1, word2, word3, word4)) {
			play();
			return;
		}

		rightWord = chooseRightWord(word1, word2, word3, word4);

		if (rightWord == null) {
			play();
			return;
		}

		if (lastWord == null) {
			lastWord = rightWord;
		} else if (lastWord.equals(rightWord)) {
			play();
			return;
		}

		lastWord = rightWord;

		wordTextView.setText(rightWord.getValue());

		setAnswers(word1, word2, word3, word4);
	}

	private void setAnswers(Word word1, Word word2, Word word3, Word word4) {
		ArrayList<Word> words = new ArrayList<>(Arrays.asList(word1, word2, word3, word4));
		ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(answer1, answer2, answer3, answer4));

		for (int i = 0; i < 4; i++) {
			Button randomButton = buttons.get(new Random().nextInt(buttons.size()));

			Word word = words.get(i);

			randomButton.setText(word.getTranslation());

			randomButton.setTag(word);

			buttons.remove(randomButton);
		}
	}

	@Nullable
	private Word chooseRightWord(Word word1, Word word2, Word word3, Word word4) {
		Random random = new Random();

		Word rightWord = null;

		switch (random.nextInt(4)) {
			case 0:
				rightWord = word1;
				break;
			case 1:
				rightWord = word2;
				break;
			case 2:
				rightWord = word3;
				break;
			case 3:
				rightWord = word4;
				break;
		}
		return rightWord;
	}

	private boolean someEquals(Word word1, Word word2, Word word3, Word word4) {
		return word1.equals(word2) || word1.equals(word3) || word1.equals(word4) ||
				word2.equals(word3) || word2.equals(word4) ||
				word3.equals(word4);
	}
}
