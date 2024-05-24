package src.interfaces;

import src.classes.FoodDatabase;
import src.classes.Profile;

public interface User {

    public void displayFoodStock(FoodDatabase foods);

    public void displayRecipies(FoodDatabase foods);

    public void displayMeals(FoodDatabase foods);

    public Profile getProfile();

    public String getUsername();

    public boolean isAuthenticated();
    
}
