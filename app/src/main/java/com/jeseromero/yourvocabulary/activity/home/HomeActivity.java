package com.jeseromero.yourvocabulary.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.activeandroid.util.ReflectionUtils;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.intent.adapter.LanguageAdapter;
import com.jeseromero.yourvocabulary.activity.language.ManageLanguageActivity;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.activity.vocabulary.VocabularyActivity;
import com.jeseromero.yourvocabulary.activity.vocabulary.languages.LanguageFragment;
import com.jeseromero.yourvocabulary.manage.EditWordActivity;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.Collection;

import static com.jeseromero.yourvocabulary.R.id.word;

public class HomeActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private ListView languageListView;
	private LanguageAdapter languageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

		drawer.addDrawerListener(toggle);

		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		mock();

		languageListView = (ListView) findViewById(R.id.languages);

		languageAdapter = new LanguageAdapter(new LanguageManager().selectAll(), this);
		languageListView.setAdapter(languageAdapter);

		languageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				final Language language = (Language) view.getTag();

				final Collection<Word> words = language.getWords();

				final CharSequence[] actions = new CharSequence[]{"Edit", "Remove"};

				DialogBuilder.buildChooserDialog(HomeActivity.this, "Actions", actions, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int selection) {
						CharSequence action = actions[selection];

						switch (action.toString()) {
							case "Edit":
								Intent intent = new Intent(HomeActivity.this, ManageLanguageActivity.class);

								intent.putExtra(ManageLanguageActivity.LANGUAGE_ID, language.getId());

								startActivity(intent);

								break;

							case "Remove":

								for (Word word : words) {
									word.getRelation().delete();
									word.delete();
								}

								language.delete();

								languageAdapter.remove(language);

								SuperActivityToast.OnButtonClickListener onButtonClickListener = new SuperActivityToast.OnButtonClickListener() {
									@Override
									public void onClick(View view, Parcelable token) {

										// Debo recrear los lenguajes y las palabras debido
										// a que el ORM mantiene las referencias y internamente realiza un update sobre un elemento que no existe

										Language newLanguage = new Language(language.getName());

										for (Word word : words) {
											newLanguage.addWord(new Word(word.getValue(), word.getTranslation()));
										}

										newLanguage.saveAll();

										languageAdapter.add(newLanguage);
									}
								};

								SuperActivityToast.create(HomeActivity.this, new Style(), Style.TYPE_BUTTON)
										.setButtonText("UNDO")
										.setOnButtonClickListener("undo_delete_language", null, onButtonClickListener)
										.setText(language.getName() + " deleted")
										.setDuration(3000).show();

								break;
						}

					}
				});

				return false;
			}
		});

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addLanguageButton);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(HomeActivity.this, ManageLanguageActivity.class));
			}
		});

	}

	private void mock() {
		ActiveAndroid.dispose();

		String aaName = ReflectionUtils.getMetaData(getApplicationContext(), "AA_DB_NAME");

		if (aaName == null) {
			aaName = "yourvocabulary.db";
		}

		deleteDatabase(aaName);
		ActiveAndroid.initialize(this);

		for (Model model : new Select().all().from(LanguageWord.class).execute()) {
			model.delete();
		}

		for (Model model : new Select().all().from(Word.class).execute()) {
			model.delete();
		}

		for (Model model : new Select().all().from(Language.class).execute()) {
			model.delete();
		}

		LanguageManager languageManager = new LanguageManager();

		Language japanese = new Language("Japanese");

		japanese.addWord(new Word("思い", "Pesado"));

		languageManager.addLanguage(japanese);

		Language english = new Language("English");

		english.addWord(new Word("Weight", "Peso"));

		languageManager.addLanguage(english);

		for (Language language : new LanguageManager().selectAll()) {
			System.out.println("LANGUAGE - " + language.getName());

			for (Word word : language.getWords()) {
				System.out.println("WORD - " + word.getValue());
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		languageAdapter.setLanguages(new LanguageManager().selectAll());
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.your_vocabuary) {

			startActivity(new Intent(this, VocabularyActivity.class));
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
