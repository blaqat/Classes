package coffeemaker.domain;

import coffeemaker.exceptions.InventoryException;

/**
 * The inventory for the coffee maker.
 * <p>
 * The inventory keeps track of the four ingredients
 * used by the coffee maker: coffee, milk, sugar and chocolate.
 * <p>
 * These quantities must never be negative.
 *
 * @author Sarah Heckman
 * @author Bryan Basham
 */
public class Inventory {

  private int coffee;
  private int milk;
  private int sugar;
  private int chocolate;

  /**
   * Creates a coffee maker inventory object and
   * fills each item in the inventory with 15 units.
   */
  public Inventory() {
    setCoffee(15);
    setMilk(15);
    setSugar(15);
    setChocolate(15);
  }

  /**
   * Returns the current number of coffee units in
   * the this.
   *
   * @return int
   */
  public int getCoffee() {
    return coffee;
  }

  /**
   * Sets the number of coffee units in the inventory
   * to the specified amount.
   */
  public void setCoffee(int coffee) {
    if (coffee >= 0) {
      this.coffee = coffee;
    }
  }

  /**
   * Add the number of coffee units in the inventory
   * to the current amount of coffee units.
   */
  public void addCoffee(String coffee) throws InventoryException {
    int amtCoffee = 0;
    try {
      amtCoffee = Integer.parseInt(coffee);
    } catch (NumberFormatException e) {
      throw new InventoryException("Units of coffee must be a positive integer");
    }
    if (amtCoffee >= 0) {
      this.coffee += amtCoffee;
    } else {
      throw new InventoryException("Units of coffee must be a positive integer");
    }
  }

  /**
   * Returns the current number of milk units in
   * the this.
   *
   * @return int
   */
  public int getMilk() {
    return milk;
  }

  /**
   * Sets the number of milk units in the inventory
   * to the specified amount.
   */
  public void setMilk(int milk) {
    if (milk >= 0) {
      this.milk = milk;
    }
  }

  /**
   * Add the number of milk units in the inventory
   * to the current amount of milk units.
   */
  public void addMilk(String milk) throws InventoryException {
    int amtMilk = 0;
    try {
      amtMilk = Integer.parseInt(milk);
    } catch (NumberFormatException e) {
      throw new InventoryException("Units of milk must be a positive integer");
    }
    if (amtMilk >= 0) {
      this.milk += amtMilk;
    } else {
      throw new InventoryException("Units of milk must be a positive integer");
    }
  }

  /**
   * Returns the current number of sugar units in
   * the this.
   *
   * @return int
   */
  public int getSugar() {
    return sugar;
  }

  /**
   * Sets the number of sugar units in the inventory
   * to the specified amount.
   */
  public void setSugar(int sugar) {
    if (sugar >= 0) {
      this.sugar = sugar;
    }
  }

  /**
   * Add the number of sugar units in the inventory
   * to the current amount of sugar units.
   */
  public void addSugar(String sugar) throws InventoryException {
    int amtSugar = 0;
    try {
      amtSugar = Integer.parseInt(sugar);
    } catch (NumberFormatException e) {
      throw new InventoryException("Units of sugar must be a positive integer");
    }
    if (amtSugar >= 0) {
      this.sugar += amtSugar;
    } else {
      throw new InventoryException("Units of sugar must be a positive integer");
    }
  }

  /**
   * Returns the current number of chocolate units in
   * the this.
   *
   * @return int
   */
  public int getChocolate() {
    return chocolate;
  }

  /**
   * Sets the number of chocolate units in the inventory
   * to the specified amount.
   */
  public void setChocolate(int chocolate) {
    if (chocolate >= 0) {
      this.chocolate = chocolate;
    }

  }

  /**
   * Add the number of chocolate units in the inventory
   * to the current amount of chocolate units.
   */
  public void addChocolate(String chocolate) throws InventoryException {
    int amtChocolate = 0;
    try {
      amtChocolate = Integer.parseInt(chocolate);
    } catch (NumberFormatException e) {
      throw new InventoryException("Units of chocolate must be a positive integer");
    }
    if (amtChocolate >= 0) {
      this.chocolate += amtChocolate;
    } else {
      throw new InventoryException("Units of chocolate must be a positive integer");
    }
  }

  /**
   * Returns true if there are enough ingredients to make
   * the beverage.
   *
   * @return boolean
   */
  protected boolean enoughIngredients(Recipe r) {
    boolean isEnough = true;
    if (this.coffee < r.getAmtCoffee()) {
      isEnough = false;
    }
    if (this.milk < r.getAmtMilk()) {
      isEnough = false;
    }
    if (this.sugar < r.getAmtSugar()) {
      isEnough = false;
    }
    if (this.chocolate < r.getAmtChocolate()) {
      isEnough = false;
    }
    return isEnough;
  }

  /**
   * Removes the ingredients used to make the specified
   * recipe.  Assumes that the user has checked that there
   * are enough ingredients to make
   */
  public boolean useIngredients(Recipe r) {
    if (enoughIngredients(r)) {
      this.coffee -= r.getAmtCoffee();
      this.milk -= r.getAmtMilk();
      this.sugar -= r.getAmtSugar();
      this.chocolate -= r.getAmtChocolate();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns a string describing the current contents
   * of the this.
   *
   * @return String
   */
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("Coffee: ");
    buf.append(getCoffee());
    buf.append("\n");
    buf.append("Milk: ");
    buf.append(getMilk());
    buf.append("\n");
    buf.append("Sugar: ");
    buf.append(getSugar());
    buf.append("\n");
    buf.append("Chocolate: ");
    buf.append(getChocolate());
    buf.append("\n");
    return buf.toString();
  }
}
