package com.jeseromero.yourvocabulary.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Version 1.0
 */

@Table(name = "LANGUAGE_WORD")
public class LanguageWord extends Model {

	@Column(name = "LANGUAGE", onDelete = Column.ForeignKeyAction.CASCADE)
	private Language language;

	@Column(name = "WORD", onDelete = Column.ForeignKeyAction.CASCADE)
	private Word word;

	public LanguageWord(Language language, Word word) {
		super();

		this.language = language;
		this.word = word;
	}

	public LanguageWord() {
		super();
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return language.getName() + " -- " + word.getValue();
	}
}
