package com.jeseromero.yourvocabulary.manager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Version 1.0
 */

public class ChangeLogManager {
	private static Collection<Version> versions = new ArrayList<>();

	private static Version version_6 = new Version(6, "Some errors found and deleted!\n\nNew interface in games and some improvements. Try to tap on the word in the write-word game!\n\nNow on new word or editing word, the app can suggest the meaning.\n\nAnd the last one but not less importante, now in the share section I personally add a contribution link, if you like the app, please, make it greater!\n\n\nThanks a lot!");
	private static Version version_7 = new Version(7, "Some errors found and deleted!\n\nSome changes on GUI!\n\n\nThanks a lot!");

	static {
		versions.add(version_6);
		versions.add(version_7);
	}

	public static String getChangeLog(int versionCode) {
		for (Version version : versions) {
			if (version.getVersion() == versionCode) {
				return version.getChangeLog();
			}
		}

		return null;
	}

	public static Integer getLastVersion() {
		Version lastVersion = null;

		for (Version version : versions) {
			lastVersion = lastVersion == null ? version : Math.max(version.getVersion(), lastVersion.getVersion()) == version.getVersion() ? version : lastVersion;
		}

		if (lastVersion != null) {
			return lastVersion.getVersion();
		}

		return null;
	}

	private static class Version {
		private final int version;
		private final String changeLog;

		public Version(int version, String changeLog) {
			this.version = version;
			this.changeLog = changeLog;
		}

		public int getVersion() {
			return version;
		}

		public String getChangeLog() {
			return changeLog;
		}
	}
}
