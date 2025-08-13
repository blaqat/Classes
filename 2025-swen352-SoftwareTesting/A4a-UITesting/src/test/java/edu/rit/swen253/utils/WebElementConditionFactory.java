package edu.rit.swen253.utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.function.Function;

/**
 * The contract for creating an {@link ExpectedCondition} for a given {@link WebElement}.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public interface WebElementConditionFactory extends Function<WebElement, ExpectedCondition<WebElement>> {
  // inherits method declaration from Function
}
