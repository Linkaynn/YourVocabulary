package com.jeseromero.yourvocabulary.manager;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Version 1.0
 */

public class StatisticsManager {

	private final ArrayList<Statistic> statistics;

	public StatisticsManager() {

		statistics = new ArrayList<>();

		for (Model model : new Select().all().from(Statistic.class).orderBy("DATE DESC").execute()) {
			statistics.add((Statistic) model);
		}
	}

	public ArrayList<Statistic> getStatistics() {
		return statistics;
	}

	public Statistic getStatistic(Language language) {
		List<Model> models = new Select().all().from(Statistic.class).where("LANGUAGE = '" + language.getId() + "'").orderBy("DATE DESC").limit(1).execute();

		if (models.isEmpty()) {
			return null;
		}

		return ((Statistic) models.get(0));
	}

	public ArrayList<Statistic> getStatistics(Language language) {
		ArrayList<Statistic> statistics = new ArrayList<>();

		for (Statistic statistic : this.statistics) {
			if (statistic.getLanguage().equals(language)) {
				statistics.add(statistic);
			}
		}

		return statistics;
	}
}
