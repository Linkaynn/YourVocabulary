package com.jeseromero.yourvocabulary.persistence;

import com.activeandroid.query.Select;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;

import java.util.List;

/**
 * Version 1.0
 */

public class StatisticsManager {

	private final List<Statistic> statistics;

	public StatisticsManager() {
		statistics = new Select().all().from(Statistic.class).execute();
	}

	public Statistic getStatistics(Language language) {

		for (Statistic statistic : statistics) {
			if (statistic.getLanguage().equals(language)) {
				return statistic;
			}
		}

		return null;
	}
}
