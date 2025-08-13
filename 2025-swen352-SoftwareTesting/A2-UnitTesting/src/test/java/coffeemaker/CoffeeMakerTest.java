package coffeemaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Sample component test suite.
 */
public class CoffeeMakerTest {

	/**
	 * Component under Test.
	 */
	private CoffeeMaker CuT;

	@BeforeEach
	public void setUp() {
		// add setup here
//		CuT = new CoffeeMaker();
	}

	@AfterEach
	public void tearDown() {
		// add tear down here
//		CuT = null;
	}

	@Test
	@DisplayName("Sample test that just succeeds.  Ignore it.")
	public void tautology() {
		assertTrue(true, "Always pass.");
	}

}
