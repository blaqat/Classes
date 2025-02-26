package src.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Teams {

    private ArrayList<Team> teams;

    public Teams() throws IOException {
        teams = new ArrayList<Team>();
        loadTeams();
    }

    public ArrayList<Team> getTeams(){
        return teams;
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public void loadTeams() throws IOException {
        File teamFile = new File("data/teams.csv");
        FileInputStream fis = new FileInputStream("data/teams.csv");
        Scanner reader = new Scanner(teamFile);
        while(reader.hasNextLine()){
            String line = reader.nextLine();
            String[] line_data = line.split(",");
            System.out.println(line_data[0]);
            System.out.println(line_data[1]);
            Team team = new Team(Integer.parseInt(line_data[0]), line_data[1]);
            teams.add(team);
        }
        reader.close();
    }

    public void saveTeams() throws IOException {
        FileWriter writer = new FileWriter("data/teams.csv", false);
        for(Team team: teams){
            writer.write(team.getId() + "," + team.getName() + "\n");
        }
        writer.close();
    }


}
