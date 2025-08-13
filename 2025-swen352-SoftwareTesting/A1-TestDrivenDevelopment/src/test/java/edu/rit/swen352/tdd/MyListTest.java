package edu.rit.swen352.tdd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test suite for the {@link MyList} component.
 */
class MyListTest {

	@ParameterizedTest(name = "Construct {0}->{1}")
	@MethodSource("constructorArgs")
	@DisplayName("constructing MyList")
	void constructor(String[] input, String expected) {
		MyList<String> list = new MyList<String>(input);
		assertNotNull(list);
		assertEquals(expected, list.toString());
	}

	static Stream<Arguments> constructorArgs() {
		return Stream.of(
				Arguments.of(new String[] {}, "[∅]"),
				Arguments.of(new String[] { "Apple" }, "[Apple]"),
				Arguments.of(new String[] { "Apple", "Orange", "Bread" }, "[Apple, Orange, Bread]"));
	}

	@ParameterizedTest(name = "Add {0} + {1} => {2}")
	@MethodSource("addArgs")
	@DisplayName("add element to MyList")
	void add(String[] initialElements, String newElement, String expected) {
		MyList<String> list = new MyList<String>(initialElements);
		list.add(newElement);
		assertEquals(expected, list.toString());
	}

	static Stream<Arguments> addArgs() {
		return Stream.of(
				Arguments.of(new String[] {}, "Apple", "[Apple]"),
				Arguments.of(new String[] { "Apple" }, "Orange", "[Apple, Orange]"),
				Arguments.of(new String[] { "Apple", "Orange" }, "Bread", "[Apple, Orange, Bread]"),
				Arguments.of(new String[] { "SameStringAndReference" },
						"SameStringAndReference",
						"[SameStringAndReference]"),
				Arguments.of(new String[] { new String("SameStringDifferentReference") },
						new String("SameStringDifferentReference"),
						"[SameStringDifferentReference, SameStringDifferentReference]"));
	}

	@ParameterizedTest(name = "Rem {0} - {1} => {2}")
	@MethodSource("remArgs")
	@DisplayName("remove element from MyList")
	void remove(String[] initialElements, String element, String expected) {
		MyList<String> list = new MyList<String>(initialElements);
		list.remove(element);
		assertEquals(expected, list.toString());
	}

	static Stream<Arguments> remArgs() {
		return Stream.of(
				Arguments.of(new String[] { "Apple" }, "Apple", "[∅]"),
				Arguments.of(new String[] { "Apple", "Orange" }, "Apple", "[Orange]"),
				Arguments.of(new String[] { "Apple", "Orange", "Bread" }, "Orange", "[Apple, Bread]"),
				Arguments.of(new String[] { "SameStringAndReference", "SameStringAndReference" },
						"SameStringAndReference",
						"[SameStringAndReference]"),
				Arguments.of(new String[] { new String("SameStringDifferentReference"),
						new String("SameStringDifferentReference") },
						new String("SameStringDifferentReference"),
						"[SameStringDifferentReference, SameStringDifferentReference]"),
				Arguments.of(new String[] { "Apple", "Orange" }, "Bread", "[Apple, Orange]"));
	}

	@ParameterizedTest(name = "Get {1} from {0} -> {2}")
	@MethodSource("getArgs")
	@DisplayName("get element from MyList")
	void get(String[] initialElements, int index, String expected) {
		MyList<String> list = new MyList<String>(initialElements);
		String item = list.get(index);
		assertEquals(expected, item);
	}

	static Stream<Arguments> getArgs() {
		return Stream.of(
				Arguments.of(new String[] { "Apple" }, 0, "Apple"),
				Arguments.of(new String[] { "Apple", "Orange", "Bread" }, 0, "Apple"),
				Arguments.of(new String[] { "Apple", "Orange", "Bread" }, 1, "Orange"),
				Arguments.of(new String[] { "Apple", "Orange", "Bread" }, 2, "Bread"));
	}

	@ParameterizedTest(name = "Get {1} from {0}")
	@MethodSource("getOOBArgs")
	@DisplayName("throw NoSuchElement if get index out of bounds")
	void getOutOfBounds(String[] initialElements, int index) {
		MyList<String> list = new MyList<String>(initialElements);
		assertThrows(NoSuchElementException.class, () -> list.get(index));
	}

	static Stream<Arguments> getOOBArgs() {
		return Stream.of(
				Arguments.of(new String[] { "Apple", "Orange", "Bread" }, -1),
				Arguments.of(new String[] { "Apple", "Orange", "Bread" }, 3));
	}

	@ParameterizedTest(name = "IsEmpty? {0}: {1}")
	@MethodSource("isEmptyArgs")
	@DisplayName("Query if the list is empty")
	void isEmpty(String[] initialElements, Boolean expect) {
		MyList<String> list = new MyList<String>(initialElements);
		Boolean isListEmpty = list.isEmpty();
		assertEquals(expect, isListEmpty);
	}

	static Stream<Arguments> isEmptyArgs() {
		return Stream.of(
				Arguments.of(new String[] { "Apple" }, false),
				Arguments.of(new String[] {}, true),
				Arguments.of(new String[] { null, null, null }, true));
	}

	@ParameterizedTest(name = "Size of {0}: {1}")
	@MethodSource("sizeArgs")
	@DisplayName("Query how many elements are in the list")
	void size(String[] initialElements, int expect) {
		MyList<String> list = new MyList<String>(initialElements);
		int listSize = list.size();
		assertEquals(expect, listSize);
	}

	static Stream<Arguments> sizeArgs() {
		return Stream.of(
				Arguments.of(new String[] { "Apple", "Orange" }, 2),
				Arguments.of(new String[] { "Apple", "Orange", null }, 2),
				Arguments.of(new String[] {}, 0),
				Arguments.of(new String[] { null, null, null }, 0));
	}

	@ParameterizedTest(name = "{0} * 2 = {1}")
	@MethodSource("forEachArgs")
	@DisplayName("Execute consumer to multiply each element by 2")
	void forEach(Integer[] initialElements, String expectedDouble) {
		MyList<Integer> list = new MyList<Integer>(initialElements);
		MyList<Integer> listDoubled = new MyList<Integer>();

		list.forEach(item -> {
			listDoubled.add(item * 2);
		});

		assertEquals(expectedDouble, listDoubled.toString());
	}

	static Stream<Arguments> forEachArgs() {
		return Stream.of(
				Arguments.of(new Integer[] {}, "[∅]"),
				Arguments.of(new Integer[] { 1 }, "[2]"),
				Arguments.of(new Integer[] { 1, 2, 3 }, "[2, 4, 6]"),
				Arguments.of(new Integer[] { 1, 2, 3, null }, "[2, 4, 6]"));
	}
}
