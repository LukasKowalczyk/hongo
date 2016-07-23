package de.hongo.enums;

public enum MongoDBConstants {
	AND("$and"), IN("$in"), NEQ("$ne"), EQ(""), GT("$gt"), LT("$lt"), REGEX(
			"$regex"), SET("$set");

	private String parameter;

	private MongoDBConstants(String parameter) {
		this.parameter = parameter;
	}

	public String getParameterName() {
		return parameter;
	}

}
