package com.jeseromero.yourvocabulary.activity.util;

import android.content.Context;
import android.os.Environment;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jeseromero.yourvocabulary.model.Language;
import com.jeseromero.yourvocabulary.persistence.LanguageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Version 1.0
 */

public class JSONBuilder {

	private final Gson gson;
	private Context context;

	public JSONBuilder(Context context) {
		this.context = context;

		this.gson = new GsonBuilder()
				.setPrettyPrinting()
				.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
				.setExclusionStrategies(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						return f.getName().equalsIgnoreCase("mId");
					}

					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						return false;
					}
				})
				.create();

	}

	public String toJSON(Object o) {
		return gson.toJson(new LanguageManager().selectAll());
	}

	public File toFile(Object o, String fileName) throws IOException {
		File file = new StorageHelper(context).createFile(String.format("%s.yvo", fileName));

		FileOutputStream fOut = new FileOutputStream(file);

		OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

		myOutWriter.append(toJSON(o));

		myOutWriter.close();

		fOut.close();

		return file;
	}

	public ArrayList<Language> fromJSON(String json) {
		Language[] languagesArray = gson.fromJson(json, Language[].class);

		ArrayList<Language> languages = new ArrayList<>();

		for (Language language : languagesArray) {
			languages.add(language);
		}

		return languages;
	}
}
