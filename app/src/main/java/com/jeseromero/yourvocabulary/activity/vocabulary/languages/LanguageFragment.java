package com.jeseromero.yourvocabulary.activity.vocabulary.languages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.adapter.WordAdapter;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A placeholder fragment containing a simple view.
 */
public class LanguageFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_LANGUAGE_ID = "section_number";

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

		ListView listView = (ListView) rootView.findViewById(R.id.translations);

		Collection<Word> words = new LanguageManager().selectLanguage(getArguments().getLong(ARG_LANGUAGE_ID)).getWords();

		listView.setAdapter(new WordAdapter((ArrayList<Word>) words, getContext()));

		return rootView;
	}
}
