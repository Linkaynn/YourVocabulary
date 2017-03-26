package com.jeseromero.yourvocabulary.util;

import java.io.Serializable;

/*
 * author : qahumor
 * date : 20/Jan/2017
 * 
 */

public class Translation implements Serializable {

	private String from;
	private String to;
	private String text;
	private String translationText;

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getText() {
		return text;
	}

	public String getTranslationText() {
		return translationText;
	}
}
