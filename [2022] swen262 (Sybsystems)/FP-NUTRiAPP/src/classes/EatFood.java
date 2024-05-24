package src.classes;

import src.interfaces.Action;
import src.interfaces.Food;

public class EatFood implements Action{
    
    Profile user; 
    Food food;

    public EatFood(Profile user, Food food){
        this.user = user;
        this.food = food;
    }


    public void execute(){
        
    }
}
