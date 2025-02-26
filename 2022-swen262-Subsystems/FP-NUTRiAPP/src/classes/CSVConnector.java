package src.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

import src.interfaces.DBConnector;
import src.interfaces.Food;

public class CSVConnector implements DBConnector{

    private String fileName; 
    private Hashtable<String, Integer> foodQuantity = new Hashtable<String, Integer>();
    private Hashtable<String, Food> foodsDB = new Hashtable<String, Food>();

    public CSVConnector(String file){
        this.fileName = file;
    }
    
    @Override
    public void saveData(Hashtable<String, Integer> foodQuantity, Hashtable<String, Food> foodsDB) throws IOException {
        FileWriter f = new FileWriter("data/" + this.fileName, false);
        f.write("0");
        f.write("\n");
        for (String name : foodsDB.keySet()){
            Ingredients ingredient = (Ingredients) foodsDB.get(name);
            f.write("\"" + ingredient.getName() + "\"");
            f.write(",");
            f.write(Double.toString(ingredient.calculateCalories()));
            f.write(",");
            f.write(Double.toString(ingredient.getFibers()));
            f.write(",");
            f.write(Double.toString(ingredient.getFats()));
            f.write(",");
            f.write(Double.toString(ingredient.getProteins()));
            f.write(",");
            f.write(Double.toString(ingredient.getCarbohydrates()));
            f.write(",");
            f.write(Integer.toString(foodQuantity.get(name)));
            f.write("\n");
        }
        f.close();
    }

    @Override
    public void readFile() throws FileNotFoundException {
        // TODO Auto-generated method stub
        File file = new File("data/" + this.fileName);
        Scanner csvReader = new Scanner(file);
        if (csvReader.hasNextLine()){
            String headers = csvReader.nextLine();
            if (headers.equals("0")){
                while(csvReader.hasNextLine()){
                    String line = csvReader.nextLine();
                    String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    String name = tokens[0].replace("\"", "");
                    Double calories = Double.parseDouble(tokens[1]);
                    Double fibers = Double.parseDouble(tokens[2]);
                    Double fat = Double.parseDouble(tokens[3]);
                    Double protein = Double.parseDouble(tokens[4]);
                    Double carbohydrates = Double.parseDouble(tokens[5]);
                    Integer offoodQuantity = Integer.parseInt(tokens[6]);
                    Food food = new Ingredients(name, calories, fibers, fat, protein, carbohydrates);
                    foodQuantity.put(name, offoodQuantity);
                    foodsDB.put(name, food);
                }
            }
            else{
                while(csvReader.hasNextLine()){
                    String line = csvReader.nextLine();
                    String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    String name = tokens[1].replace("\"", "");;
                    Double protein = Double.parseDouble(tokens[4].equals("") ? "0.00" : tokens[4]);
                    Double fibers = Double.parseDouble(tokens[8].equals("") ? "0.00" : tokens[8]);
                    Double fat = Double.parseDouble(tokens[44].equals("") ? "0.00" : tokens[44]);
                    Double carbohydrates = Double.parseDouble(tokens[7].equals("") ? "0.00" : tokens[7]);
                    Double calories = (protein * 4) + (carbohydrates * 4) + (fat * 4);
                    Food ingredients = new Ingredients(name, calories, fibers, fat, protein, carbohydrates);
                    foodQuantity.put(name, 1);
                    foodsDB.put(name, ingredients);
                }
            }
        }
    }

    @Override
    public Hashtable<String, Integer> getQuantity() {
        return this.foodQuantity;
    }

    @Override
    public Hashtable<String, Food> getDatabase() {
        return this.foodsDB;
    }
    
}
