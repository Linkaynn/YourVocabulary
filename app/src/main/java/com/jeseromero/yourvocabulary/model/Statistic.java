package com.jeseromero.yourvocabulary.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Version 1.0
 */

@Table(name = "STATISTIC")
public class Statistic extends Model {

	@Column(name = "LANGUAGE", onDelete = Column.ForeignKeyAction.CASCADE)
	private Language language;

	@Column(name = "CORRECT_ANSWERS")
	private float correctAnswers;

	@Column(name = "TRIES")
	private float tries;

	public Statistic() {
		super();

		correctAnswers = 0;
		tries = 0;
	}

	public Language getLanguage() {
		return language;
	}

	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public float getCorrectAnswers() {
		return correctAnswers;
	}

	public void newCorrectAnswer() {
		correctAnswers++;
		tries++;
	}

	public float getTries() {
		return tries;
	}

	public void addTries() {
		tries++;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}

	public float getPercentage() {
		float percentage = (correctAnswers / tries) * 100;

		return Math.max(1, percentage);
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
