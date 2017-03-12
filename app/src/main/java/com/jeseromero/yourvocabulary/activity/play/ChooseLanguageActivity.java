package com.jeseromero.yourvocabulary.activity.play;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;
import com.jeseromero.yourvocabulary.persistence.StatisticsManager;

import java.util.ArrayList;

public class ChooseLanguageActivity extends AppCompatActivity {

	private ListView languageListView;
	private LanguageAdapter languageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_language);

		setTitle("Play with your languages");

		languageListView = (ListView) findViewById(R.id.languages);

		ArrayList<Language> languages = new LanguageManager().getLanguagesPlayables();

		if (!languages.isEmpty()) {
			languageAdapter = new LanguageAdapter(languages, this);

			languageListView.setAdapter(languageAdapter);

			languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					final Language language = (Language) view.getTag();

					Statistic statistic = new StatisticsManager().getStatistic(language);

					if (statistic == null) {
						DialogBuilder.buildAlertDialog(ChooseLanguageActivity.this, "New game",
								"Do you want to play first time with " + language.getName() + " ?",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {
										Intent intent = new Intent(ChooseLanguageActivity.this, PlayActivity.class);

										intent.putExtra(PlayActivity.LANGUAGE_ID, language.getId());

										startActivity(intent);
									}
								});
					} else {
						Intent intent = new Intent(ChooseLanguageActivity.this, LanguageStatisticActivity.class);

						intent.putExtra(LanguageStatisticActivity.LANGUAGE_ID, language.getId());

						startActivity(intent);
					}

				}
			});
		}
	}
}
