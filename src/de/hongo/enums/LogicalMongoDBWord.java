package de.hongo.enums;

public enum LogicalMongoDBWord {
	AND("$and"), IN("$in"), NEQ("$ne"), EQ(""), GT("$gt"), LT("$lt"),REGEX("$regex");

	private String parameter;

	private LogicalMongoDBWord(String parameter) {
		this.parameter = parameter;
	}

	public String getParameterName() {
		return parameter;
	}

}
