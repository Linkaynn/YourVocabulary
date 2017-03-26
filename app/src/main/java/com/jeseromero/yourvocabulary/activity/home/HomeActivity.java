package com.jeseromero.yourvocabulary.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.activeandroid.util.ReflectionUtils;
import com.crashlytics.android.Crashlytics;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.language.LanguageActivity;
import com.jeseromero.yourvocabulary.activity.language.ManageLanguageActivity;
import com.jeseromero.yourvocabulary.activity.play.ChooseLanguageActivity;
import com.jeseromero.yourvocabulary.activity.share.MainShareActivity;
import com.jeseromero.yourvocabulary.activity.util.DialogBuilder;
import com.jeseromero.yourvocabulary.activity.vocabulary.VocabularyActivity;
import com.jeseromero.yourvocabulary.manager.ChangeLogManager;
import com.jeseromero.yourvocabulary.manager.Key;
import com.jeseromero.yourvocabulary.manager.LanguageManager;
import com.jeseromero.yourvocabulary.manager.SharedPreferencesManager;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;
import com.jeseromero.yourvocabulary.model.Word;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		SharedPreferencesManager.init(this);
		Fabric.with(this, new Crashlytics());

		if (SharedPreferencesManager.getBoolean(Key.RECENTLY_INSTALLED)) {
			SharedPreferencesManager.putBoolean(Key.RECENTLY_INSTALLED, false);
			SharedPreferencesManager.putInt(Key.LAST_CHANGELOG, ChangeLogManager.getLastVersion());
		} else {
			int lastChangelog = SharedPreferencesManager.getInt(Key.LAST_CHANGELOG);

			int versionCode = -1;
			String versionName = null;

			try {
				PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				versionCode = packageInfo.versionCode;
				versionName = packageInfo.versionName;
			} catch (PackageManager.NameNotFoundException e) {
				Crashlytics.logException(e);
				e.printStackTrace();
			}

//			DialogBuilder.buildInfoDialog(this, "Version " + versionName, ChangeLogManager.getChangeLog(versionCode));

			if ((lastChangelog == -1 || lastChangelog < versionCode) && versionCode != -1 && versionName != null) {
				String changeLog = ChangeLogManager.getChangeLog(versionCode);

				if (changeLog != null) {
					DialogBuilder.buildInfoDialog(this, "Version " + versionName, changeLog);
					SharedPreferencesManager.putInt(Key.LAST_CHANGELOG, versionCode);
				}

			}
		}

		View.OnClickListener playListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ArrayList<Language> languagesPlayable = new LanguageManager().getLanguagesPlayable();

				if (!languagesPlayable.isEmpty()) {
					startActivity(new Intent(HomeActivity.this, ChooseLanguageActivity.class));
				} else {
					Toast.makeText(HomeActivity.this, "You need a language with 4 or more words to play", Toast.LENGTH_LONG).show();
				}
			}
		};

		View.OnClickListener languageListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
			}
		};

		View.OnClickListener vocabularyListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						startActivity(new Intent(HomeActivity.this, ManageLanguageActivity.class));
					}
				};

				if (LanguageManager.getLanguageCount() != 0) {
					startActivity(new Intent(HomeActivity.this, VocabularyActivity.class));
				} else {
					DialogBuilder.buildWarningDialogWithoutIcon(HomeActivity.this, "You need a language", "No language detected. Do you want to create one?", yesListener, null);
				}
			}
		};

		View.OnClickListener shareListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(HomeActivity.this, MainShareActivity.class));
			}
		};

		findViewById(R.id.play_image).setOnClickListener(playListener);
		findViewById(R.id.play_text).setOnClickListener(playListener);

		findViewById(R.id.languages_image).setOnClickListener(languageListener);
		findViewById(R.id.languages_text).setOnClickListener(languageListener);

		findViewById(R.id.vocabulary_image).setOnClickListener(vocabularyListener);
		findViewById(R.id.vocabulary_text).setOnClickListener(vocabularyListener);

		findViewById(R.id.share_image).setOnClickListener(shareListener);
		findViewById(R.id.share_text).setOnClickListener(shareListener);

//		mock();

//		deleteDatabase();
	}

	private void mock() {
		deleteDatabase();

		for (Model model : new Select().all().from(LanguageWord.class).execute()) {
			model.delete();
		}

		for (Model model : new Select().all().from(Word.class).execute()) {
			model.delete();
		}

		for (Model model : new Select().all().from(Language.class).execute()) {
			model.delete();
		}

		Language japanese = new Language("Japanese");

		japanese.addWord(new Word("思い", "Pesado"));

		japanese.addWord(new Word("こんにちは, こんばんは", "Buenas tardes"));

		japanese.addWord(new Word("リストラン", "Restaurante"));

		japanese.addWord(new Word("おはようございます", "Buenos días"));

		japanese.addWord(new Word("おやすみなさい", "Buenas noches (Despedirse)"));

		Language english = new Language("English");

		english.addWord(new Word("Weight", "Peso"));

		english.addWord(new Word("Hello", "Hola"));

		english.addWord(new Word("Goodbye", "Adiós"));

		english.addWord(new Word("Long", "Largo"));

		english.addWord(new Word("Water", "Agua"));

		english.addWord(new Word("Paper clip", "Clip"));

		english.addWord(new Word("File folder", "Carpeta de ficheros"));

		english.addWord(new Word("Stapler", "Grapadora"));

		english.addWord(new Word("Scissors", "Tijeras"));

		english.addWord(new Word("Highlighter", "Subrayador"));

		english.addWord(new Word("Whiteboard", "Pizarra blanca"));

		english.addWord(new Word("Box", "Caja"));

		english.addWord(new Word("Bottle", "Botella"));

		japanese.saveAll();

		english.saveAll();

		for (Language language : new LanguageManager().getAllLanguages()) {
			System.out.println("LANGUAGE - " + language.getName());

			for (Word word : language.getWords()) {
				System.out.println("WORD - " + word.getValue());
			}
		}
	}

	private void deleteDatabase() {
		ActiveAndroid.dispose();

		String aaName = ReflectionUtils.getMetaData(getApplicationContext(), "AA_DB_NAME");

		if (aaName == null) {
			aaName = "yourvocabulary.db";
		}

		deleteDatabase(aaName);
		ActiveAndroid.initialize(this);
	}
}
