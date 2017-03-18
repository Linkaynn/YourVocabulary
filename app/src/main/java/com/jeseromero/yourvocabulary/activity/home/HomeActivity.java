package com.jeseromero.yourvocabulary.activity.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.activeandroid.util.ReflectionUtils;
import com.jeseromero.yourvocabulary.R;
import com.jeseromero.yourvocabulary.activity.language.LanguageActivity;
import com.jeseromero.yourvocabulary.activity.play.ChooseLanguageActivity;
import com.jeseromero.yourvocabulary.activity.share.MainShareActivity;
import com.jeseromero.yourvocabulary.activity.vocabulary.VocabularyActivity;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.model.LanguageWord;
import com.jeseromero.yourvocabulary.model.Word;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		View.OnClickListener playListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ArrayList<Language> languagesPlayables = new LanguageManager().getLanguagesPlayables();

				if (!languagesPlayables.isEmpty()) {
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
				startActivity(new Intent(HomeActivity.this, VocabularyActivity.class));
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

		mock();
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

		japanese.addWord(new Word("こんにちは, こんばんは", "Buenas tardes"));

		japanese.addWord(new Word("リストラン", "Restaurante"));

		japanese.addWord(new Word("おはようございます", "Buenos días"));

		japanese.addWord(new Word("おやすみなさい", "Buenas noches (Despedirse)"));

		languageManager.addLanguage(japanese);

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

		languageManager.addLanguage(english);

		for (Language language : new LanguageManager().selectAll()) {
			System.out.println("LANGUAGE - " + language.getName());

			for (Word word : language.getWords()) {
				System.out.println("WORD - " + word.getValue());
			}
		}
	}
}
