package com.jeseromero.yourvocabulary.activity.intent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.home.HomeActivity;
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.manager.LanguageManager;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.util.Translation;
import com.jeseromero.yourvocabulary.util.Translator;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class ReceiveTextActivity extends AppCompatActivity {

	private EditText translationEditText;

	private ListView languagesList;

	private TextView wordTextView;

	private TextView languageTextView;

	private Language language = null;

	private View suggestionLayout;

	private TextView suggestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recieve_text);

		Fabric.with(this, new Crashlytics());

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		wordTextView = (TextView) findViewById(R.id.word);

		languageTextView = (TextView) findViewById(R.id.language);

		translationEditText = (EditText) findViewById(R.id.translate);

		languagesList = (ListView) findViewById(R.id.languages);

		suggestionLayout = findViewById(R.id.suggest_layout);

		suggestion = ((TextView) findViewById(R.id.suggest));

		suggestion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				translationEditText.setText(suggestion.getText().toString());
				suggestionLayout.setVisibility(View.GONE);
			}
		});

		ArrayList<Language> languages = new LanguageManager().getAllLanguages();

		if (languages.isEmpty()) {
			Toast.makeText(this, "No language found", Toast.LENGTH_SHORT).show();

			finish();
		}

		languagesList.setAdapter(new LanguageAdapter(languages, this));

		languagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				language = (Language) view.getTag();
				languageTextView.setText(language.getName());
			}
		});

		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent);
			}
		} else {
			startActivity(new Intent(this, HomeActivity.class));
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

				String value = wordTextView.getText().toString();

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

				if (translation.trim().isEmpty()) {
					translationEditText.setError("Can't be empty");
					failed = true;
				}

				if (!failed) {

					language.addWord(new Word(value, translation));

					language.saveAll();

					Toast.makeText(this, "Added " + value + " as " + translation + " to " + language.getName() + " language.", Toast.LENGTH_SHORT).show();

					finish();

				} else {
					return false;
				}

				Answers.getInstance().logCustom(new CustomEvent("New word").putCustomAttribute("Language", language.getName()).putCustomAttribute("Value", value).putCustomAttribute("Translation", translation));

				return true;
			case R.id.action_suggest:

				DialogBuilder.buildWarningDialogWithoutIcon(this,
						"Do you want a suggestion?",
						"If you want a suggestion, press OK. The suggested word will appear below the translation box, " +
						"click on the suggestion to set suggestion as translation.",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								if (!wordTextView.getText().toString().trim().isEmpty()) {
									try {
										Translation translationText = new Translator().translate(wordTextView.getText().toString());

										suggestionLayout.setVisibility(View.VISIBLE);

										suggestion.setText(translationText.getTranslationText());
									} catch (Exception e) {
										e.printStackTrace();

										Toast.makeText(ReceiveTextActivity.this, "Can'r retrieve the translation.", Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(ReceiveTextActivity.this, "The word is empty. Something wrong?", Toast.LENGTH_SHORT).show();
								}
							}
				}, null);
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}
	}

	private void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {

			Log.d("YOUR_VOCABULARY", "RECEIVED TEXT: " + sharedText);

			if (sharedText.matches(".*[@:#].*")) {
				Toast.makeText(this, "Shared text contains illegal characters like '@:#', remove it first", Toast.LENGTH_LONG).show();

				finish();

				return;
			}

			wordTextView.setText(sharedText);
		}
	}
}
