package coffeemaker.domain;

import coffeemaker.exceptions.InventoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

	private Inventory inventory;

	@BeforeEach
	void setUp() {
		inventory = new Inventory();
	}

	// Sets the inventory to specific values for testing
	void initializeInventory(int coffee, int milk, int sugar, int chocolate) {
		inventory.setCoffee(coffee);
		inventory.setMilk(milk);
		inventory.setSugar(sugar);
		inventory.setChocolate(chocolate);
	}

	@Test
	@DisplayName("Inventory ctor initializes all ingredients to 15")
	void constructorInitializesAllIngredientsTo15() {
		assertAll(
				() -> assertEquals(15, inventory.getCoffee(), "Coffee should be initialized to 15"),
				() -> assertEquals(15, inventory.getMilk(), "Milk should be initialized to 15"),
				() -> assertEquals(15, inventory.getSugar(), "Sugar should be initialized to 15"),
				() -> assertEquals(15, inventory.getChocolate(), "Chocolate should be initialized to 15"));
	}

	@ParameterizedTest(name = "setCoffee {0} = {1}")
	@CsvSource({
			"0, 0",
			"10, 10",
			"-1, 15", // Negative values should not change the inventory
	})
	void setCoffee(int coffee, int expected) {
		inventory.setCoffee(coffee);
		assertEquals(expected, inventory.getCoffee());
	}

	@ParameterizedTest(name = "setMilk {0} = {1}")
	@CsvSource({
			"0, 0",
			"10, 10",
			"-1, 15", // Negative values should not change the inventory
	})
	void setMilk(int milk, int expected) {
		inventory.setMilk(milk);
		assertEquals(expected, inventory.getMilk());
	}

	@ParameterizedTest(name = "setSugar {0} = {1}")
	@CsvSource({
			"0, 0",
			"10, 10",
			"-1, 15", // Negative values should not change the inventory
	})
	void setSugar(int sugar, int expected) {
		inventory.setSugar(sugar);
		assertEquals(expected, inventory.getSugar());
	}

	@ParameterizedTest(name = "setChocolate {0} = {1}")
	@CsvSource({
			"0, 0",
			"10, 10",
			"-1, 15", // Negative values should not change the inventory
	})
	void setChocolate(int chocolate, int expected) {
		inventory.setChocolate(chocolate);
		assertEquals(expected, inventory.getChocolate());
	}

	@ParameterizedTest(name = "addCoffee {0} = {1}")
	@CsvSource({
			"0, 15",
			"10, 25",
	})
	void addCoffeeValid(String coffee, int expected) {
		inventory.addCoffee(coffee);
		assertEquals(expected, inventory.getCoffee());
	}

	@ParameterizedTest(name = "addCoffee {0} throws InventoryException")
	@CsvSource({
			"-1",
			"Add this coffee pls",
	})
	void addCoffeeInvalid(String coffee) {
		assertThrows(InventoryException.class, () -> inventory.addCoffee(coffee));
	}

	@ParameterizedTest(name = "addMilk {0} = {1}")
	@CsvSource({
			"0, 15",
			"10, 25",
	})
	void addMilkValid(String milk, int expected) {
		inventory.addMilk(milk);
		assertEquals(expected, inventory.getMilk());
	}

	@ParameterizedTest(name = "addMilk {0} throws InventoryException")
	@CsvSource({
			"-1",
			"Add this milk pls",
	})
	void addMilkInvalid(String milk) {
		assertThrows(InventoryException.class, () -> inventory.addMilk(milk));
	}

	@ParameterizedTest(name = "addSugar {0} = {1}")
	@CsvSource({
			"0, 15",
			"10, 25",
	})
	void addSugarValid(String sugar, int expected) {
		inventory.addSugar(sugar);
		assertEquals(expected, inventory.getSugar());
	}

	@ParameterizedTest(name = "addSugar {0} throws InventoryException")
	@CsvSource({
			"-1",
			"Add this sugar pls",
	})
	void addSugarInvalid(String sugar) {
		assertThrows(InventoryException.class, () -> inventory.addSugar(sugar));
	}

	@ParameterizedTest(name = "addChocolate {0} = {1}")
	@CsvSource({
			"0, 15",
			"10, 25",
	})
	void addChocolateValid(String chocolate, int expected) {
		inventory.addChocolate(chocolate);
		assertEquals(expected, inventory.getChocolate());
	}

	@ParameterizedTest(name = "addChocolate {0} throws InventoryException")
	@CsvSource({
			"-1",
			"Add this chocolate pls",
	})
	void addChocolateInvalid(String chocolate) {
		assertThrows(InventoryException.class, () -> inventory.addChocolate(chocolate));
	}

	@ParameterizedTest(name = "enoughIngredients {0}, {1}, {2}, {3} = {4}")
	@CsvSource({
			"10, 10, 10, 10, true",
			"5, 5, 5, 5, true",
			"0, 0, 0, 0, false",
			"0, 5, 5, 5, false", // Not enough coffee
			"5, 0, 5, 5, false", // Not enough milk
			"5, 5, 0, 5, false", // Not enough sugar
			"5, 5, 5, 0, false" // Not enough chocolate
	})
	void enoughIngredients(int coffee, int milk, int sugar, int chocolate, boolean expected) {
		initializeInventory(coffee, milk, sugar, chocolate);

		Recipe recipe = new Recipe();
		recipe.setAmtCoffee("5");
		recipe.setAmtMilk("5");
		recipe.setAmtSugar("5");
		recipe.setAmtChocolate("5");

		assertEquals(expected, inventory.enoughIngredients(recipe));
	}

	@ParameterizedTest(name = "useIngredients [{0}, {1}, {2}, {3}] - 5 = {4}, {5}, {6}, {7}")
	@CsvSource({
			"5, 6, 7, 8,     0, 1, 2, 3",
			"0, 10, 10, 10,  0, 10, 10, 10", // Not enough coffee
			"10, 0, 10, 10,  10, 0, 10, 10", // Not enough milk
			"10, 10, 0, 10,  10, 10, 0, 10", // Not enough sugar
			"10, 10, 10, 0,  10, 10, 10, 0", // Not enough chocolate
			"0, 0, 0, 0,     0, 0, 0, 0",
	})
	void useIngredients(int coffee, int milk, int sugar, int chocolate,
			int expectedCoffee, int expectedMilk, int expectedSugar, int expectedChocolate) {
		initializeInventory(coffee, milk, sugar, chocolate);

		Recipe recipe = new Recipe();
		recipe.setAmtCoffee("5");
		recipe.setAmtMilk("5");
		recipe.setAmtSugar("5");
		recipe.setAmtChocolate("5");

		inventory.useIngredients(recipe);

		assertAll(
				() -> assertEquals(expectedCoffee, inventory.getCoffee(), "Coffee amount mismatch"),
				() -> assertEquals(expectedMilk, inventory.getMilk(), "Milk amount mismatch"),
				() -> assertEquals(expectedSugar, inventory.getSugar(), "Sugar amount mismatch"),
				() -> assertEquals(expectedChocolate, inventory.getChocolate(), "Chocolate amount mismatch"));
	}

	@Test
	@DisplayName("toString returns expected format")
	void toStringReturnsExpectedFormat() {
		initializeInventory(10, 20, 30, 40);

		String expected = "Coffee: 10\n" +
				"Milk: 20\n" +
				"Sugar: 30\n" +
				"Chocolate: 40\n";

		assertEquals(expected, inventory.toString());
	}
}
