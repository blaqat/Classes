package src.classes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A class for history
 * @author Eliza Ostrowski
 */

public class History {
    private ArrayList<Double> weightHistory = new ArrayList<Double>(); //one weight recorded per day
    private ArrayList<Workout>[] workouts; //infinate workouts per day
    private ArrayList<Meal>[] meals;
    private ArrayList<Double> caloriehistory = new ArrayList<Double>();
    private int currDay = 0; //index
    private Date[] dates;

    public void endDay(double weight, double caloriesEaten) {
        weightHistory.add(weight);
        caloriehistory.add(caloriesEaten);
        dates[currDay] = new Date();
        currDay++;
    }

    public void doWorkout(String workoutType) throws Exception {
        Workout workout;
        //the user should not input these
        if (workoutType.equals("low")) {
            workout = new LowWorkout();
        } else if (workoutType.equals("med")) {
            workout = new MediumWorkout();
        } else if (workoutType.equals("high")) {
            workout = new HighWorkout();
        } else {
            throw new Exception("Invalid Workout Type. Check capitalization");
        }

        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);

        Date today = Calendar.getInstance().getTime();        

        String timeAndDate = df.format(today);

        workout.setTimeAndDate(timeAndDate);

        workouts[currDay].add(workout);
    }

    public void eatMeal(Meal meal) {
        meals[currDay].add(meal);
    }

    public String viewDay(int day) {
        String str = "History for " + day + ":\nWeight: " + weightHistory.get(day).toString() + "\n";
        str += "Calories: " + caloriehistory.get(day).toString() + "\n";
        str += "Workouts: " + workouts[day] + "\n";
        str += "Meals: " + meals[day] +"\n";
        return str;
    }

    public String viewDay(String dayStr) {
        return null;
    }

    public void deleteWorkout() {
        //Deletes the last workout
        workouts[currDay].remove(workouts[currDay].size() - 1);
    }

    public void deleteMeal() {
        //deletes last meal
        meals[currDay].remove(meals[currDay].size() - 1);
    }
}
