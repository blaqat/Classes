package src.classes;

import java.util.Scanner;

import src.classes.FoodDatabase;
import src.interfaces.User;

public class GuestUser implements User {

    RegisteredUser realUser;
    boolean authenticated;

    public GuestUser(){
        this.realUser = UsersManager.getUser("Guest");
        this.authenticated = false;
    }
    
    public User authenticate(String username, String password){
       RegisteredUser possibleUser = UsersManager.getUser(username);

       if(possibleUser != null && possibleUser.getPassword().equals(password)){
           this.realUser = possibleUser;
           this.authenticated = true;
           return possibleUser;
       } else {
           return this;
       }
    }

    public RegisteredUser createAccount(){
        Scanner userInput = new Scanner(System.in);
        System.out.println("Creating an account:");
        System.out.println("Enter your username:");
        String username = userInput.nextLine();
        String password;
        do {
            System.out.println("Enter your password:");
            password = userInput.nextLine();
            System.out.println("Re-Enter your password:");
            if(!password.equals(userInput.nextLine())){
                System.out.println("Incorrect password, try again.");
                password = null;
            }
        } while (password == null);
        System.out.println("Account Created.");

        System.out.println("\nCreating a profile: ");
        
        System.out.println("Enter your name: ");
        String name = userInput.nextLine();
        System.out.println("Enter your height (ft.in): ");
        double height = Double.parseDouble(userInput.nextLine());
        System.out.println("Enter your weight (xx.xx): ");
        double weight = Double.parseDouble(userInput.nextLine());
        System.out.println("Enter your target weight (xx.xx): ");
        Double targetWeight = Double.parseDouble(userInput.nextLine());

        Profile profile = new Profile(name, height, weight, targetWeight);
        RegisteredUser newUser = new RegisteredUser(username, password, profile);

        UsersManager.newUser(newUser);
        this.authenticated = true;

        return newUser;
    }

    public void displayFoodStock(FoodDatabase foods) {
        realUser.displayFoodStock(foods);
    }

    public void displayRecipies(FoodDatabase foods) {
        realUser.displayRecipies(foods);
    }

    public void displayMeals(FoodDatabase foods) {
        realUser.displayMeals(foods);
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public Profile getProfile(){
        return this.realUser.getProfile();
    }

    public String getUsername(){
        return "Guest";
    }
    
}
