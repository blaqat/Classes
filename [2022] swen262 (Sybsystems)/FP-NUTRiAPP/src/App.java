package src;

import java.io.*;
import java.util.Scanner;

import src.classes.*;
import src.interfaces.Food;
import src.interfaces.User;
//import src.interfaces.WeightGoal;

/**
 * Acts as the main class that puts everything together.
 * @author Daniel Chung
 */

public class App {

    public Boolean loadData(){
        File userFile = new File("data/profile.csv");
        return userFile.exists();
    }

    public static void displayActions(User userAccount){
        Profile user = userAccount.getProfile();
        if(!userAccount.isAuthenticated()){
            System.out.println("l) Login to Account");
            System.out.println("r) Register New Account");
        } else {
            System.out.println("l) Logout");
            System.out.println("n) Check notifications.");
            System.out.println("w) Do a workout.");
            System.out.println("e) Eat a food.");
            System.out.println("m) Create a meal.");
            System.out.println("c) Check goal.");
            System.out.println("g) Change goal.");
            System.out.println("r) Create a recipe.");
            System.out.println("h) View History.");
            System.out.println("u) Undo.");
            if (user.getTeamStatus()){
                System.out.println("in) Send a team invite.");
            }
            else {
                System.out.println("t) Create a team.");
            }
        }
        System.out.println("v) View Ingredients List");
        System.out.println("h) Help.");
        System.out.println("q) Quit.");
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        UsersManager.loadUsers();
        Teams teamList = new Teams();

        System.out.println("Enter ingredients file name: ");
        Scanner userFile = new Scanner(System.in);
        String fileName = userFile.next();
        FoodDatabase foodDatabase = new FoodDatabase(fileName);

        User userAccount = new GuestUser();
        System.out.println(userAccount.isAuthenticated() + " " + userAccount.getProfile());
        Profile user = userAccount.getProfile();
        Goal weightGoal = new Goal(user);
        Undo undo = new Undo(user.getHistory(), weightGoal, user);

        boolean running = true;
        while(running) {
            System.out.println("Hello, " + userAccount.getUsername());
            System.out.println("What would you like to do?");
            displayActions(userAccount);

            Scanner userInput = new Scanner(System.in);
            String input = userInput.nextLine().toLowerCase();
            boolean answered = true;

            if(!userAccount.isAuthenticated()){
                switch(input){
                    case "l": case "login": 
                        boolean flag = false;
                        do {
                            System.out.println("Please Enter Your Username: ");
                            String username = userInput.nextLine();
                            System.out.println("Please Enter Your Password: ");
                            String password = userInput.nextLine();

                            userAccount = ((GuestUser) userAccount).authenticate(username, password);
                            user = userAccount.getProfile();
                            weightGoal = new Goal(user);
                            undo = new Undo(user.getHistory(), weightGoal, user);

                            if(!userAccount.isAuthenticated()){
                                System.out.println("Invalid login information. \nWould you like to try again? (y/n):");
                                flag = userInput.nextLine().toLowerCase().charAt(0)=='y';
                            } else {
                                weightGoal.checkChangeGoal();
                                System.out.println("Welcome, " + userAccount.getUsername());
                                flag = false;
                            }

                        } while ( flag );
                        break;
                    
                    case "r": case "register":
                        userAccount = ((GuestUser) userAccount).createAccount();
                        user = userAccount.getProfile();
                        weightGoal = new Goal(user);
                        weightGoal.checkChangeGoal();
                        undo = new Undo(user.getHistory(), weightGoal, user);
                        break;

                    default:
                        answered = false;
                        break;
                }
            } else {
                switch(input){
                    case "l": case "logout":
                        userAccount = ((RegisteredUser) userAccount).logout();
                        System.out.println("User has been logged out successfully");
                        break;

                    case "n": case "notif":
                        System.out.println("Not yet implemented");
                        break;

                    case "w": case "workout":
                        boolean flag = true;
                        Workout workout = null;
                        while (flag) {
                            System.out.println("What workout would you like to do? (low, med, high)");
                            input = userInput.nextLine().toLowerCase();
                            switch (input) {
                                case "low" : {
                                    workout = new LowWorkout();
                                    flag = false;
                                }
                                case "med" : {
                                    workout = new MediumWorkout();
                                    flag = false;
                                }
                                case "high" : {
                                    workout = new HighWorkout();
                                    flag = false;
                                }
                                case "q" : {
                                    System.out.println("Quitting...");
                                    flag = false;
                                }
                                default : System.out.println("Err. Input not recognized. Please input either low, med, high, or q");
                            }
                        }
                        flag = true;
                        if (workout != null) {
                            double minutes = 0;
                            while (flag) {
                                System.out.println("How long did you work out for in minutes: ");
                                input = userInput.nextLine().toLowerCase();
                                try {
                                    minutes = Double.parseDouble(input);
                                    flag = false;
                                } catch (NumberFormatException err) {
                                    System.out.println("Err. NaN please try again.");
                                }
                            }
        
                            double caloriesBurned = minutes * workout.getCaloriesPerMinute();
                            System.out.println("You burned " + caloriesBurned + " calories!");
                            weightGoal.subCalories(caloriesBurned);
                            undo.logcommand("w", caloriesBurned);
                        }
                        break;

                    case "e": case "eat":
                        boolean eatFlag = true;
                        while (eatFlag){
                            System.out.println("What food would you like to eat?");
                            System.out.println("Enter food name");
                            System.out.println("or");
                            System.out.println("s) Show available foods");
                            System.out.println("b) Back.");
                            input = userInput.nextLine();
                            if (input.toLowerCase().equals("s")){
                                foodDatabase.displayFoods();
                            }
                            else if(input.toLowerCase().equals("b")){
                                eatFlag = false;
                            }
                            else{
                                System.out.println(input);
                                Food foodToBeConsumed = foodDatabase.getFood(input);
                                System.out.println(foodToBeConsumed);
                                if (foodToBeConsumed == null){
                                    System.out.println("This food has no availability.");
                                }
                                else{
                                    weightGoal.addCalories(foodToBeConsumed.calculateCalories());
                                    foodDatabase.saveData();
                                    eatFlag = false;
                                    System.out.println("You ate " + foodToBeConsumed.getName());
                                    undo.logcommand("e", foodToBeConsumed.calculateCalories());
                                }
                            }
                        }
                        break;

                    case "m": case "meal":
                        boolean mealFlag = true;
                        int step = 0;
                        Meal newMeal = null;
                        while(mealFlag){
                            System.out.println("- Creating a recipe -" + "\n" + newMeal);
                            switch (step) {
                                case 0 : {
                                    System.out.println("What is the name of the recipe?");
                                    String mealName = userInput.nextLine();
                                    newMeal = new Meal(mealName);
                                    step++;
                                }
                                case 1 : {
                                    System.out.println("Adding Recipes:");
                                    System.out.println("State the name of the recipe you would like to add. Or if you are done, say 'done'");
                                    String possibleIngredient = userInput.nextLine();
                                    if (possibleIngredient.equals("done")) {
                                        step++;
                                        break;
                                    }
        
                                    //I don't know how the database works but this should see if what they entered is in the database.
                                    Recipes recipe = (Recipes)foodDatabase.getFood(possibleIngredient);
                                    if (recipe == null) {
                                        System.out.println("Error: This recipe is not in the database. Please try again.");
                                    } else {
                                        newMeal.addRecipe(recipe);
                                    }
                                }
                                case 2 : {
                                    System.out.println("New Meal Created.");
                                    // I don't know where to put the created meal...
                                    // foodDatabase.addMeal(newMeal) or something
                                    mealFlag = false;
                                }
                            }
                        }
                        break;

                    case "c": case "check":
                        weightGoal.checkChangeGoal();
                        System.out.println("Your goal is currently set to " + weightGoal.getWeightGoal().toString());
                        Workout recommendWorkout = weightGoal.reccomendWorkout();
                        if (recommendWorkout == null) {
                            System.out.println("You currently don't have any recommended workouts.");
                        } else {
                            System.out.println("It is recommended you complete a " + recommendWorkout.getName());
                        }
                        System.out.println("You have eaten " + weightGoal.getCurrentConsumedCalories() + " out of " + weightGoal.getTargetCalories() + " calories today.");
                        break;

                    case "g": case "goal":
                        System.out.print("Your current weight goal is " + user.getTargetWeight() + ". What would you like to change it to?");
                        double newWeight = Double.parseDouble(userInput.nextLine());
                        undo.logcommand("ch", user.getTargetWeight());
                        user.setTargetWeight(newWeight);
                        weightGoal.checkChangeGoal();
                        System.out.println("Your new weight goal is "  + weightGoal.getWeightGoal().toString());
                        break;

                    case "r": case "recipe":
                        boolean recFlag = true;
                        int recStep = 0;
                        Recipes newRecipes = null;
                        while(recFlag){
                            System.out.println("- Creating a recipe -" + "\n" + newRecipes);
                            switch (recStep) {
                                case 0 : {
                                    System.out.println("What is the name of the recipe?");
                                    String recipeName = userInput.nextLine();
                                    newRecipes = new Recipes(recipeName);
                                    recStep++;
                                }
                                case 1 : {
                                    System.out.println("Adding Ingredients:");
                                    System.out.println("State the name of the ingredient you would like to add. Or if you are done, say 'done'");
                                    String possibleIngredient = userInput.nextLine();
                                    if (possibleIngredient.equals("done")) {
                                        recStep++;
                                        break;
                                    }
        
                                    //I don't know how the database works but this should see if what they entered is in the database.
                                    Ingredients ingredient = (Ingredients) foodDatabase.getFood(possibleIngredient);
                                    if (ingredient == null) {
                                        System.out.println("Error: This food is not in the database. Please try again.");
                                    } else {
                                        System.out.println("Enter an integer quantity of this ingredient needed");
                                        int quantity = userInput.nextInt();
                                        newRecipes.addIngredient(ingredient, quantity);
                                    }
                                }
                                case 2 : {
                                    System.out.println("Adding Instructions:");
                                    System.out.println("Write the next step in preparation of the recipe. Or if you are done, say 'done'");
                                    String nextStep = userInput.nextLine();
                                    if (nextStep.equals("done") || nextStep.equals("")) {
                                        recStep++;
                                    } else {
                                        newRecipes.addInstruction(nextStep);
                                    }
                                }
                                case 3 : {
                                    System.out.println("New Recipe Created.");
                                    // I don't know where to put the created recipe... but it should be like
                                    // foodDatabase.addRecipe(newRecipe) or something
                                    recFlag = false;
                                }
                            }
                        }
                        break;

                    case "t": case "team":
                        String teamName = "";
                        int teamNum = 0;
                        boolean taken = true;
                        do{
                            System.out.println("Please enter your chosen team name:");
                            teamName = userInput.nextLine();

                            if (teamList.getTeams().size() == 0){
                                taken = false;
                            }
                            for(Team team: teamList.getTeams()){
                                if (team.getName().equalsIgnoreCase(teamName)){
                                    System.out.println("This team name is unavailable.");
                                    taken = true;
                                    break;
                                }
                                else {
                                    taken = false;
                                }
                            }
                        }while(taken);

                        for(Team t: teamList.getTeams()){
                            if (t.getId() > teamNum)
                                teamNum = t.getId();
                        }
                        teamNum = teamNum + 1;
                        teamList.addTeam(new Team(teamNum, teamName));
                        user.manageTeamStatus(true, teamNum);
                        System.out.println("Your team has been created.");
                        break;

                    case "h": case "history":
                        System.out.println("Please select a date. ");
                        System.out.println("Not yet implemented");
                        break;

                    case "u": case "undo":
                        System.out.println("Undoing previous action...");
                        undo.undo();
                        break;

                    default:
                        answered = false;
                        break;
                }
            }

            switch(input){
                case "h": case "help": 
                    System.out.println("You cannot currently access the full app because you are not logged in.");
                    displayActions(userAccount);
                    break;

                case "q": case "quit":
                    System.out.println("Thank you for using NutriApp. Goodbye!");
                    running = false;
                    UsersManager.saveUsers();
                    teamList.saveTeams();
                    break;

                case "v": case "view":
                    foodDatabase.displayFoods();
                    break;

                default:
                    if(!answered){
                        if(!userAccount.isAuthenticated())
                            System.out.println("Either the command you have inputted is not a valid command or the command requires you to be logged in.");
                        else
                            System.out.println("Command not recognized. If you need help, type help or h!");
                    }
                    break;
            }

        }
        
    }
}
