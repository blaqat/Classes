package src.classes;

/**
 * A class to represent a workout. There are three types
 * low, med, and high
 * @author Eliza Ostrowski
 */

public class Workout {
    private String name;
    private Double caloriesPerMinute;
    private String timeAndDate;

    public Workout(String name, Double caloriesPerMinute){
        this.name = name;
        this.caloriesPerMinute = caloriesPerMinute;
        timeAndDate = "Placeholder";
    }

    public String getName(){
        return this.name;
    }

    public Double getCaloriesPerMinute(){
        return this.caloriesPerMinute;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }

    public String getTimeAndDate(){
        return this.timeAndDate;
    }
}
