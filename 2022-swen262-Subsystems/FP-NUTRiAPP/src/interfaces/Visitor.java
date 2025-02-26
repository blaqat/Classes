package src.interfaces;

import src.classes.Profile;
import src.classes.Team;

public interface Visitor {

    void visit(Profile p);

    void visit(Team t);

}
