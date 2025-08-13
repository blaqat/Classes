package coffeemaker.domain;

/**
 * The recipe book for the coffee maker.
 * <p>
 * The book can hold a fixed number of {@linkplain Recipe recipes};
 * currently four slots.
 * <p>
 *   There are three operations:
 *   <ul>
 *     <li>Add a new recipe to an open slot in the book.</li>
 *     <li>Delete a recipe as a specific slot in the book.</li>
 *     <li>Replace a recipe as a specific slot in the book.</li>
 *   </ul>
 *   You can also access the complete set of recipes in the book.
 * </p>
 *
 * @author Sarah Heckman
 * @author Bryan Basham
 */
public class RecipeBook {

  /**
   * Number of recipes in coffee maker
   */
  private static final int NUM_RECIPES = 4;

  /**
   * Array of recipes in coffee maker
   */
  private Recipe[] recipeArray;

  /**
   * Default constructor for a RecipeBook.
   */
  public RecipeBook() {
    recipeArray = new Recipe[NUM_RECIPES];
  }

  /**
   * Get the recipe book.
   */
  public Recipe[] getRecipes() {
    return recipeArray;
  }

  /**
   * Add a new {@link Recipe} to the book.
   * <p>
   * The book is filled from front to back; the add operation fills the first available
   * (empty, aka {@code null}) slot and returns {@code true}.
   * <p>
   * If the recipe already exists in the book, then nothing changes
   * and {@code false} is returned.
   *
   * @param newRecipe the new {@link Recipe} to add
   * @return {@code true} if the operation succeeds; otherwise {@code false}
   */
  public boolean addRecipe(Recipe newRecipe) {
    //Assume the recipe doesn't exist in the array until find out otherwise
    boolean exists = false;
    //Check that recipe doesn't already exist in the array
    for (int i = 0; i < recipeArray.length; i++) {
      if (newRecipe.equals(recipeArray[i])) {
        exists = true;
      }
    }
    //Assume the recipe cannot be added until find an empty spot
    boolean added = false;
    //Check for the first empty spot in the array
    if (!exists) {
      for (int i = 0; i < recipeArray.length && !added; i++) {
        if (recipeArray[i] == null) {
          recipeArray[i] = newRecipe;
          added = true;
        }
      }
    }
    return added;
  }

  /**
   * Delete the {@linkplain Recipe recipe} at the slot specified.
   * <p>
   * Returns the name of the recipe deleted at the slot specified,
   * or null if there is no recipe at that slot.
   *
   * @param recipeToDelete the index of the slot in the book
   * @return the name of the old recipe or {@code null} if the delete operation failed
   */
  public String deleteRecipe(int recipeToDelete) {
    if (recipeArray[recipeToDelete] != null) {
      String recipeName = recipeArray[recipeToDelete].getName();
      recipeArray[recipeToDelete] = new Recipe();
      return recipeName;
    } else {
      return null;
    }
  }

  /**
   * Replace the {@linkplain Recipe recipe} at the slot specified with a new one.
   * <p>
   * Returns the name of the recipe replaced at the position specified,
   * or null if there is no recipe at that slot.
   *
   * @param recipeToReplace the index of the slot in the book
   * @param newRecipe the new {@link Recipe} to fill the slot
   * @return the name of the old recipe or {@code null} if the replace operation failed
   */
  public String replaceRecipe(int recipeToReplace, Recipe newRecipe) {
    if (recipeArray[recipeToReplace] != null) {
      String recipeName = recipeArray[recipeToReplace].getName();
      newRecipe.setName("");
      recipeArray[recipeToReplace] = newRecipe;
      return recipeName;
    } else {
      return null;
    }
  }

}
