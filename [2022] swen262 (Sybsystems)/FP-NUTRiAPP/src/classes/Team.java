package src.classes;

import src.interfaces.Visitable;

import java.util.ArrayList;

public class Team implements Visitable {

    private final int id;
    private final String name;
    private ArrayList<Profile> members;

    public Team(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Profile> getMembers(){
        return members;
    }

    public void addMember(Profile profile){
        members.add(profile);
    }

    public String getName(){
        return name;
    }

    public void accept(LogWorkoutVisitor visitor){
        visitor.visit(this);
    }

    public void accept(ChallengesVisitor visitor){
        visitor.visit(this);
    }
}
