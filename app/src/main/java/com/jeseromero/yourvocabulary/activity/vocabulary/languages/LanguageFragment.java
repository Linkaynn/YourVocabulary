package com.jeseromero.yourvocabulary.activity.vocabulary.languages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.adapter.WordAdapter;
import com.jeseromero.yourvocabulary.manage.EditWordActivity;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.datatype.Duration;

public class LanguageFragment extends Fragment {

	private static final String ARG_LANGUAGE_ID = "section_number";
	private Language language;
	private ListView listView;
	private WordAdapter wordAdapter;

	public LanguageFragment() {
	}

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 * @param language
	 */
	public static LanguageFragment newInstance(Language language) {

		LanguageFragment fragment = new LanguageFragment();

		Bundle args = new Bundle();

		args.putLong(ARG_LANGUAGE_ID, language.getId());

		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_vocabulary, container, false);

		listView = (ListView) rootView.findViewById(R.id.translations);

		language = new LanguageManager().selectLanguage(getArguments().getLong(ARG_LANGUAGE_ID));

		final Collection<Word> words = language.getWords();

		wordAdapter = new WordAdapter((ArrayList<Word>) words, getContext());

		listView.setAdapter(wordAdapter);

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				final Word word = (Word) view.getTag();

				System.out.println("WORD " + word.getValue());

				final CharSequence[] actions = {"Edit", "Remove"};

				DialogBuilder.buildChooserDialog(LanguageFragment.this.getContext(), "Actions", actions, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int selection) {
						CharSequence action = actions[selection];

						switch (action.toString()) {
							case "Edit":
								Intent intent = new Intent(LanguageFragment.this.getContext(), EditWordActivity.class);

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

								SuperActivityToast.create(LanguageFragment.this.getContext(), new Style(), Style.TYPE_BUTTON)
										.setButtonText("UNDO")
										.setOnButtonClickListener("undo_delete_word", null, onButtonClickListener)
										.setText(word.getValue() + " deleted")
										.setDuration(3000).show();

								break;
						}
					}
				});

				return false;
			}
		});

		return rootView;
	}
}
