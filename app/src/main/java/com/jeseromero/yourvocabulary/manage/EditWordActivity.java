package com.jeseromero.yourvocabulary.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.manage.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

public class EditWordActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	public static final String WORD_ID = "WORD_ID";

	private EditText translationEditText;

	private TextView wordTextView;

	private TextView languageTextView;

	private Language language;

	private Word word;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recieve_text);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		retrieveData();

		wordTextView = (TextView) findViewById(R.id.word);

		wordTextView.setText(word.getValue());

		languageTextView = (TextView) findViewById(R.id.language);

		languageTextView.setText(language.getName());

		translationEditText = (EditText) findViewById(R.id.translate);

		translationEditText.setText(word.getTranslation());

		ListView languagesList = (ListView) findViewById(R.id.languages);

		languagesList.setAdapter(new LanguageAdapter(new LanguageManager().selectAll(), this));

		languagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				language = (Language) view.getTag();
				languageTextView.setText(language.getName());
			}
		});

	}

	private void retrieveData() {
		Intent intent = getIntent();

		long languageId = intent.getLongExtra(LANGUAGE_ID, -1);

		if (languageId == -1) {
			finish();

			toast("Do not exist the language of the word");
		}

		language = new LanguageManager().selectLanguage(languageId);

		if (language == null) {
			finish();

			toast("Do not exist the language of the word");
		}

		long wordId = intent.getLongExtra(WORD_ID, -1);

		if (wordId == -1) {
			finish();

			toast("Do not exist the word");
		}

		word = language.getWord(wordId);

		if (word == null) {
			finish();

			toast("Do not exist the word");
		}

		language.removeWord(word);

		LanguageWord languageWord = word.getRelation();

		if (languageWord == null) {
			finish();

			toast("Do not exist the word");
		} else {
			languageWord.delete();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.text_receiver, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_save:

				String value = wordTextView.getText().toString();

				String translation = translationEditText.getText().toString();

				word.setValue(value);

				word.setTranslation(translation);

				language.addWord(word);

				language.saveAll();

				new LanguageWord(language, word).save();

				toast("Saved " + value + " as " + translation + " to " + language.getName() + " language.").show();

				finish();

				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}

	private Toast toast(String text) {
		return Toast.makeText(this, text, Toast.LENGTH_SHORT);
	}

}
