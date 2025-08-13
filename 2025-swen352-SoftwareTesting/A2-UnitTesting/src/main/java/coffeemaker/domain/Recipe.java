package coffeemaker.domain;

import coffeemaker.exceptions.RecipeException;

/**
 * A Recipe is an Entity component that maintains the data for a specific coffee product.
 * It must have a non-null String name and non-negative price and ingredient amounts.
 *
 * @author Sarah Heckman
 */
public class Recipe {

  private String name;
  private int price;
  private int amtCoffee;
  private int amtMilk;
  private int amtSugar;
  private int amtChocolate;

  /**
   * Creates a default recipe for the coffee maker.
   */
  public Recipe() {
    this.name = "";
    this.price = 0;
    this.amtCoffee = 0;
    this.amtMilk = 0;
    this.amtSugar = 0;
    this.amtChocolate = 0;
  }

  /**
   * Get the name of this recipe.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of this recipe.
   * @param name The name to set; must have at least one non-blank character.
   */
  public void setName(String name) {
    if (name != null) {
      this.name = name;
    }
  }

  /**
   * Get the amount of price needed for this recipe.
   */
  public int getPrice() {
    return price;
  }

  /**
   * Set the amount of price needed for this recipe.
   * @param price The price to set.
   */
  public void setPrice(String price) throws RecipeException {
    int amtPrice;
    try {
      amtPrice = Integer.parseInt(price);
    } catch (NumberFormatException e) {
      throw new RecipeException("Price must be a positive integer");
    }
    if (amtPrice >= 0) {
      this.price = amtPrice;
    } else {
      throw new RecipeException("Price must be a positive integer");
    }
  }

  /**
   * Get the amount of coffee needed for this recipe.
   */
  public int getAmtCoffee() {
    return amtCoffee;
  }

  /**
   * Set the amount of coffee needed for this recipe.
   * @param coffee  The amount of coffee to set.
   */
  public void setAmtCoffee(String coffee) throws RecipeException {
    int amtCoffee;
    try {
      amtCoffee = Integer.parseInt(coffee);
    } catch (NumberFormatException e) {
      throw new RecipeException("Units of coffee must be a positive integer");
    }
    if (amtCoffee >= 0) {
      this.amtCoffee = amtCoffee;
    } else {
      throw new RecipeException("Units of coffee must be a positive integer");
    }
  }

  /**
   * Get the amount of milk needed for this recipe.
   */
  public int getAmtMilk() {
    return amtMilk;
  }

  /**
   * Set the amount of milk needed for this recipe.
   * @param milk  The amount of milk to set.
   */
  public void setAmtMilk(String milk) throws RecipeException {
    int amtMilk;
    try {
      amtMilk = Integer.parseInt(milk);
    } catch (NumberFormatException e) {
      throw new RecipeException("Units of milk must be a positive integer");
    }
    if (amtMilk >= 0) {
      this.amtMilk = amtMilk;
    } else {
      throw new RecipeException("Units of milk must be a positive integer");
    }
  }

  /**
   * Get the amount of sugar needed for this recipe.
   */
  public int getAmtSugar() {
    return amtSugar;
  }

  /**
   * Set the amount of sugar needed for this recipe.
   * @param sugar  The amount of sugar to set.
   */
  public void setAmtSugar(String sugar) throws RecipeException {
    int amtSugar;
    try {
      amtSugar = Integer.parseInt(sugar);
    } catch (NumberFormatException e) {
      throw new RecipeException("Units of sugar must be a positive integer");
    }
    if (amtSugar >= 0) {
      this.amtSugar = amtSugar;
    } else {
      throw new RecipeException("Units of sugar must be a positive integer");
    }
  }

  /**
   * Get the amount of chocolate needed for this recipe.
   */
  public int getAmtChocolate() {
    return amtChocolate;
  }

  /**
   * Set the amount of chocolate needed for this recipe.
   * @param chocolate  The amount of chocolate to set.
   */
  public void setAmtChocolate(String chocolate) throws RecipeException {
    int amtChocolate;
    try {
      amtChocolate = Integer.parseInt(chocolate);
    } catch (NumberFormatException e) {
      throw new RecipeException("Units of chocolate must be a positive integer");
    }
    if (amtChocolate >= 0) {
      this.amtChocolate = amtChocolate;
    } else {
      throw new RecipeException("Units of chocolate must be a positive integer");
    }
  }

  /**
   * Returns a textual representation of a recipe using its name.
   */
  public String toString() {
    return String.format("Recipe{%s}", name);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Recipe other = (Recipe) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

}
