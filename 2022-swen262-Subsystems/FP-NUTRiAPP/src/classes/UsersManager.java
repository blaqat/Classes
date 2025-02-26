package src.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import src.interfaces.User;

//Username,Password,Name,Height,Weight,TargetWeight,Birthdate

public class UsersManager {

    static HashMap<String, RegisteredUser> users;
    //ArrayList<User> users;

    public UsersManager(){
        users = new HashMap<String, RegisteredUser>();
    }
    
    public static void loadUsers() throws ParseException, FileNotFoundException{
        users = new HashMap<String, RegisteredUser>();
        File file = new File("data/users.csv");
        Scanner csvReader = new Scanner(file);
        while(csvReader.hasNextLine()){
            String[] data = csvReader.nextLine().split(",");
            java.util.Date birthdate = new SimpleDateFormat("MM/dd/yyyy").parse(data[6]);
            Profile userProf = new Profile(data[2], Double.parseDouble(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]), birthdate);
            newUser(new RegisteredUser(data[0], data[1], userProf));
        }
    }

    public static void saveUsers() throws IOException{
        FileWriter fileWriter = new FileWriter("data/users.csv", false);

        for(RegisteredUser user : users.values()){
            Profile userProf = user.getProfile();

            String[] data = new String[7];
            data[0] = user.getUsername();
            data[1] = user.getPassword();
            data[2] = userProf.getName();
            data[3] = "" + userProf.getHeight();
            data[4] = "" + userProf.getWeight();
            data[5] = "" + userProf.getTargetWeight();
            data[6] = userProf.getBirthdate();
            
            String line = String.join(",", data);
            fileWriter.write(line);
            fileWriter.write("\n");
        }

        fileWriter.flush();
        fileWriter.close(); 
    }

    public static void newUser(RegisteredUser newUser){
        users.put(newUser.getUsername(), newUser);
    }

    public static RegisteredUser getUser(String username){
        return users.get(username);
    }
    
}
