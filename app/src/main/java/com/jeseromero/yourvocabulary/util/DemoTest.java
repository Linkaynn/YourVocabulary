package com.jeseromero.yourvocabulary.util;


/*
 * author : qahumor
 * date : 20/Jan/2017
 * 
 */

public class DemoTest {
	
	public static void main(String[] args) throws Exception {

		System.out.println("Passing String to Language Detect API ");

		Translation name = new Translator().translate("Hello");

		System.out.println("Detected language of String : " + name.getFrom());
		System.out.println(name.getTranslationText());

	}
	
	
}
