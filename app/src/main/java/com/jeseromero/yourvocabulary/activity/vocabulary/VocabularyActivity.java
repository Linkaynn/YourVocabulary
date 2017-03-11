package com.jeseromero.yourvocabulary.activity.vocabulary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.LanguageFragment;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.ArrayList;

public class VocabularyActivity extends AppCompatActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;

	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocabulary);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "New language", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});


	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private final ArrayList<Language> languages;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

			languages = new LanguageManager().selectAll();
		}

		@Override
		public Fragment getItem(int position) {
			return LanguageFragment.newInstance(languages.get(position));
		}

		@Override
		public int getCount() {
			return languages.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position < 0 || position >= languages.size()) {
				return null;
			}

			return languages.get(position).getName();
		}
	}
}
