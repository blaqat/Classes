package edu.rit.swen352.tdd.samples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NumbersTest {

  @ParameterizedTest(name = "Test: {0}->{1}")
  @CsvSource({
    "1,1st", "2,2nd", "3,3rd", "4,4th", "5,5th",
    "6,6th", "7,7th", "8,8th", "9,9th"
  })
  @DisplayName("single digits")
  void singleDigits(int num, String expectedResult) {
    assertEquals(expectedResult, Numbers.asOrdinal(num));
  }

  @ParameterizedTest(name = "Test: {0}->{1}")
  @MethodSource("teensTestCases")
  @DisplayName("the teens")
  void teens(int num, String expectedResult) {
    assertEquals(expectedResult, Numbers.asOrdinal(num));
  }
  static Stream<Arguments> teensTestCases() {
    return IntStream.rangeClosed(10,19)
      .mapToObj(i -> Arguments.of(i, i+"th"));
  }

  @ParameterizedTest(name = "Test: {0}->{1}")
  @MethodSource("otherTwoDigitTestCases")
  @DisplayName("other two digit numbers")
  void otherTwoDigits(int num, String expectedResult) {
    assertEquals(expectedResult, Numbers.asOrdinal(num));
  }
  static Stream<Arguments> otherTwoDigitTestCases() {
    return buildArgumentsFromMap(Map.of(
      21, "21st",
      22, "22nd",
      23, "23rd",
      24, "24th",
      31, "31st",
      32, "32nd",
      33, "33rd",
      34, "34th"
    ));
  }

  @Test
  @DisplayName("defensive programming: no negative numbers")
  void negativeNumbers() {
    assertThrows(IllegalArgumentException.class, () -> Numbers.asOrdinal(-1));
  }

  @ParameterizedTest(name = "Test: {0}->{1}")
  @MethodSource("largeNumberTestCases")
  @DisplayName("other large numbers")
  void largeNumbers(int num, String expectedResult) {
    assertEquals(expectedResult, Numbers.asOrdinal(num));
  }
  static Stream<Arguments> largeNumberTestCases() {
    return buildArgumentsFromMap(Map.of(
      420, "420th",
      421, "421st",
      422, "422nd",
      423, "423rd",
      424, "424th",
      // default locale (US-en)
      5_011, "5,011th",
      42_021, "42,021st"
    ));
  }

  @ParameterizedTest(name = "Test: {0}->{1} for {2}")
  @MethodSource("largeNumber_L10N_TestCases")
  @DisplayName("large numbers with L10N support")
  void largeNumbers_L10N(int num, String expectedResult, Locale locale) {
    final Locale defaultLocale = Locale.getDefault();
    try {
      Locale.setDefault(locale);
      assertEquals(expectedResult, Numbers.asOrdinal(num));
    } finally {
      Locale.setDefault(defaultLocale);
    }
  }
  static Stream<Arguments> largeNumber_L10N_TestCases() {
    return Stream.of(
      // Italian uses periods
      Arguments.of(5_011, "5.011th", Locale.ITALIAN),
      Arguments.of(42_021, "42.021st", Locale.ITALIAN),
      // French uses a "narrow non-breaking space"
      Arguments.of(5_011, "5 011th".replace(' ', ' '), Locale.FRENCH),
      Arguments.of(42_021, "42 021st".replace(' ', ' '), Locale.FRENCH)
    );
  }

  private static Stream<Arguments> buildArgumentsFromMap(final Map<Integer, String> testCases) {
    return testCases.entrySet().stream()
      .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
  }
}
