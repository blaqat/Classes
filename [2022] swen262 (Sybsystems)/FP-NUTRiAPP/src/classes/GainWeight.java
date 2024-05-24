package src.classes;

import src.interfaces.WeightGoal;

/**
 * A class to gain weight
 * @author Eliza Ostrowski
 */

public class GainWeight implements WeightGoal {
    public double calculateTargetCalories(double currWeight, double targetWeight) {
        //If you are 50 pounds away, you should eat 2500 calories a day (gain approx 1 pound a week)
        //Else eat 3000 (2 pounds a week)
        if (currWeight + 50 > targetWeight) {
            return 2500;
        } else {
            return 3000;
        }
    }
    public WeightGoal checkChangeGoal(double currWeight, double targetWeight) {
        if (currWeight - 5 > targetWeight) {
            return new LoseWeight();
        } else if (currWeight + 5 < targetWeight) {
            return this;
        } else {
            return new MaintainWeight();
        }
    }

    @Override
    public String toString() {
        return "Gain Weight";
    }
}