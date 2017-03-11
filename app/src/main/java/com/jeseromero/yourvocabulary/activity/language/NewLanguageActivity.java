package com.jeseromero.yourvocabulary.activity.language;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.ArrayList;

import static com.jeseromero.yourvocabulary.R.id.language;

public class NewLanguageActivity extends AppCompatActivity {

	private EditText languageEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_language);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		languageEditText = ((EditText) findViewById(language));
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


				String name = languageEditText.getText().toString();

				ArrayList<Language> languages = new LanguageManager().selectAll();

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

				Language language = new Language(name);

				language.saveAll();

				Toast.makeText(this, "Added new language: " + name + ".", Toast.LENGTH_SHORT).show();

				finish();

				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}
}
