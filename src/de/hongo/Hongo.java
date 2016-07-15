package de.hongo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.hongo.annotation.MongoCollectionInformation;
import de.hongo.annotation.MongoDatabaseInformation;
import de.hongo.annotation.MongoUpdateKey;
import de.hongo.enums.LogicalMongoDBWord;
import de.hongo.exception.LogicalMongoDBWordException;
import de.hongo.exception.MongelpCollectionConnectionException;
import de.hongo.exception.MongelpDatabaseConnectionException;

public class Hongo {

	private static Map<String, MongoDatabase> collectionMap = new HashMap<String, MongoDatabase>();

	public static void generateDatabaseConnection(Class<?> c)
			throws MongelpDatabaseConnectionException {
		if (c.isAnnotationPresent(MongoDatabaseInformation.class)) {
			MongoDatabaseInformation mdbInfo = c
					.getAnnotation(MongoDatabaseInformation.class);
			String uri = MongoDatabaseInformation.URISTART + mdbInfo.username()
					+ ":" + mdbInfo.password() + "@" + mdbInfo.host() + ":"
					+ mdbInfo.port() + "/" + mdbInfo.databaseName();
			MongoDatabase mongoDB = getMongoClient(uri).getDatabase(
					mdbInfo.databaseName());
			if (mongoDB == null) {
				throw new MongelpDatabaseConnectionException(
						"No such database found!");
			}
			collectionMap.put(mdbInfo.databaseName(), mongoDB);
		} else {
			throw new MongelpDatabaseConnectionException(
					"No @MongoDatabaseInformation found!");
		}
	}

	private static MongoClient getMongoClient(String uri) {
		return new MongoClient(new MongoClientURI(uri));
	}

	private static MongoCollection<Document> getCollectionFromDatabase(
			Class<?> c) throws MongelpDatabaseConnectionException {
		if (c.isAnnotationPresent(MongoCollectionInformation.class)) {
			MongoCollectionInformation mCInfo = c
					.getAnnotation(MongoCollectionInformation.class);
			MongoDatabase mongoDB = collectionMap.get(mCInfo.databaseName());
			if (mongoDB == null) {
				throw new MongelpDatabaseConnectionException(
						"No such database found!");
			}
			return mongoDB.getCollection(mCInfo.collectionName());
		}
		return null;
	}

	public static void insertIntoCollection(Object o)
			throws MongelpDatabaseConnectionException,
			MongelpCollectionConnectionException {
		MongoCollection<Document> coll = getCollectionFromDatabase(o.getClass());
		if (coll == null) {
			throw new MongelpCollectionConnectionException(
					"No such Collection found!");
		}
		coll.insertOne(Document.parse(new Gson().toJson(o)));
	}

	public static void updateInCollection(Object o)
			throws MongelpDatabaseConnectionException,
			MongelpCollectionConnectionException {
		BasicDBObject filter = new BasicDBObject();
		try {
			String key = getUpdateKey(o.getClass());
			Object value = getMethodeValue(o, key);
			filter = new BasicDBObject(key, value);
		} catch (Exception e) {
			throw new MongelpCollectionConnectionException(e);
		}
		MongoCollection<Document> coll = getCollectionFromDatabase(o.getClass());
		if (coll == null) {
			throw new MongelpCollectionConnectionException(
					"No such Collection found!");
		}
		 coll.updateOne(filter, (Document.parse(new Gson().toJson(o))));
	}

	private static Object getMethodeValue(Object o, String key)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		String firstLetter = new String(key.charAt(0) + "").toUpperCase();
		Method m = o.getClass().getMethod(
				"get" + firstLetter + key.substring(1));
		return m.invoke(o);
	}

	private static String getUpdateKey(Class<?> c)
			throws IllegalArgumentException, IllegalAccessException {

		for (Field f : c.getDeclaredFields()) {
			if (f.isAnnotationPresent(MongoUpdateKey.class)) {
				return f.getName();
			}
		}
		return null;
	}

	public static void insertIntoCollection(Class<?> c, List<Object> objectList)
			throws MongelpDatabaseConnectionException,
			MongelpCollectionConnectionException {
		MongoCollection<Document> coll = getCollectionFromDatabase(c);
		if (coll == null) {
			throw new MongelpCollectionConnectionException(
					"No such Collection found!");
		}
		List<Document> insertList = Collections.emptyList();
		for (Object object : objectList) {
			insertList.add(Document.parse(new Gson().toJson(object)));
		}
		coll.insertMany(insertList);
	}

	public static <T> List<T> findInCollection(Class<T> c, BasicDBObject query)
			throws MongelpDatabaseConnectionException,
			MongelpCollectionConnectionException {
		MongoCollection<Document> coll = getCollectionFromDatabase(c);
		if (coll == null) {
			throw new MongelpCollectionConnectionException(
					"No such Collection found!");
		}
		ArrayList<T> ausg = new ArrayList<T>();
		FindIterable<Document> finds = coll.find(query);
		for (Document document : finds) {
			ausg.add(new Gson().fromJson(document.toJson(), c));
		}
		return ausg;
	}

	public static <T> List<T> getCollection(Class<T> c)
			throws MongelpDatabaseConnectionException,
			MongelpCollectionConnectionException {
		MongoCollection<Document> coll = getCollectionFromDatabase(c);
		if (coll == null) {
			throw new MongelpCollectionConnectionException(
					"No such Collection found!");
		}
		ArrayList<T> ausg = new ArrayList<T>();
		FindIterable<Document> finds = coll.find();
		for (Document document : finds) {
			ausg.add(new Gson().fromJson(document.toJson(), c));
		}
		return ausg;
	}

	public static BasicDBObject quereyBuilder(String name, String value) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put(name, value);
		return basicDBObject;
	}

	public static BasicDBObject quereyBuilder(String parameterName,
			List<Object> values, LogicalMongoDBWord word)
			throws LogicalMongoDBWordException {
		switch (word) {
		case IN:
			return generateINQuery(parameterName, values);

		default:
			throw new LogicalMongoDBWordException(
					"Logical word is not maintain in this method! Please use another one!");
		}
	}

	private static BasicDBObject generateINQuery(String parameterName,
			List<Object> values) {
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put(parameterName, new BasicDBObject(
				LogicalMongoDBWord.IN.getParameterName(), values));
		return basicDBObject;
	}

	public static BasicDBObject quereyBuilder(Map<String, Object> keyValue,
			LogicalMongoDBWord word) throws LogicalMongoDBWordException {
		switch (word) {
		case AND:
			return generateANDQuery(keyValue);
		case EQ:
			return generateEQQuery(keyValue);
		case NEQ:
			return generateMapQuery(keyValue, word);
		case LT:
			return generateMapQuery(keyValue, word);
		case GT:
			return generateMapQuery(keyValue, word);
		case REGEX:
			return generateMapQuery(keyValue, word);
		default:
			throw new LogicalMongoDBWordException(
					"Logical word is not maintain in this method! Please use another one!");
		}
	}

	private static BasicDBObject generateMapQuery(Map<String, Object> keyValue,
			LogicalMongoDBWord word) {
		BasicDBObject basicDBObject = new BasicDBObject();
		for (Entry<String, Object> entry : keyValue.entrySet()) {
			basicDBObject
					.put(entry.getKey(),
							new BasicDBObject(word.getParameterName(), entry
									.getValue()));
		}
		return basicDBObject;
	}

	private static BasicDBObject generateEQQuery(Map<String, Object> keyValue) {
		BasicDBObject basicDBObject = new BasicDBObject();
		for (Entry<String, Object> entry : keyValue.entrySet()) {
			basicDBObject.put(entry.getKey(), entry.getValue());
		}
		return basicDBObject;
	}

	private static BasicDBObject generateANDQuery(Map<String, Object> keyValue) {
		BasicDBObject basicDBObject = new BasicDBObject();
		List<BasicDBObject> list = Collections.emptyList();
		for (Entry<String, Object> entry : keyValue.entrySet()) {
			list.add(new BasicDBObject(entry.getKey(), entry.getValue()));
		}
		basicDBObject.put(LogicalMongoDBWord.AND.getParameterName(), list);
		return basicDBObject;
	}
}
