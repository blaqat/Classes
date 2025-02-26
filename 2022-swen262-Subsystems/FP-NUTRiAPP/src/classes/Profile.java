package src.classes;

import src.interfaces.Visitable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * A class to represent a user
 * @author Eliza Ostrowski, Daniel Chung
 */

public class Profile implements Visitable {
    private String name;
    private double height; //feet.inches
    private double weight; //lbs
    private Date birthDate; //month/day/year
    private Double targetWeight;
    private History history;

    private boolean teamStatus = false; // default
    private int teamID = 0; //0 if not user is not in a team
    private LinkedList<Notifications> notifications = new LinkedList<Notifications>();

    public Profile(String name, double height, double weight, Double targetWeight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.setBirthDate();
        history = new History();
    } 

    public Profile(String name, double height, double weight, Double targetWeight,Date birthDate){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.birthDate = birthDate;
    }

    public String getBirthdate(){
        return new SimpleDateFormat("MM/dd/yyyy").format(this.birthDate) ;
    }
    public String getName(){
        return this.name;
    }

    public double getHeight(){
        return this.height;
    }

    public double getWeight(){
        return this.weight;
    }

    public double getTargetWeight(){
        return this.targetWeight;
    }

    public void setTargetWeight(double newWeight) {
        this.targetWeight = newWeight;
    }

    public History getHistory() {
        return history;
    }

    public void printProfile(){
        System.out.println("Name: " + this.name + " height: " + Double.toString(this.height) + " weight: " + Double.toString(this.weight) + " birthdate: " + this.birthDate.toString());
    }

    public void setBirthDate(){
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the month number you were born: ");
        String month = userInput.nextLine();
        System.out.println("Enter the day you were born: ");
        String day = userInput.nextLine();
        System.out.println("Enter the year you were born: ");
        String year = userInput.nextLine();
        Date birthDate = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day)).getTime();
        this.birthDate = birthDate;
    }

    public int getAge(){
        LocalDate currentDate = LocalDate.now();
        return Period.between(this.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), currentDate).getYears();
    }

    public void saveProfile() throws IOException{
        Format f = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = f.format(this.birthDate);
        Writer fileWriter = new FileWriter("data/profile.csv", false);
        fileWriter.write(this.name);
        fileWriter.write(",");
        fileWriter.write(Double.toString(this.height));
        fileWriter.write(",");
        fileWriter.write(Double.toString(this.weight));
        fileWriter.write(",");
        fileWriter.write(Double.toString(this.targetWeight));
        fileWriter.write(",");
        fileWriter.write(strDate);
        fileWriter.flush();
        fileWriter.close();
        System.out.println("File has been saved");
    }

    public boolean getTeamStatus(){
        return teamStatus;
    }

    public int getTeamID(){
        return teamID;
    }

    public void manageTeamStatus(boolean status, int id){
        teamStatus = status;
        teamID = (teamStatus) ? id : 0;
    }

    public void addNotification(int type, String message){
        notifications.add(new Notifications(type, message));
    }

    public Notifications readNotification () {
        Notifications n = notifications.getFirst();
        notifications.removeFirst();
        return n;
    }

    public int getNotificationCount(){
        return notifications.size();
    }

    public String sendInvite(Profile n){
        if (teamStatus) {
            n.addNotification(1, name + " has invited you to Team " + teamID);
            return "Invite has been sent.";
        }

        else
            return "You must be in a team before you can send any invites.";
    }

    public void accept(LogWorkoutVisitor visitor){
        visitor.visit(this);
    }

    public void accept(ChallengesVisitor visitor){
        visitor.visit(this);
    }
}
