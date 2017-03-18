package com.jeseromero.yourvocabulary.activity.intent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.home.HomeActivity;
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.activity.share.MainShareActivity;
import com.jeseromero.yourvocabulary.activity.util.IntentHelper;
import com.jeseromero.yourvocabulary.activity.util.JSONHelper;
import com.jeseromero.yourvocabulary.activity.util.StorageHelper;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.adapter.WordAdapter;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiveFileActivity extends AppCompatActivity {

	public static final int FILE_REQUEST_CODE = 1;

	private ListView languageListView;
	private ListView wordsListView;

	private HashMap<Language, ArrayList<Word>> relations = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_file);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		languageListView = ((ListView) findViewById(R.id.languages));

		languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Language language = (Language) view.getTag();

				wordsListView.setAdapter(new WordAdapter(language.getWords(), ReceiveFileActivity.this));
			}
		});

		wordsListView = ((ListView) findViewById(R.id.words));

		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_REQUEST_CODE);
		} else {
			handleIntent();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

		switch (requestCode) {
			case FILE_REQUEST_CODE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					handleIntent();
				} else {
					Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();

					finish();
				}
			}
		}
	}

	private void handleIntent() {
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_VIEW.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent);
			}
		} else {
			startActivity(new Intent(this, HomeActivity.class));
		}
	}

	private void handleSendText(Intent intent) {
		Uri data = intent.getData();

		ArrayList<Language> externalLanguages = null;

		if (data != null) {

			try {
				externalLanguages = new JSONHelper(this).fromLanguageArray(StorageHelper.readFile(data.getPath()));
			} catch (IOException e) {
				e.printStackTrace();

				startActivity(IntentHelper.createShareTextIntent(e.getMessage()));
			}

			if (externalLanguages != null) {

				LanguageManager languageManager = new LanguageManager();

				Collections.sort(externalLanguages);

				for (int i = 0; i < externalLanguages.size(); i++) {
					Language externalLanguage = externalLanguages.get(i);

					Language myLanguage = languageManager.getLanguageByName(externalLanguage.getName());

					if (myLanguage != null) {
						ArrayList<Word> externalWords = externalLanguage.getWords();
						ArrayList<Word> myWords = myLanguage.getWords();

						for (Word myWord : myWords) {
							externalWords.remove(myWord);
						}

						if (!externalWords.isEmpty()) {
							relations.put(myLanguage, externalWords);
						}
					} else {
						relations.put(externalLanguage, externalLanguage.getWords());
					}
				}

			}

			if (externalLanguages != null) {
				if (!externalLanguages.isEmpty()) {
					languageListView.setAdapter(new LanguageAdapter(externalLanguages, this));

					wordsListView.setAdapter(new WordAdapter(externalLanguages.get(0).getWords(), this));
				} else {
					Toast.makeText(this, "No differences found", Toast.LENGTH_SHORT).show();

					finish();
				}
			} else {
				Toast.makeText(this, "Failed to import data", Toast.LENGTH_SHORT).show();

				finish();
			}

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

				saveAndFinish();

				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}

	private void saveAndFinish() {
		if (relations != null && !relations.isEmpty()) {

			for (Map.Entry<Language, ArrayList<Word>> entry : relations.entrySet()) {
				for (Word word : entry.getValue()) {
					word.save();

					Language language = entry.getKey();

					if (language.getId() == null) {
						language.save();
					}

					new LanguageWord(language, word).save();
				}
			}

			Toast.makeText(this, "Imported successfully", Toast.LENGTH_SHORT).show();

			finish();
		}
	}

}
