package com.jeseromero.yourvocabulary.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Version 1.0
 */

@Table(name = "LANGUAGE")
public class Language extends Model {

	@Column(name = "NAME", notNull = true)
	private String name;

	private Collection<Word> words;

	public Language(String name) {
		super();

		this.name = name;

		words = new ArrayList<>();
	}

	public Language() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Language language = (Language) o;

		return getId().equals(language.getId()) && name.equalsIgnoreCase(language.name);

	}
}
