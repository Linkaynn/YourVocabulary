package com.jeseromero.yourvocabulary.util;

import com.crashlytics.android.Crashlytics;
import com.detectlanguage.DetectLanguage;
import com.detectlanguage.errors.APIError;
import com.google.gson.Gson;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;


/*
 * author : qahumor
 * date : 20/Jan/2017
 * 
 */

public class Translator {

	private final String translateAPIURL = "http://www.transltr.org/api/translate?";

	private final String methodType = "GET";

	private final String requestValue = "USER_AGENT";

	// HTTP GET request
	public Translation translate(String text) throws Exception {

		/*Please, dont steal it :)*/
		DetectLanguage.apiKey = "b2ec8a2446201a82b355d39680820efb";

		String detectedLanguage = null;

		detectedLanguage = DetectLanguage.simpleDetect(text);

		String urlWithText = translateAPIURL + "text=" + URLEncoder.encode(text, "UTF-8") + "&to=" + Locale.getDefault().getLanguage();

		if (detectedLanguage != null) {
			urlWithText += "&from=" + detectedLanguage;
		}

		URL obj = new URL(urlWithText);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod(methodType);

		con.setRequestProperty("User-Agent", requestValue);

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String inputLine;

		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		if (responseCode != 200) {
			return null;
		} else {
			return new Gson().fromJson(response.toString(),Translation.class);
		}
	}

}