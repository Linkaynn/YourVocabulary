package com.jeseromero.yourvocabulary.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Version 1.0
 */

@Table(name = "STATISTIC")
public class Statistic extends Model {

	@Column(name = "LANGUAGE", onDelete = Column.ForeignKeyAction.CASCADE)
	private Language language;

	@Column(name = "DATE")
	private Date date;

	@Column(name = "CORRECT_ANSWERS")
	private float correctAnswers;

	@Column(name = "TRIES")
	private float tries;

	public Statistic() {
		super();

		date = new Date();
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

	public void addTry() {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPercentageOfTotal() {
		if (correctAnswers == 0) {
			return 1;
		}

		return (correctAnswers / language.getWords().size()) * 100;
	}
}
