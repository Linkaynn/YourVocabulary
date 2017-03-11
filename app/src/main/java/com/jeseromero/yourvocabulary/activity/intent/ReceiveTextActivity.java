package com.jeseromero.yourvocabulary.activity.intent;

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
import com.jeseromero.yourvocabulary.activity.home.HomeActivity;
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

public class ReceiveTextActivity extends AppCompatActivity {

	private EditText translationEditText;

	private ListView languagesList;

	private TextView wordTextView;

	private TextView languageTextView;

	private Language language;

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

		languagesList.setAdapter(new LanguageAdapter(new LanguageManager().selectAll(), this));

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
		getMenuInflater().inflate(R.menu.text_receiver, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_save:

				String value = wordTextView.getText().toString();

				String translation = translationEditText.getText().toString();

				language.addWord(new Word(value, translation));

				language.saveAll();

				Toast.makeText(this, "Added " + value + " as " + translation + " to " + language.getName() + " language.", Toast.LENGTH_SHORT).show();

				finish();

				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}

	private void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			wordTextView.setText(sharedText);
		}
	}
}
