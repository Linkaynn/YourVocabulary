package com.jeseromero.yourvocabulary.activity.vocabulary.languages.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Word;

import java.util.ArrayList;

/**
 * Version 1.0
 */

public class WordAdapter extends ArrayAdapter<Word> {
	private final ArrayList<Word> words;

	public WordAdapter(@NonNull ArrayList<Word> words, @NonNull Context context) {
		super(context, R.layout.word_item, words);

		this.words = words;
	}

	@Override
	public int getCount() {
		return words.size();
	}

	@Override
	public long getItemId(int i) {
		return words.get(i).hashCode();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
		Word word = words.get(position);

		if (view == null) {

			view = LayoutInflater.from(getContext()).inflate(R.layout.word_item, null);

		}

		view.setTag(word);

		TextView name = (TextView) view.findViewById(R.id.value);
		TextView translation = (TextView) view.findViewById(R.id.translation);

		name.setText(word.getValue());

		translation.setText(word.getTranslation());

		return view;
	}

	@Override
	public void remove(@Nullable Word object) {
		words.remove(object);
		notifyDataSetChanged();
	}

	@Override
	public void add(@Nullable Word object) {
		if (!words.contains(object)) {
			words.add(object);
		}
		notifyDataSetChanged();
	}
}
