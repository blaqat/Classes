public class Food
{
    private double price, fat, carbohydrates, fiber;
    private String name;
    public Food(){
        price = 0;
        fat = 0;
        carbohydrates = 0;
        fiber = 0;
    }

    public Food(String nam){
        name = nam;
    }
    
    public Food(String nam, double pric, double fats, double carbs, double fibs){
        name = nam;
        price = pric;
        fat = fats;
        carbohydrates = carbs;
        fiber = fibs;        
    }

    public double getPrice(){
        return price;
    }

    public double getFat(){
        return fat;
    }

    public double getCarbs(){
        return carbohydrates;
    }

    public double getFibers(){
        return fiber;
    }
    
    public String toString(){
        return "Every " + name + " has " + fat + "g of fat, " + carbohydrates + "g of carbs, and " + fiber +"g of fiber.";
    }
}
