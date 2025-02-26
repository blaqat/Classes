package src.classes;

import src.interfaces.WeightGoal;

/**
 * A class to lose weight
 * @author Eliza Ostrowski
 */

public class LoseWeight implements WeightGoal {
    public double calculateTargetCalories(double currWeight, double targetWeight) {
        //If you are 50 pounds away, you should eat 1500 calories a day (lose approx 1 pound a week)
        //Else eat 1000 (2 pounds a week. Should not eat less its unhealthy)
        if (currWeight - 50 > targetWeight) {
            return 1500;
        } else {
            return 1000;
        }
    }
    public WeightGoal checkChangeGoal(double currWeight, double targetWeight) {
        if (currWeight - 5 > targetWeight) {
            //if no change
            return this;
        } else if (currWeight + 5 < targetWeight) {
            return new GainWeight();
        } else {
            return new MaintainWeight();
        }
    }

    @Override
    public String toString() {
        return "Lose Weight";
    }
}
