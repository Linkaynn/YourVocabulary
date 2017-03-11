package com.jeseromero.yourvocabulary.persistence;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.jeseromero.yourvocabulary.model.Language;

import java.util.ArrayList;

/**
 * Version 1.0
 */

public class LanguageManager {

	ArrayList<Language> languages;

	public LanguageManager() {
		languages = new ArrayList<>();

		load();
	}

	public void addLanguage(Language language) {
		if (!languages.contains(language)) {
			languages.add(language);
		}

		commit();
	}

	public ArrayList<Language> selectAll() {
		return languages;
	}

	private void load() {
		for (Model model : new Select().from(Language.class).orderBy("NAME ASC").execute()) {
			Language language = (Language) model;

			languages.add(language);
		}
	}

	private void commit() {
		for (Language language : languages) {
			language.save();
		}
	}

	public void deleteAll() {
		for (Language language : languages) {
			language.delete();
		}

		languages.clear();
	}
}
