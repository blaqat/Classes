package coffeemaker;

import coffeemaker.domain.Inventory;
import coffeemaker.domain.Recipe;
import coffeemaker.domain.RecipeBook;
import coffeemaker.exceptions.InventoryException;

/**
 * @author Sarah Heckman
 */
public class CoffeeMaker {

  /**
   * Array of recipes in coffee maker
   */
  private final RecipeBook recipeBook;

  /**
   * Inventory of the coffee maker
   */
  private final Inventory inventory;

  /**
   * Constructor for the coffee maker
   */
  public CoffeeMaker() {
    recipeBook = new RecipeBook();
    inventory = new Inventory();
  }

  /**
   * Returns true if the recipe is added to the
   * list of recipes in the CoffeeMaker and false
   * otherwise.
   *
   * @return boolean
   */
  public boolean addRecipe(Recipe r) {
    return recipeBook.addRecipe(r);
  }

  /**
   * Returns the name of the successfully deleted recipe
   * or null if the recipe cannot be deleted.
   *
   * @return String
   */
  public String deleteRecipe(int recipeToDelete) {
    return recipeBook.deleteRecipe(recipeToDelete);
  }

  /**
   * Returns the name of the successfully edited recipe
   * or null if the recipe cannot be edited.
   *
   * @return String
   */
  public String editRecipe(int recipeToEdit, Recipe r) {
    return recipeBook.replaceRecipe(recipeToEdit, r);
  }

  /**
   * Returns true if inventory was successfully added
   *
   * @return boolean
   */
  public synchronized void addInventory(String amtCoffee, String amtMilk, String amtSugar,
      String amtChocolate) throws InventoryException {
    inventory.addCoffee(amtCoffee);
    inventory.addMilk(amtMilk);
    inventory.addSugar(amtSugar);
    inventory.addChocolate(amtChocolate);
  }

  /**
   * Returns the inventory of the coffee maker
   *
   * @return Inventory
   */
  public synchronized String checkInventory() {
    return inventory.toString();
  }

  /**
   * Returns the change of a user's beverage purchase, or
   * the user's money if the beverage cannot be made
   *
   * @return int
   */
  public synchronized int makeCoffee(int recipeToPurchase, int amtPaid) {
    int change = 0;

    if (getRecipes()[recipeToPurchase] == null) {
      change = amtPaid;
    } else if (getRecipes()[recipeToPurchase].getPrice() <= amtPaid) {
      if (inventory.useIngredients(getRecipes()[recipeToPurchase])) {
        change = amtPaid - getRecipes()[recipeToPurchase].getPrice();
      } else {
        change = amtPaid;
      }
    } else {
      change = amtPaid;
    }

    return change;
  }

  /**
   * Returns the list of Recipes in the RecipeBook.
   *
   * @return Recipe []
   */
  public synchronized Recipe[] getRecipes() {
    return recipeBook.getRecipes();
  }
}
