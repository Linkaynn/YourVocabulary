package com.jeseromero.yourvocabulary.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Version 1.0
 */

@Table(name = "WORD")
public class Word extends Model {

	@Column(name = "NAME", notNull = true)
	private String value;

	public Word(String value) {
		super();

		this.value = value;
	}

	public Word() {
		super();
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Word word = (Word) o;

		return getId().equals(word.getId()) && value.equals(word.value);

	}
}
