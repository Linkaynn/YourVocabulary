package com.jeseromero.yourvocabulary.activity.language;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.adapter.WordAdapter;
import com.jeseromero.yourvocabulary.activity.word.manage.ManageWordActivity;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.manager.LanguageManager;

import java.util.ArrayList;
import java.util.Collection;

import static com.activeandroid.Cache.getContext;

public class LanguageDetailActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	private Language language;

	private ListView listView;

	private WordAdapter wordAdapter;
	private Toolbar toolbar;
	private TextView languageNameTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language_detail);

		final long languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);


		if (languageID == -1) {
			Toast.makeText(this, "An error ocurred.", Toast.LENGTH_SHORT).show();

			finish();
		}

		language = new LanguageManager().getLanguage(languageID);

		toolbar = (Toolbar) findViewById(R.id.toolbar);

		toolbar.setTitle("Language - " + language.getName());

		setSupportActionBar(toolbar);

		languageNameTextView = (TextView) findViewById(R.id.language);

		languageNameTextView.setText(language.getName());

		listView = (ListView) findViewById(R.id.translations);

		final Collection<Word> words = language.getWords();

		wordAdapter = new WordAdapter((ArrayList<Word>) words, getContext());

		listView.setAdapter(wordAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				final Word word = (Word) view.getTag();

				final CharSequence[] actions = {"Edit", "Remove"};

				DialogBuilder.buildChooserDialog(LanguageDetailActivity.this, "Actions", actions, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int selection) {
						CharSequence action = actions[selection];

						switch (action.toString()) {
							case "Edit":
								Intent intent = new Intent(LanguageDetailActivity.this, ManageWordActivity.class);

								intent.putExtra(ManageWordActivity.EDIT_ACTION, true);
								intent.putExtra(ManageWordActivity.LANGUAGE_ID, language.getId());
								intent.putExtra(ManageWordActivity.WORD_ID, word.getId());

								startActivity(intent);

								break;

							case "Remove":
								word.getRelation().delete();

								language.removeWord(word);

								word.delete();

								wordAdapter.remove(word);

								SuperActivityToast.OnButtonClickListener onButtonClickListener = new SuperActivityToast.OnButtonClickListener() {
									@Override
									public void onClick(View view, Parcelable token) {
										language.addWord(word);

										new LanguageWord(language, word).save();

										wordAdapter.add(word);
									}
								};

								SuperActivityToast.create(LanguageDetailActivity.this, new Style(), Style.TYPE_BUTTON)
										.setButtonText("UNDO")
										.setOnButtonClickListener("undo_delete_word", null, onButtonClickListener)
										.setText(word.getValue() + " deleted")
										.setDuration(3000).show();

								break;
						}
					}
				});
			}
		});

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addWordButton);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LanguageDetailActivity.this, ManageWordActivity.class);

				intent.putExtra(ManageWordActivity.LANGUAGE_ID, languageID);

				startActivity(intent);
			}
		});
	}


	@Override
	protected void onResume() {
		super.onResume();

		toolbar.setTitle("Language - " + language.getName());

		languageNameTextView.setText(language.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_delete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_edit:
				Intent intent = new Intent(LanguageDetailActivity.this, ManageLanguageActivity.class);

				intent.putExtra(ManageLanguageActivity.LANGUAGE_ID, language.getId());

				startActivity(intent);

				break;
			case R.id.action_delete:
				DialogBuilder.buildWarningDialog(LanguageDetailActivity.this, "Removing language",
						"Are you sure you want to remove " + language.getName() + " language? This action can't be undone.",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which) {
								for (Word word : language.getWords()) {
									word.getRelation().delete();
									word.delete();
								}

								language.delete();

								finish();
							}

						});
				break;
		}

		return false;
	}

}
