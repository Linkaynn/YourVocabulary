package com.jeseromero.yourvocabulary.manager;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Version 1.0
 */

public class LanguageManager {

	private ArrayList<Language> languages;

	public LanguageManager() {
		languages = new ArrayList<>();

		load();
	}

	public ArrayList<Language> getAllLanguages() {
		return languages;
	}

	private void load() {
		for (Model model : new Select().all().from(Language.class).orderBy("NAME ASC").execute()) {
			Language language = (Language) model;

			languages.add(language);
		}

		for (Language language : languages) {
			List<LanguageWord> languageWords = new Select().all().from(LanguageWord.class).where("LANGUAGE = '" + language.getId() + "'").execute();

			for (LanguageWord languageWord : languageWords) {

				language.addWord(languageWord.getWord());

			}
		}
	}

	public Language getLanguage(long id) {
		for (Language language : languages) {
			if (language.getId() == id) {
				return language;
			}
		}

		return null;
	}

	public ArrayList<Language> getLanguagesPlayable() {
		ArrayList<Language> languages = new ArrayList<>();

		for (Language language : this.languages) {
			if (language.getWords().size() >= 4) {
				languages.add(language);
			}
		}

		return languages;
	}

	public Language getLanguageByName(String name) {
		for (Language language : languages) {
			if (language.getName().equalsIgnoreCase(name)) {
				return language;
			}
		}

		return null;
	}

	public static int getLanguageCount() {
		return new Select().all().from(Language.class).count();
	}
}
