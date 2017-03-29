package com.jeseromero.yourvocabulary.manager;

/**
 * Version 1.0
 */
public enum Key {
	RECENTLY_INSTALLED("RECENTLY_INSTALLED", -1, true), LAST_CHANGELOG("LAST_CHANGELOG", -1, false);

	public final String name;
	public final Integer defaultIntValue;
	public final Boolean defaultBooleanValue;

	Key(String name, Integer defaultIntValue, Boolean defaultBooleanValue) {
		this.name = name;
		this.defaultIntValue = defaultIntValue;
		this.defaultBooleanValue = defaultBooleanValue;
	}
}
