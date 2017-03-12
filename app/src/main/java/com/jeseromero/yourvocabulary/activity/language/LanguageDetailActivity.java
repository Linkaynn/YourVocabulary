package com.jeseromero.yourvocabulary.activity.language;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.VocabularyFragment;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.adapter.WordAdapter;
import com.jeseromero.yourvocabulary.manage.EditWordActivity;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.ArrayList;
import java.util.Collection;

import static com.activeandroid.Cache.getContext;

public class LanguageDetailActivity extends AppCompatActivity {

	public static final String LANGUAGE_ID = "LANGUAGE_ID";

	private Language language;

	private ListView listView;

	private WordAdapter wordAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language_detail);

		long languageID = getIntent().getLongExtra(LANGUAGE_ID, -1);


		if (languageID == -1) {
			Toast.makeText(this, "No language found", Toast.LENGTH_SHORT).show();

			finish();
		}

		language = new LanguageManager().selectLanguage(languageID);

		setTitle("Language - " + language.getName());

		((TextView) findViewById(R.id.language)).setText(language.getName());

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
								Intent intent = new Intent(LanguageDetailActivity.this, EditWordActivity.class);

								intent.putExtra(EditWordActivity.LANGUAGE_ID, language.getId());
								intent.putExtra(EditWordActivity.WORD_ID, word.getId());

								startActivity(intent);

								break;

							case "Remove":
								language.removeWord(word);

								language.saveAll();

								wordAdapter.remove(word);

								SuperActivityToast.OnButtonClickListener onButtonClickListener = new SuperActivityToast.OnButtonClickListener() {
									@Override
									public void onClick(View view, Parcelable token) {
										language.addWord(word);

										language.saveAll();

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
	}
}
