package com.jeseromero.yourvocabulary.model;

import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Version 1.0
 */

@Table(name = "LANGUAGE")
public class Language extends Model implements Comparable<Language>{

	@Column(name = "NAME", notNull = true)
	private String name;

	private ArrayList<Word> words;

	public Language(String name) {
		super();

		this.name = name;

		words = new ArrayList<>();
	}

	public Language() {
		super();

		words = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Language language = (Language) o;

		return (getId() != null && getId().equals(language.getId())) || (name != null && name.equalsIgnoreCase(language.getName()));
	}

	public ArrayList<Word> getWords() {
		return words;
	}

	public void addWord(Word word) {
		if (!words.contains(word)) {
			words.add(word);
		}
	}

	public Long saveAll() {
		Long valueSaved = super.save();

		for (Model model : new Select().all().from(LanguageWord.class).where("LANGUAGE = '" + getId() + "'").execute()) {
			model.delete();
		}

		for (Word word : words) {
			word.save();

			LanguageWord languageWord = new LanguageWord(this, word);

			languageWord.save();
		}

		return valueSaved;
	}

	public Word getWord(long wordId) {
		for (Word word : words) {
			if (word.getId() == wordId) {
				return word;
			}
		}

		return null;
	}

	public void clearWords() {
		words.clear();
	}

	public void removeWord(Word wordToSearch) {
		words.remove(wordToSearch);
	}

	@Override
	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(@NonNull Language language) {
		return name.compareTo(language.name);
	}
}
