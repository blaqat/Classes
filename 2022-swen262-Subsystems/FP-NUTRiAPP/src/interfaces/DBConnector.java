package src.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

public interface DBConnector {
    public void saveData(Hashtable<String, Integer> foodQuantity, Hashtable<String, Food> foodsDB) throws IOException;
    public void readFile() throws FileNotFoundException, ParserConfigurationException;
    public Hashtable<String, Integer> getQuantity();
    public Hashtable<String, Food> getDatabase();
}
