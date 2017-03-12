package com.jeseromero.yourvocabulary.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Version 1.0
 */

@Table(name = "WORD")
public class Word extends Model {

	@Column(name = "NAME", notNull = true)
	private String value;

	@Column(name = "TRANSLATION", notNull = true)
	private String translation;

	public Word(String value, String translation) {
		super();

		this.value = value;
		this.translation = translation;
	}

	public Word() {
		super();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Word word = (Word) o;

		return getId().equals(word.getId()) && value.equals(word.value) && translation.equals(word.translation);
	}

	public LanguageWord getRelation() {
		List<Model> models = new Select().all().from(LanguageWord.class).where("WORD = '" + getId() + "'").limit(1).execute();

		if (models.isEmpty()) {
			return null;
		}

		return ((LanguageWord) models.get(0));
	}

	@Override
	public String toString() {
		return value;
	}
}
