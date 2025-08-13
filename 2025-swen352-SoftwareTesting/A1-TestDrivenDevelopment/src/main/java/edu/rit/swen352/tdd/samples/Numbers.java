package edu.rit.swen352.tdd.samples;

import java.text.NumberFormat;

/**
 * Utility class for functions dealing with numbers.
 *
 * <p>
 *   The {@code asOrdinal} function spec:
 *   <ol>
 *     <li>single digits: 1->"1st", 2->"2nd", 3->"3rd", 4->"4th" and so on</li>
 *     <li>teens: 10->"10th", 11->"11th", 12->"12th", 13->"13th", 14->"14th", and so on</li>
 *     <li>other two-digit numbers</li>
 *     <li>defensive programming: no negative numbers</li>
 *     <li>large numbers</li>
 *     <li>L10N support for large numbers</li>
 *   </ol>
 * </p>
 */
public final class Numbers {

  public static String asOrdinal(int number) {
    // defensive check
    if (number < 0) {
      throw new IllegalArgumentException("no negative numbers");
    }
    //
    NumberFormat formatter = NumberFormat.getIntegerInstance();
    final String numAsString = formatter.format(number);
    final int numOfDigits = numAsString.length();
    if (numOfDigits == 1) {
      return singleDigitAsOrdinal(numAsString.charAt(0));
    } else if (numAsString.charAt(numOfDigits-2) == '1') {
      return numAsString + "th";
    } else {
      return numAsString.substring(0, numOfDigits-1)
        + singleDigitAsOrdinal(numAsString.charAt(numOfDigits-1));
    }
  }
  private static String singleDigitAsOrdinal(char digit) {
    return switch (digit) {
      case '1' -> "1st";
      case '2' -> "2nd";
      case '3' -> "3rd";
      default -> digit + "th";
    };
  }

  private Numbers() {}
}
