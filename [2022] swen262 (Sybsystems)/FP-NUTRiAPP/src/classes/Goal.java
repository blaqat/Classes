package src.classes;

import java.util.ArrayList;

import src.interfaces.Food;
import src.interfaces.WeightGoal;

/**
 * A class to represent a weight goal. The hub for everything.
 * @author Eliza Ostrowski, Daniel Chung
 */

public class Goal {
    private Profile user;

    private double currentConsumedCalories;
    private ArrayList<Food> foodConsumed;
    private ArrayList<Workout> workoutsPerformed;

    private WeightGoal weightGoal;

    public Goal(Profile user){
        this.user = user;
        setWeightGoal();
    }

    public void setWeightGoal() {
        if( user.getWeight() > user.getTargetWeight()){
            this.weightGoal = new LoseWeight();
        }
        if( user.getWeight() < user.getTargetWeight()){
            this.weightGoal = new GainWeight();
        }
        if (user.getWeight() - 5 >= user.getTargetWeight() || user.getWeight() + 5 <= user.getTargetWeight()){
            this.weightGoal = new MaintainWeight();
        }
    }

    public WeightGoal getWeightGoal() {
        return weightGoal;
    }

    public double getCurrentConsumedCalories() {
        return currentConsumedCalories;
    }

    public double getTargetCalories() {
        return weightGoal.calculateTargetCalories(user.getWeight(), user.getTargetWeight());
    }

    public void checkChangeGoal() {
        weightGoal = weightGoal.checkChangeGoal(user.getWeight(), user.getTargetWeight());
    }

    public void addCalories(double calories) {
        currentConsumedCalories += calories;
    }

    public void subCalories(double calories) {
        currentConsumedCalories -= calories;
    }

    public Workout reccomendWorkout() {
        if (currentConsumedCalories > getTargetCalories()) {
            if (currentConsumedCalories - getTargetCalories() < 100) {
                return new LowWorkout();
            } else if (currentConsumedCalories - getTargetCalories() < 500) {
                return new MediumWorkout();
            } else {
                return new HighWorkout();
            }
        }

        //if no workout reccomended, return null
        return null;
    }
}
