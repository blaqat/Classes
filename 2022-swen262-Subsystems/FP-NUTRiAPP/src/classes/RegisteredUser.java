package src.classes;

import java.io.IOException;

import src.interfaces.User;

public class RegisteredUser implements User {
    String username;
    String password;
    Profile profile;

    public RegisteredUser(String username, String password, Profile profile){
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    public User logout(){
        try {
            profile.saveProfile();
        } catch (IOException e) {
        }

        return new GuestUser();
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public Profile getProfile(){
        return this.profile;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void displayFoodStock(FoodDatabase foods){
        foods.displayFoods();
    }

    public void displayMeals(FoodDatabase foods){

    }

    public void displayRecipies(FoodDatabase foods){

    }

    public boolean isAuthenticated() {
        return true;
    }
}
