package com.jeseromero.yourvocabulary.activity.play;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.manager.LanguageManager;
import com.jeseromero.yourvocabulary.manager.StatisticsManager;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;
import com.jeseromero.yourvocabulary.model.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import az.plainpie.PieView;

public class WriteWordPlayActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	private ArrayList<Word> remainingWords;

	private Statistic statistics;

	private PieView performancePie;

	private TextView wordTextView;

	private Word lastWord;

	private Word rightWord;

	private PieView progressPie;
	private TextView hintTextView;
	private EditText answerEditText;
	private long languageID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_word_play);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);

		if (languageID == -1) {
			Toast.makeText(this, "Language not found", Toast.LENGTH_SHORT).show();

			finish();
		}

		final Language language = new LanguageManager().getLanguage(languageID);

		remainingWords = new ArrayList<>();

		for (Word word : language.getWords()) {
			remainingWords.add(word);
		}

		setTitle("You can with this");

		statistics = new StatisticsManager().getStatistic(language);

		statistics = new Statistic();

		statistics.setLanguage(language);

		performancePie = (PieView) findViewById(R.id.pie);
		progressPie = (PieView) findViewById(R.id.progress);

		wordTextView = (TextView) findViewById(R.id.word);
		hintTextView = (TextView) findViewById(R.id.hint);

		answerEditText = (EditText) findViewById(R.id.answer);

		answerEditText.setImeActionLabel("Check it!", KeyEvent.KEYCODE_ENTER);

		answerEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

				String answer = textView.getText().toString();

				if (!answer.trim().isEmpty()) {
					if (answer.equalsIgnoreCase(rightWord.getValue())) {
						statistics.newCorrectAnswer();

						remainingWords.remove(rightWord);

						textView.setText(null);

						hintTextView.setVisibility(View.GONE);

						if (remainingWords.isEmpty()) {
							finishGame();
						} else {
							play();
						}

					} else {
						statistics.addTry();

						textView.setText(null);

						for (Word word : language.getWords()) {
							if (answer.equalsIgnoreCase(word.getValue())) {
								Toast.makeText(WriteWordPlayActivity.this, answer + " means " + word.getTranslation(), Toast.LENGTH_LONG).show();
							}
						}
					}

					updatePies();
				}

				return true;
			}
		});

		statistics.setCorrectAnswers(0);

		statistics.setTries(0);

		performancePie.setPercentage(100);

		progressPie.setPercentage(1);

		lastWord = null;

		play();
	}

	private void updatePies() {
		performancePie.setPercentage(statistics.getPercentage());

		progressPie.setPercentage(statistics.getPercentageOfTotal());
	}

	private void play() {

		if (!remainingWords.isEmpty()) {
			Random random = new Random();

			rightWord = remainingWords.get(random.nextInt(remainingWords.size()));

			if (lastWord == null) {
				lastWord = rightWord;
			} else if (lastWord.equals(rightWord)) {
				play();
				return;
			}

			lastWord = rightWord;

			wordTextView.setText(rightWord.getTranslation());
		}
	}

	private String generateHint() {
		char[] rightWordChars = rightWord.getValue().toCharArray();

		Random random = new Random();

		rightWordChars[0] = '_';

		for (int i = 2; i < rightWordChars.length; i++) {
			if (random.nextBoolean()) {
				rightWordChars[i] = '_';
			}
		}

		return Arrays.toString(rightWordChars).replace("[", "").replace("]", "").replace(", ", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.writewordactions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_suggest:
				DialogBuilder.buildWarningDialog(WriteWordPlayActivity.this, "Hint!", "Do you want a hint?\nThis action will increase you number of tries.", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						hintTextView.setVisibility(View.VISIBLE);

						hintTextView.setText(generateHint());

						statistics.addTry();

						performancePie.setPercentage(statistics.getPercentage());
					}
				});
				break;
			case R.id.action_skip:
				DialogBuilder.buildWarningDialogWithoutIcon(WriteWordPlayActivity.this, "Skip word", "Do you want to skip the word?\n   This action will increase your number of tries in 1.", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						statistics.addSkiped();

						answerEditText.setText(null);

						remainingWords.remove(rightWord);

						hintTextView.setVisibility(View.GONE);

						updatePies();

						if (remainingWords.isEmpty()) {
							finishGame();
						} else {
							play();
						}
					}
				}, null);
		}

		return true;
	}

	private void finishGame() {
		statistics.setDate(new Date(System.currentTimeMillis()));

		statistics.save();

		Intent intent = new Intent(WriteWordPlayActivity.this, LanguageStatisticActivity.class);

		intent.putExtra(LanguageStatisticActivity.LANGUAGE_ID, languageID);

		startActivity(intent);
	}
}
