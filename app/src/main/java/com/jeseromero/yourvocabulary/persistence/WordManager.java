package com.jeseromero.yourvocabulary.persistence;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.jeseromero.yourvocabulary.model.Word;

import java.util.ArrayList;

/**
 * Version 1.0
 */

public class WordManager {

	ArrayList<Word> words;

	public WordManager() {
		words = new ArrayList<>();

		load();
	}

	public void addWord(Word word) {
		if (!words.contains(word)) {
			words.add(word);
		}

		commit();
	}

	public ArrayList<Word> selectAll() {
		return words;
	}

	private void load() {
		for (Model model : new Select().from(Word.class).orderBy("NAME ASC").execute()) {
			Word word = (Word) model;

			words.add(word);
		}
	}

	private void commit() {
		for (Word word : words) {
			word.save();
		}
	}

	public void deleteAll() {
		for (Word word : words) {
			word.delete();
		}

		words.clear();
	}
}
