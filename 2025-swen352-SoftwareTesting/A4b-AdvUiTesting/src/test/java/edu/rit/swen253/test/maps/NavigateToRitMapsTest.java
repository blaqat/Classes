package edu.rit.swen253.test.maps;

import edu.rit.swen253.page.SimplePage;
import edu.rit.swen253.page.tiger.TigerCenterHomePage;
import edu.rit.swen253.test.AbstractWebTest;
import edu.rit.swen253.utils.BrowserWindow;
import org.junit.jupiter.api.*;

import static edu.rit.swen253.utils.BrowserType.FIREFOX;
import static edu.rit.swen253.utils.BrowserType.onBrowser;
import static edu.rit.swen253.utils.TimingUtils.sleep;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A simple test that uses the TigerCenter <em>Maps at RIT</em> button to navigate
 * to a new tab that displays the {@code maps.rit.edu} web page.
 *
 * @author Bryan Basham
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NavigateToRitMapsTest extends AbstractWebTest {

  private TigerCenterHomePage homePage;
  private String homeWindow;

  //
  // Test sequence
  //

  @Test
  @Order(1)
  @DisplayName("First, navigate to the Tiger Center Home page.")
  void navigateToHomePage() {
    homePage = navigateToPage("https://tigercenter.rit.edu", TigerCenterHomePage::new);
    assertNotNull(homePage);
    homeWindow = getCurrentWindow();
  }

  @Test
  @Order(2)
  @DisplayName("Second, click on the Maps at RIT button and validate navigation.")
  void navigateToMaps() {
    homePage.selectMapsAtRIT();
    final SimplePage mapsPage = assertNewWindowAndSwitch(SimplePage::new);

    // there's a timing issue with Firefox (give it a second to render)
    if (onBrowser(FIREFOX)) {
      sleep(1);
    }

    assertEquals("https://maps.rit.edu/", mapsPage.getURL());
  }

  void switchToApp() {
    BrowserWindow<TigerCenterHomePage> currentWindow = getWindow(getCurrentWindow(), homePage);
    assertNotSame(homePage, currentWindow.page(), "Before switch");

    switchToWindow(homeWindow);

    BrowserWindow<TigerCenterHomePage> switchedWindow = getWindow(homeWindow, homePage);
    assertSame(homePage, switchedWindow.page(), "After switch");
}
