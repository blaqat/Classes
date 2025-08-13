package edu.rit.swen352.tdd;

/**
 * An integral counter with an optional lower and upper limit.
 * The count must start at the lower limit and must not exceed the upper limit.
 * The count must never dip below the lower limit.
 *
 * <p>
 * You must implement these features:
 * <ul>
 *   <li>constructor: with lower and upper limits, one that defaults the lower limit to zero, and a third that defaults the upper limit to MAX_INTEGER.</li>
 *   <li>getLower: access lower limit</li>
 *   <li>getUpper: access upper limit</li>
 *   <li>getCount: access the current state of the counter</li>
 *   <li>increment: increase the count by one; throw {@link IllegalStateException} if count is already at the upper limit</li>
 *   <li>decrement: decrease the count by one; throw {@link IllegalStateException} if count is already at the upper limit</li>
 * </ul>
 */
public class Counter {
}
