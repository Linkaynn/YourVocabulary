package com.jeseromero.yourvocabulary.activity.language;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.activity.vocabulary.VocabularyActivity;
import com.jeseromero.yourvocabulary.manage.ManageWordActivity;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.Collection;

public class LanguageActivity extends AppCompatActivity {

	private ListView languageListView;

	private LanguageAdapter languageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);

		setTitle("Your languages");

		languageListView = (ListView) findViewById(R.id.languages);

		languageAdapter = new LanguageAdapter(new LanguageManager().selectAll(), this);

		languageListView.setAdapter(languageAdapter);

		languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Language language = (Language) view.getTag();

				Intent intent = new Intent(LanguageActivity.this, LanguageDetailActivity.class);

				intent.putExtra(LanguageDetailActivity.LANGUAGE_ID, language.getId());

				startActivity(intent);
			}
		});

		languageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				final Language language = (Language) view.getTag();

				final Collection<Word> words = language.getWords();

				final CharSequence[] actions = new CharSequence[]{"Edit", "Remove"};

				DialogBuilder.buildChooserDialog(LanguageActivity.this, "Actions", actions, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int selection) {
						CharSequence action = actions[selection];

						switch (action.toString()) {
							case "Edit":
								Intent intent = new Intent(LanguageActivity.this, ManageLanguageActivity.class);

								intent.putExtra(ManageLanguageActivity.LANGUAGE_ID, language.getId());

								startActivity(intent);

								break;

							case "Remove":

								for (Word word : words) {
									word.getRelation().delete();
									word.delete();
								}

								language.delete();

								languageAdapter.remove(language);

								SuperActivityToast.OnButtonClickListener onButtonClickListener = new SuperActivityToast.OnButtonClickListener() {
									@Override
									public void onClick(View view, Parcelable token) {

										// Debo recrear los lenguajes y las palabras debido
										// a que el ORM mantiene las referencias y internamente realiza un update sobre un elemento que no existe

										Language newLanguage = new Language(language.getName());

										for (Word word : words) {
											newLanguage.addWord(new Word(word.getValue(), word.getTranslation()));
										}

										newLanguage.saveAll();

										languageAdapter.add(newLanguage);
									}
								};

								SuperActivityToast.create(LanguageActivity.this, new Style(), Style.TYPE_BUTTON)
										.setButtonText("UNDO")
										.setOnButtonClickListener("undo_delete_language", null, onButtonClickListener)
										.setText(language.getName() + " deleted")
										.setDuration(3000).show();

								break;
						}

					}
				});

				return true;

			}
		});

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addLanguageButton);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LanguageActivity.this, ManageLanguageActivity.class));
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		languageAdapter.setLanguages(new LanguageManager().selectAll());
	}
}
