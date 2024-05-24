package src.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

import src.interfaces.DBConnector;
import src.interfaces.Food;


public class JSONConnector implements DBConnector{
    
    private String fileName;
    private Hashtable<String, Integer> foodQuantity = new Hashtable<String, Integer>();
    private Hashtable<String, Food> foodsDB = new Hashtable<String, Food>();

    public JSONConnector(String file){
        this.fileName = file;
    }

    @Override
    public void saveData(Hashtable<String, Integer> foodQuantity, Hashtable<String, Food> foodsDB) throws IOException {
        FileWriter f = new FileWriter("data/" + this.fileName, false);
        int counter = 0;
        Set<String> keys = foodsDB.keySet();
        f.write("[\n");
        for (String key: keys){
            counter += 1;
            Food curr = foodsDB.get(key);
            f.write("\t{\n");
            f.write("\t\t\"name\": \"" + curr.getName() + "\",\n");
            f.write("\t\t\"calories\": \"" + Double.toString(curr.calculateCalories()) + "\",\n");
            f.write("\t\t\"fibers\": \"" + Double.toString(curr.getFibers()) + "\",\n");
            f.write("\t\t\"fat\": \"" + Double.toString(curr.getFats()) + "\",\n");
            f.write("\t\t\"protein\": \"" + Double.toString(curr.getProteins()) + "\",\n");
            f.write("\t\t\"carbohydrates\": \"" + Double.toString(curr.getCarbohydrates()) + "\",\n");
            f.write("\t\t\"quantity\": \"" + Integer.toString(foodQuantity.get(key)) + "\"\n");
            if (counter < keys.size()){
                f.write("\t},\n");
            }
            else{
                f.write("\t}\n");
            }
        }
        f.write("]");
        f.close();
    }

    @Override
    public void readFile() throws FileNotFoundException {
        File file = new File("data/" + this.fileName);
        Scanner jsonReader = new Scanner(file);
        Boolean ingredientFlag = false;
        Boolean counter = false;
        ArrayList<String> values = new ArrayList<String>();
        while(jsonReader.hasNext()){
            String curr = jsonReader.next();
            if (curr.equals("{") && ingredientFlag == false){
                ingredientFlag = true;
            }
            else if (curr.startsWith("\"") && counter == false){
                counter = true;
            }
            else if (curr.startsWith("\"") && counter){
                values.add(curr.replace("\"", "").replace(",", ""));
                counter = false;
            }
            else if (curr.equals("}") || curr.equals("},")){
                ingredientFlag = false;
                foodsDB.put(values.get(0), this.createFood(values));
                foodQuantity.put(values.get(0), Integer.parseInt(values.get(6)));
                values = new ArrayList<String>();
            }
        }
    }

    public Food createFood(ArrayList<String> values){
        String name = values.get(0);
        Double calories = Double.parseDouble(values.get(1));
        Double fibers = Double.parseDouble(values.get(2));
        Double fat = Double.parseDouble(values.get(3));
        Double protein = Double.parseDouble(values.get(4));
        Double carbohydrates = Double.parseDouble(values.get(5));
        return new Ingredients(name, calories, fibers, fat, protein, carbohydrates);
    }

    @Override
    public Hashtable<String, Integer> getQuantity() {
        return foodQuantity;
    }

    @Override
    public Hashtable<String, Food> getDatabase() {
        return foodsDB;
    }
    
}
