package src.classes;

import java.util.ArrayList;

import src.interfaces.Food;

/**
 * A class representing recipes.
 * @author Daniel Chung
 */

public class Recipes implements Food{
    private String name;
    private ArrayList<Ingredients> ingredients; 
    private ArrayList<String> instructions;

    public Recipes(String name){
        this.name = name;
        this.ingredients = new ArrayList<Ingredients>();
        this.instructions = new ArrayList<String>();
    }

    public void displayInstructions(){
        for (int i = 0; i < instructions.size(); i++){
            System.out.println(instructions.get(i));
        }
    }

    public double calculateCalories(){
        Double totalCalories = 0.00;
        for(int i = 0; i < ingredients.size(); i++){
            totalCalories += ingredients.get(i).calculateCalories();
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

    public void addIngredient(Ingredients ingredient, int amount){
        for(int i = 0; i < amount; i++){
            this.ingredients.add(ingredient);
        }
    }

    public void addInstruction(String step){
        this.instructions.add(step);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String recipe = "[Recipe - " + this.name + "]\nIngredients:\n";
        Ingredients previousFood = null;

        String ingredient = "";
        int count = 0;
        for(Ingredients food : this.ingredients){
            if(previousFood != null && food.equals(previousFood)){
                count++;
            } 
            else if(count >= 1 && !food.equals(previousFood)){
                recipe += "\t("+ count + ") " + ingredient + "\n";
                count = 0;
            }
            if(count == 0) {
                count = 1;
                previousFood = food;
                ingredient = food.getName();
            }
        }

        recipe += "Instructions:\n";
        for(int i = 0; i < instructions.size(); i++){
            recipe+="\t" + i + ". " + instructions.get(i) +"\n";
        }

        return recipe;
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
