package src.interfaces;
/**
 * A class to represent a weight goal. Interface that can be either
 * lose weight, maintain weight, and gain weight. 
 * @author Eliza Ostrowski
 */

public interface WeightGoal {
    public double calculateTargetCalories(double currWeight, double targetWeight);
    public WeightGoal checkChangeGoal(double currWeight, double targetWeight);
    public String toString();
}
