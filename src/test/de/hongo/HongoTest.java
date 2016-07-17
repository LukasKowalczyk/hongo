/**
 * 
 */
package de.hongo;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hongo.container.TestDatenbank;
import de.hongo.container.TestObject;

/**
 * @author Lukas
 *
 */
public class HongoTest {

	private static boolean debug = false;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Hongo.generateDatabaseConnection(TestDatenbank.class);
		// debug = true;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Hongo.dropCollections(TestObject.class);
		Hongo.insertIntoCollection(new TestObject("test@test.com", "testUser"));
	}

	@Test
	public void updatePositivTest() {
		boolean test = true;
		try {
			printOutCollection(TestObject.class);
			Hongo.updateInCollection(new TestObject("test@test.com",
					"testUser1"));
			printOutCollection(TestObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			test = false;
		}
		assertTrue(test);
	}

	@Test
	public void updateNegativTest() {
		boolean test = false;
		try {
			printOutCollection(TestObject.class);
			Hongo.updateInCollection(null);
			printOutCollection(TestObject.class);
		} catch (Exception e) {
			test = true;
		}
		assertTrue(test);
	}

	@Test
	public void insertPositivTest() {
		boolean test = true;
		try {
			printOutCollection(TestObject.class);
			Hongo.insertIntoCollection(new TestObject("bla@test.com", "blaUser"));
			printOutCollection(TestObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			test = false;
		}
		assertTrue(test);
	}

	@Test
	public void insertNegativTest() {
		boolean test = false;
		try {
			printOutCollection(TestObject.class);
			Hongo.insertIntoCollection(null);
			printOutCollection(TestObject.class);
		} catch (Exception e) {
			test = true;
		}
		assertTrue(test);
	}

	@Test
	public void findPositivTest() {
		boolean test = true;
		try {
			printOutCollection(TestObject.class);
			Hongo.findInCollection(TestObject.class,
					Hongo.quereyBuilder("name", "testUser"));
			printOutCollection(TestObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			test = false;
		}
		assertTrue(test);
	}

	@Test
	public void findNegativTest() {
		boolean test = false;
		try {
			printOutCollection(TestObject.class);
			Hongo.findInCollection(TestObject.class, null);
			printOutCollection(TestObject.class);
		} catch (Exception e) {
			test = true;
		}
		assertTrue(test);
	}

	private void printOutCollection(Class<?> c) {
		if (debug) {
			try {
				List<?> erg = Hongo.getCollection(c);
				for (Object object : erg) {
					System.out.println(object);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
