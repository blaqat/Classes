package edu.rit.swen253.utils;

import org.openqa.selenium.Dimension;

import java.util.Optional;

/**
 * An enumeration of screen modes that are supported for integration testing.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public enum ScreenMode {
  DESKTOP(new Dimension(1280, 800)),
  // FIXME: choose a better (representative) mobile device screen size
  PHONE(new Dimension(375, 800)) // iPhone 6,7,8
  ;

  /**
   * Discover the {@linkplain ScreenMode screen mode} from the
   * custom Java property, {@code screenMode}.
   *
   * @return  the {@link ScreenMode} to run the UI tests against
   *
   * @throws IllegalArgumentException  if the ScreenMode is not recognized
   */
  public static synchronized ScreenMode discover() {
    if (cachedScreenMode == null) {
      cachedScreenMode = Optional.ofNullable(System.getProperty("screenMode"))
        .map(ScreenMode::valueOf)
        .orElse(ScreenMode.DESKTOP);
    }
    return cachedScreenMode;
  }
  private static volatile ScreenMode cachedScreenMode;

  public Dimension getScreenSize() {
    return screenSize;
  }

  private final Dimension screenSize;

  ScreenMode(Dimension screenSize) {
    this.screenSize = screenSize;
  }
}
