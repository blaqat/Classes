package src.interfaces;

import src.classes.ChallengesVisitor;
import src.classes.LogWorkoutVisitor;

public interface Visitable {

    void accept(LogWorkoutVisitor v);

    void accept(ChallengesVisitor v);
}
