package com.jeseromero.yourvocabulary.activity.language;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.manager.LanguageManager;

import java.util.ArrayList;

/**
 * This class allow as long Extra a Language ID. Key: {@link ManageLanguageActivity#LANGUAGE_ID}
 * If LANGUAGE_ID is not setted, its just a new language activity.
 */
public class ManageLanguageActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	private EditText languageEditText;

	private Language language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_language);

		long languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);

		if (languageID != -1) {
			language = new LanguageManager().getLanguage(languageID);
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		languageEditText = ((EditText) findViewById(R.id.language));

		if (language != null) {
			languageEditText.setText(language.getName());
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

				String name = languageEditText.getText().toString();

				ArrayList<Language> languages = new LanguageManager().getAllLanguages();

				boolean exist = false;

				for (Language language : languages) {
					if (language.getName().equalsIgnoreCase(name)) {
						exist = true;
					}
				}

				if (exist) {
					languageEditText.setError("Already exist");
					return false;
				}

				if (name.trim().isEmpty()) {
					languageEditText.setError("Can't be empty");
					return false;
				}

				String message;
				if (language != null) {
					language.setName(name);

					language.save();

					message = language.getName() + " updated.";
				} else {
					Language language = new Language(name);

					language.saveAll();

					message = "Added new language: " + name + ".";
				}


				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

				finish();

				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}
}
