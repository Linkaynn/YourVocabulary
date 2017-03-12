package com.jeseromero.yourvocabulary.activity.play;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.play.adapter.StatisticAdapter;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;
import com.jeseromero.yourvocabulary.persistence.StatisticsManager;

import az.plainpie.PieView;

public class LanguageStatisticActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";
	private Language language;
	private ListView statisticsListView;
	private TextView wordsCount;
	private PieView pieView;
	private TextView correctAnswers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_language_statistic);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		long languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);

		if (languageID == -1) {
			Toast.makeText(this, "Language not found", Toast.LENGTH_SHORT).show();

			finish();
		}

		language = new LanguageManager().selectLanguage(languageID);

		Statistic statistics = new StatisticsManager().getStatistic(language);

		TextView languageTextField = (TextView) findViewById(R.id.language);

		languageTextField.setText(language.getName());

		setStatistics(statistics);

		statisticsListView = ((ListView) findViewById(R.id.statistics));

		statisticsListView.setAdapter(new StatisticAdapter(this, new StatisticsManager().getStatistics()));

		statisticsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Statistic statistic = (Statistic) view.getTag();

				setStatistics(statistic);
			}
		});
	}

	private void setStatistics(Statistic statistics) {
		correctAnswers = (TextView) findViewById(R.id.answers_count);

		correctAnswers.setText(String.valueOf(Float.valueOf(statistics.getCorrectAnswers()).intValue()));

		wordsCount = (TextView) findViewById(R.id.words_count);

		wordsCount.setText(String.valueOf(Float.valueOf(statistics.getTries()).intValue()));

		pieView = (PieView) findViewById(R.id.pie);

		pieView.setPercentage(statistics.getPercentage());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_play:

				DialogBuilder.buildAlertDialog(this, "Repeat test", "Do you want to start a new test?", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(LanguageStatisticActivity.this, PlayActivity.class);

						intent.putExtra(PlayActivity.LANGUAGE_ID, language.getId());

						startActivity(intent);
					}
				});

				break;
		}

		return true;
	}

}
