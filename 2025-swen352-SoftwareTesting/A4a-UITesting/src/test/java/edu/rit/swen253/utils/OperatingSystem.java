package edu.rit.swen253.utils;

/**
 * An enumeration of operating systems that are supported for Web UI testing.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public enum OperatingSystem {
  WINDOWS, LINUX, MAC_OSX
  ;

  /**
   * Discover the {@linkplain OperatingSystem operating system} from the
   * standard Java property, {@code os.name}.
   *
   * @return  the {@link OperatingSystem} that UI test is running under
   *
   * @throws IllegalArgumentException  if the OS is not recognized
   */
  public static synchronized OperatingSystem discover() {
    if (cachedOS == null) {
      cachedOS = _discover();
    }
    return cachedOS;
  }
  private static volatile OperatingSystem cachedOS;

  /**
   * Private implementation of the OS discovery process.
   * Pulled-out to make it easier to see the caching code in the
   * public {@link #discover()} method.
   */
  private static OperatingSystem _discover() {
    final String osName = System.getProperty("os.name");
    if (osName.startsWith("Windows")) {
      return WINDOWS;
    } else if (osName.equals("Mac OS X")) {
      return MAC_OSX;
    } else if (osName.equals("Linux")) {
      return LINUX;
    } else {
      throw new IllegalArgumentException("Unsupported OS: " + osName);
    }
  }
}
