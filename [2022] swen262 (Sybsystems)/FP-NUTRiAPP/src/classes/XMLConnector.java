package src.classes;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import src.interfaces.DBConnector;
import src.interfaces.Food;

public class XMLConnector implements DBConnector{

    private String fileName;
    private Hashtable<String, Integer> foodQuantity = new Hashtable<String, Integer>();
    private Hashtable<String, Food> foodsDB = new Hashtable<String, Food>();

    public XMLConnector(String file){
        this.fileName = file;
    }

    @Override
    public void saveData(Hashtable<String, Integer> foodQuantity, Hashtable<String, Food> foodsDB) throws IOException {
        // TODO Auto-generated method stub
        FileWriter f = new FileWriter("data/" + this.fileName, false);
        Set<String> keys = foodsDB.keySet();
        f.write( "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        f.write("<root>\n");
        for (String key : keys){
            Food curr = foodsDB.get(key);
            f.write("\t<ingredient>\n");
            f.write("\t\t<name>" + curr.getName() + "</name>\n");
            f.write("\t\t<calories>" + Double.toString(curr.calculateCalories()) +"</calories>\n");
            f.write("\t\t<fibers>" + Double.toString(curr.getFibers()) +"</fibers>\n");
            f.write("\t\t<fat>" + Double.toString(curr.getFats()) +"</fat>\n");
            f.write("\t\t<protein>" + Double.toString(curr.getProteins()) +"</protein>\n");
            f.write("\t\t<carbohydrates>" + Double.toString(curr.getCarbohydrates()) +"</carbohydrates>\n");
            f.write("\t\t<quantity>" + Integer.toString(foodQuantity.get(key)) +"</quantity>\n");
            f.write("\t</ingredient>\n");
        }
        f.write("</root>");
        f.close();
    }

    @Override
    public void readFile() throws FileNotFoundException {
        try{
            File file = new File("data/" + this.fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("ingredient");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    Double calories = Double.parseDouble( eElement.getElementsByTagName("calories").item(0).getTextContent());
                    Double fibers = Double.parseDouble(eElement.getElementsByTagName("fibers").item(0).getTextContent());
                    Double fat = Double.parseDouble(eElement.getElementsByTagName("fat").item(0).getTextContent());
                    Double protein = Double.parseDouble(eElement.getElementsByTagName("protein").item(0).getTextContent());
                    Double carbohydrates = Double.parseDouble(eElement.getElementsByTagName("carbohydrates").item(0).getTextContent());
                    int quantity = Integer.parseInt(eElement.getElementsByTagName("quantity").item(0).getTextContent());
                    Food ingredient = new Ingredients(name, calories, fibers, fat, protein, carbohydrates);
                    foodQuantity.put(name, quantity);
                    foodsDB.put(name, ingredient);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
