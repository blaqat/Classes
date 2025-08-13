package edu.rit.swen352.tdd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class MyCircularQueueTest {
	@Test
	@DisplayName("Default MyQueue Construct")
	void defaultConstructor() {
		MyCircularQueue<Object> queue = new MyCircularQueue<>();
		assertAll(
				"Queue should not be null and have default capacity of 16",
				() -> assertNotNull(queue),
				() -> assertEquals(16, queue.getCapacity()));
	}

	@ParameterizedTest(name = "Construct with {0} capacity")
	@CsvSource({ "0", "5", "16", "32" })
	@DisplayName("MyQueue Construct with Capacity")
	void capacityConstructor(int capacity) {
		MyCircularQueue<Object> queue = new MyCircularQueue<>(capacity);
		assertAll(
				"Queue should not be null and have the specified capacity",
				() -> assertNotNull(queue),
				() -> assertEquals(capacity, queue.getCapacity()));
	}

	@ParameterizedTest(name = "Add {1} to queue of size {0} -> {2}")
	@MethodSource("addArgs")
	void add(int capacity, String[] items, String expected) {
		MyCircularQueue<String> queue = new MyCircularQueue<>(capacity);

		if (expected.equals("IllegalStateException")) {
			assertThrows(IllegalStateException.class, () -> {
				for (String item : items) {
					queue.add(item);
				}
			});
		} else {
			for (String item : items) {
				queue.add(item);
			}
			assertEquals(expected, queue.toString());
		}
	}

	static Stream<Arguments> addArgs() {
		return Stream.of(
				Arguments.of(1, new String[] { "Apple" }, "[Apple]"),
				Arguments.of(3, new String[] { "Apple", "Banana", "Cherry" }, "[Apple, Banana, Cherry]"),
				Arguments.of(2, new String[] { "Apple", "Banana", "Cherry" }, "IllegalStateException"));
	}

	@ParameterizedTest(name = "Is queue {0} empty: {1}")
	@MethodSource("isEmptyArgs")
	void isEmpty(String[] items, Boolean expected) {
		MyCircularQueue<String> queue = new MyCircularQueue<>();
		for (String item : items) {
			queue.add(item);
		}

		assertEquals(expected, queue.isEmpty());
	}

	static Stream<Arguments> isEmptyArgs() {
		return Stream.of(
				Arguments.of(new String[] {}, true),
				Arguments.of(new String[] { "Apple" }, false),
				Arguments.of(new String[] { "Apple", "Banana" }, false));
	}
}
