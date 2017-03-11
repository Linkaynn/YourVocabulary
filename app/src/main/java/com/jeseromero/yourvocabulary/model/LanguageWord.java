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
	private Language recipe;

	@Column(name = "WORD", onDelete = Column.ForeignKeyAction.CASCADE)
	private Word word;

	public LanguageWord(Language recipe, Word word) {
		super();

		this.recipe = recipe;
		this.word = word;
	}

	public LanguageWord() {
		super();
	}

	public Language getRecipe() {
		return recipe;
	}

	public Word getWord() {
		return word;
	}
}
