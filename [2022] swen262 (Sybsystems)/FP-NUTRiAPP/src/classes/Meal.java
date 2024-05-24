package src.classes;

import java.util.ArrayList;

import src.interfaces.Food;
/**
 * A class representing a Meal
 * @author Daniel Chung
 */

public class Meal implements Food{
    private String name;
    private ArrayList<Recipes> recipes; 

    public Meal(String name){
        this.name = name;
        this.recipes = new ArrayList<Recipes>();
    }

    public double calculateCalories(){
        Double totalCalories = 0.00;
        for(int i = 0; i < recipes.size(); i++){
            totalCalories += recipes.get(i).calculateCalories();
        }
        return totalCalories;
    }

    //TODO
    public void display(){
        System.out.println(this.name);
    }
    
    public String getName(){
        return this.name;
    }

    public void addRecipe(Recipes recipe){
        this.recipes.add(recipe);
    }

    @Override
    public String toString() {
        String meal = "[Meal - " + this.name + "]\nRecipes:\n";
        for(Recipes recipe: this.recipes){
            meal += recipe.getName() + ", ";
        }
        return meal;
    }

    @Override
    public double getFibers() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getFats() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getCarbohydrates() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getProteins() {
        // TODO Auto-generated method stub
        return 0;
    }
}
