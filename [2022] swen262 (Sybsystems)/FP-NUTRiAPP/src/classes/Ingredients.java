package src.classes;

import src.interfaces.Food;

/**
 * A Class representing ingredients
 * @author Daniel Chung
 */

public class Ingredients implements Food{
    private String name; 
    private Double calories;
    private Double fibers;
    private Double fat; 
    private Double protein; 
    private Double carbohydrates; 

    public Ingredients(String name, Double calories, Double fibers, Double fat, Double protein, Double carbohydrates){
        this.name = name; 
        this.calories = calories;
        this.fibers = fibers;
        this.fat = fat; 
        this.protein = protein;
        this.carbohydrates = carbohydrates;
    }

    public String getName(){
        return this.name;
    }

    public double getFibers(){
        return this.fibers;
    }

    public double getFats(){
        return this.fat;
    }

    public double getProteins(){
        return this.protein;
    }

    public double getCarbohydrates(){
        return this.carbohydrates;
    }

    public double calculateCalories(){
        return this.calories;
    }

    public void display(){
        String ingredient = String.format("(%s, calories: %f, fibers: %f, fat: %f, protein: %f, carbs: %f)", this.name, this.calories, this.fibers, this.fat, this.protein, this.carbohydrates);
        System.out.println(ingredient);
    }
}
