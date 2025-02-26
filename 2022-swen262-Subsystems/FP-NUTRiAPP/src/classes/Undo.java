package src.classes;

public class Undo {
    private String lastcommand;
    private double lastint; //for if you change the weight or eat something
    private History userHistory;
    private Goal user;
    private Profile profile;

    public Undo(History history, Goal user, Profile profile) {
        userHistory = history;
        this.user = user;
        this.profile = profile;
    }

    public void logcommand(String cmd) {
        this.lastcommand = cmd;
        this.lastint = -1;
    }

    public void logcommand(String cmd, double lastint) {
        this.lastcommand = cmd;
        this.lastint = lastint;
    }

    public void undo() {
        if (lastcommand.equals("w")) {
            userHistory.deleteWorkout();
            user.addCalories(lastint);
        } else if (lastcommand.equals("e")) {
            //userHistory.deleteMeal();
            user.subCalories(lastint);
        } else if (lastcommand.equals("ch")) {
            profile.setTargetWeight(lastint);
        }

        System.out.println("Command undone.");

        lastcommand = "";
        lastint = -1;
    }
}
