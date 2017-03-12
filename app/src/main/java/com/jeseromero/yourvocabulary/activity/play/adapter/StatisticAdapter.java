package com.jeseromero.yourvocabulary.activity.play.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.model.Statistic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import az.plainpie.PieView;

/**
 * Version 1.0
 */

public class StatisticAdapter extends ArrayAdapter<Statistic> {
	private ArrayList<Statistic> statistics;

	public StatisticAdapter(@NonNull Context context, @NonNull ArrayList<Statistic> objects) {
		super(context, R.layout.statistic_item, objects);


		statistics = objects;
	}

	@Override
	public int getCount() {
		return statistics.size();
	}

	@Override
	public long getItemId(int i) {
		return statistics.get(i).getId();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
		Statistic statistic = statistics.get(position);

		if (view == null) {

			view = LayoutInflater.from(getContext()).inflate(R.layout.statistic_item, null);

		}

		view.setTag(statistic);

		TextView correctAnswers = (TextView) view.findViewById(R.id.correct_answers);
		TextView totalAnswers = (TextView) view.findViewById(R.id.total_answers);
		PieView pieView = (PieView) view.findViewById(R.id.pie);
		TextView date = (TextView) view.findViewById(R.id.date);

		correctAnswers.setText(String.valueOf(Float.valueOf(statistic.getCorrectAnswers()).intValue()));
		totalAnswers.setText(String.valueOf(Float.valueOf(statistic.getTries()).intValue()));

		pieView.setPercentage(statistic.getPercentage());

		date.setText(String.valueOf(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(statistic.getDate())));

		return view;
	}

	public void setStatistics(ArrayList<Statistic> statistics) {
		this.statistics = statistics;

		notifyDataSetChanged();
	}

	@Override
	public void remove(@Nullable Statistic object) {
		statistics.remove(object);
		notifyDataSetChanged();
	}

	@Override
	public void add(@Nullable Statistic object) {
		if (!statistics.contains(object)) {
			statistics.add(object);
		}
		notifyDataSetChanged();
	}
}
