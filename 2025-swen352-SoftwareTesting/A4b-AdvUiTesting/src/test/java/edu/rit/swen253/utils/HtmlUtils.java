package edu.rit.swen253.utils;

import org.openqa.selenium.By;

/**
 * A Utility class for HTML: tag names, helper methods, etc.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class HtmlUtils {

  //
  // General tags
  //

  public static final String DIV_TAG = "div";
  public static final By DIV_FINDER = By.tagName(DIV_TAG);
  public static final String ANCHOR_TAG = "a";
  public static final By ANCHOR_FINDER = By.tagName(ANCHOR_TAG);
  public static final String BODY_TAG = "body";
  public static final By BODY_FINDER = By.xpath("/html/body");
  public static final By SPAN_FINDER = By.tagName("span");
  public static final By LABEL_FINDER = By.tagName("label");
  public static final By PARAGRAPH_FINDER = By.tagName("p");
  public static final By SECTION_FINDER = By.tagName("section");
  public static final By IMG_FINDER = By.tagName("img");

  //
  // List tags
  //

  public static final By LIST_FINDER = By.tagName("ul");
  public static final By LIST_ITEM_FINDER = By.tagName("li");

  //
  // table tags
  //

  public static final String TABLE_TAG = "table";
  public static final String TABLE_HEAD_TAG = "thead";
  public static final String TABLE_HEADER_TAG = "th";
  public static final String TABLE_BODY_TAG = "tbody";
  public static final String TABLE_ROW_TAG = "tr";
  public static final String TABLE_CELL_TAG = "td";
  public static final By TABLE_CELL_FINDER = By.tagName(TABLE_CELL_TAG);

  //
  // form tags
  //

  public static final String FORM_TAG = "form";
  public static final By FORM_FINDER = By.tagName(FORM_TAG);
  public static final String LISTBOX_TAG = "select";
  public static final String BUTTON_TAG = "button";
  public static final By BUTTON_FINDER = By.tagName(BUTTON_TAG);
  public static final String INPUT_TAG = "input";
  public static final By INPUT_TAG_FINDER = By.tagName(INPUT_TAG);
  public static final String TEXTAREA_TAG = "textarea";
  public static final By TEXTAREA_TAG_FINDER = By.tagName(TEXTAREA_TAG);


  //
  // HTML element attributes
  //

  public static final String ID_ATTR = "id";
  public static final String CLASS_ATTR = "class";
  public static final String VALUE_ATTR = "value";
  public static final String DISABLED_ATTR = "disabled";
  public static final String CHECKED_ATTR = "checked";
  public static final String NAME_ATTR = "name";
  public static final String PLACEHOLDER_ATTR = "placeholder";
  public static final String SRC_ATTR = "src";
  public static final String HREF_ATTR = "href";
  public static final String TITLE_ATTR = "title";

  //
  // CSS attributes
  //

  public static final String STYLE_DISPLAY = "display";


  /* hide ctor to complete the Utility idiom */
  private HtmlUtils() {
  }
}
