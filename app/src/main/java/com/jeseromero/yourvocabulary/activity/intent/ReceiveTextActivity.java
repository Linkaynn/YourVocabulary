package com.jeseromero.yourvocabulary.activity.intent;

import android.content.Intent;
import android.os.Bundle;
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

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.home.HomeActivity;
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.ArrayList;

public class ReceiveTextActivity extends AppCompatActivity {

	private EditText translationEditText;

	private ListView languagesList;

	private TextView wordTextView;

	private TextView languageTextView;

	private Language language = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recieve_text);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		wordTextView = (TextView) findViewById(R.id.word);

		languageTextView = (TextView) findViewById(R.id.language);

		translationEditText = (EditText) findViewById(R.id.translate);

		languagesList = (ListView) findViewById(R.id.languages);

		ArrayList<Language> languages = new LanguageManager().selectAll();

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
		getMenuInflater().inflate(R.menu.save, menu);
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
