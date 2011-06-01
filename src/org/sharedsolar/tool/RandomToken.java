package org.sharedsolar.tool;

public class RandomToken {
	public static String generate() {
		char[] token = new char[10];
		for (int i = 0; i < token.length; i++) {
			int t = (int)(Math.random()*26 + 97);
			token[i] = (char)t;
		}
		return String.valueOf(token);
	}
}
