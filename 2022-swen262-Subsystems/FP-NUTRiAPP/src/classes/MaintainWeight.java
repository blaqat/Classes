package src.classes;

import src.interfaces.WeightGoal;

/**
 * A class to maintain weight
 * @author Eliza Ostrowski
 */

public class MaintainWeight implements WeightGoal {
    public double calculateTargetCalories(double currWeight, double targetWeight) {
        return 2000;
    }
    public WeightGoal checkChangeGoal(double currWeight, double targetWeight) {
        if (currWeight - 5 > targetWeight) {
            return new LoseWeight();
        } else if (currWeight + 5 < targetWeight) {
            return new GainWeight();
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return "Maintain Weight";
    }
}