package de.hongo.container;

import de.hongo.annotation.MongoCollectionInformation;
import de.hongo.annotation.MongoUpdateKey;

@MongoCollectionInformation(collectionName = "object", databaseName = "junittest")
public class TestObject {
	@MongoUpdateKey
	private String email;
	private String name;

	public TestObject(String email, String name) {
		super();
		this.email = email;
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "email:" + email + " name:" + name;
	}
}
