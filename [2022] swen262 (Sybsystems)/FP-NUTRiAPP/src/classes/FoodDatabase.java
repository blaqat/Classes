package src.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import src.interfaces.DBConnector;
import src.interfaces.Food;

/**
 * A "Database class" tends to manipulate the CSV file.
 * @author Daniel Chung
 */

public class FoodDatabase{
     
    private DBConnector dbConnector;
    private Hashtable<String, Integer> foodQuantity;
    private Hashtable<String, Food> foodsDB;

    public FoodDatabase(String file) throws FileNotFoundException, ParserConfigurationException{
        String[] splitted = file.split("\\.");
        if (splitted[1].equals("csv")){
            dbConnector = new CSVConnector(file);
            dbConnector.readFile();
        }
        if (splitted[1].equals("json")){
            dbConnector = new JSONConnector(file);
            dbConnector.readFile();
        }
        if (splitted[1].equals("xml")){
            dbConnector = new XMLConnector(file);
            dbConnector.readFile();
        }
        foodQuantity = dbConnector.getQuantity();
        foodsDB = dbConnector.getDatabase();
    }

    public Boolean foodExist(String name){
        if (foodsDB.get(name) != null && foodQuantity.get(name) > 0){
            return true;
        }
        return null;
    }
    
    //Acts more as a pop method, because in theory it removes 
    //when taking out of the foodQuantity dictionary.
    public Food getFood(String name){
        if (foodsDB.get(name) != null && foodQuantity.get(name) > 0){
            foodQuantity.put(name, foodQuantity.get(name) - 1);
            return foodsDB.get(name);
        }
        else{
            return null;
        }
    }

    public Double getFoodCalories(String name){
        return foodsDB.get(name).calculateCalories();
    }

    public void addFood(Food food){
        Ingredients ingredient =  (Ingredients) food;
        this.addFood(ingredient.getName(), ingredient.calculateCalories(), ingredient.getFibers(), ingredient.getFats(), ingredient.getProteins(), ingredient.getCarbohydrates());
    }

    public void addFood(String name, Double calories, Double fibers, Double fat, Double protein, Double carbohydrates){
        if (foodQuantity.get(name) == null){
            Food ingredient = new Ingredients(name, calories, fibers, fat, protein, carbohydrates);
            foodQuantity.put(name, 1);
            foodsDB.put(name, ingredient);
            System.out.println("Ingredient Added");
        }
        else{
            foodQuantity.put(name, foodQuantity.get(name) + 1);
            System.out.println("Ingredient Added");
        }
    }

    public void displayFoods(){
        Set<String> keys = foodsDB.keySet();
        System.out.println("Displaying foods");
        System.out.println("----------------------------------");
        for (String key : keys){
            System.out.println(key);
        }
        System.out.println("----------------------------------");
    }

    public Boolean foodExists(String foodname){
        return foodsDB.containsKey(foodname);
    }

    public void saveData() throws IOException{
        dbConnector.saveData(this.foodQuantity, this.foodsDB);
    }
}
