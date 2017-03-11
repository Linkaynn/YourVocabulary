package com.jeseromero.yourvocabulary.activity.intent.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Language;

import java.util.ArrayList;

/**
 * Version 1.0
 */

public class LanguageAdapter extends ArrayAdapter<Language> {
	private final ArrayList<Language> languages;

	public LanguageAdapter(@NonNull ArrayList<Language> languages, @NonNull Context context) {
		super(context, R.layout.language_item, languages);

		this.languages = languages;
	}

	@Override
	public int getCount() {
		return languages.size();
	}

	@Override
	public long getItemId(int i) {
		return languages.get(i).getId();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
		Language language = languages.get(position);

		if (view == null) {

			view = LayoutInflater.from(getContext()).inflate(R.layout.language_item, null);

		}

		view.setTag(language);

		TextView name = (TextView) view.findViewById(R.id.language);

		name.setText(language.getName());

		return view;
	}
}
