package src.interfaces;

/**
 * A Interface for foods
 * @author Daniel Chung
 */

public interface Food {
    public double calculateCalories();
    public void display();
    public String getName();
    public double getFibers();
    public double getFats();
    public double getCarbohydrates();
    public double getProteins();
}
