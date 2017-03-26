package com.jeseromero.yourvocabulary.activity.word.manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.manager.LanguageManager;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.util.Translation;
import com.jeseromero.yourvocabulary.util.Translator;

public class ManageWordActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	public static final String WORD_ID = "WORD_ID";

	public static final String EDIT_ACTION = "EDIT_ACTION";

	private EditText translationEditText;

	private EditText valueEditText;

	private TextView languageTextView;

	private Language language;

	private Word word;

	private View suggestionLayout;

	private TextView suggestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_manage_word);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		valueEditText = (EditText) findViewById(R.id.word);

		languageTextView = (TextView) findViewById(R.id.language);

		translationEditText = (EditText) findViewById(R.id.translate);

		suggestionLayout = findViewById(R.id.suggest_layout);

		suggestion = ((TextView) findViewById(R.id.suggest));

		suggestion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				translationEditText.setText(suggestion.getText().toString());
				suggestionLayout.setVisibility(View.GONE);
			}
		});

		if (getIntent().getBooleanExtra(EDIT_ACTION, false)) {
			retrieveData();

			valueEditText.setText(word.getValue());

			languageTextView.setText(language.getName());

			translationEditText.setText(word.getTranslation());
		} else {
			long languageId = getIntent().getLongExtra(LANGUAGE_ID, -1);

			if (languageId != -1) {
				language = new LanguageManager().getLanguage(languageId);
				languageTextView.setText(language.getName());
			}
		}

		ListView languagesList = (ListView) findViewById(R.id.languages);

		languagesList.setAdapter(new LanguageAdapter(new LanguageManager().getAllLanguages(), this));

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

			toastError();
		}

		language = new LanguageManager().getLanguage(languageId);

		if (language == null) {
			finish();

			toastError();
		}

		long wordId = intent.getLongExtra(WORD_ID, -1);

		if (wordId == -1) {
			finish();

			toastError();
		}

		word = language.getWord(wordId);

		if (word == null) {
			finish();

			toastError();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.wordactions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_save:

				final String value = valueEditText.getText().toString();

				String translation = translationEditText.getText().toString();

				boolean failed = false;

				if (language != null) {
					for (Word word : language.getWords()) {
						if (word.getValue().equalsIgnoreCase(value) && word.getTranslation().equalsIgnoreCase(translation)) {
							translationEditText.setError("Already exist this combination of value - translation");
							failed = true;
						}
					}
				} else {
					Toast.makeText(this, "Select a language", Toast.LENGTH_SHORT).show();

					failed = true;
				}

				if (value.trim().isEmpty()) {
					valueEditText.setError("Can't be empty");
					failed = true;
				}

				if (translation.trim().isEmpty()) {
					translationEditText.setError("Can't be empty");
					failed = true;
				}

				if (!failed) {

					word.setValue(value);

					word.setTranslation(translation);

					word.save();

					toast("Saved " + value + " as " + translation + " to " + language.getName() + " language.").show();

					finish();

					return true;
				} else {
					return false;
				}

			case R.id.action_suggest:
				DialogBuilder.buildWarningDialogWithoutIcon(this,
						"Do you want a suggestion?",
						"If you want a suggestion, press OK. The suggested word will appear below the translation box, " +
								"click on the suggestion to set suggestion as translation.",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								if (!valueEditText.getText().toString().trim().isEmpty()) {
									try {
										Translation translationText = new Translator().translate(valueEditText.getText().toString());

										suggestionLayout.setVisibility(View.VISIBLE);

										suggestion.setText(translationText.getTranslationText());
									} catch (Exception e) {
										e.printStackTrace();

										Toast.makeText(ManageWordActivity.this, "Can'r retrieve the translation.", Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(ManageWordActivity.this, "First write the word, please.", Toast.LENGTH_SHORT).show();
								}
							}
						}, null);
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}
	}

	private Toast toast(String text) {
		return Toast.makeText(this, text, Toast.LENGTH_SHORT);
	}

	private void toastError() {
		toast("An error occurred.");
	}

}
