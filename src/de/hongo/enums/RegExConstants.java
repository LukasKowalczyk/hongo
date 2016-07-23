package de.hongo.enums;

import java.util.regex.Pattern;

public enum RegExConstants {
	REGEX_START_WITH_IGNORECASE("^(?i)"), REGEX_CONTAINS_IGNORECASE("(?i)");

	private String pattern;

	private RegExConstants(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public static String generateRegExCommand(String pattern,
			RegExConstants regExConstant) {
		return regExConstant.getPattern() + Pattern.quote(pattern);
	}
}
