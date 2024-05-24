package com.webcheckers.model;

import java.util.HashMap;
import java.util.ArrayList;

public class Achievement {
    private static HashMap<String, String> achievementList = new HashMap<String, String> () {
        {
            put("Tournament Winner", "👑");
            put("Beat Easy Bot", "🎲");
            put("Beat Medium Bot", "🎰");
            put("Beat Hard Bot", "🦾");
            put("Zero Deaths", "💯");
            put("Hot Streak", "🔥");
        }
    };

    public static String getAchievement(String name) {
        return achievementList.get(name);
    }

    private ArrayList<String> achievements;

    public Achievement() {
        this.achievements = new ArrayList<String> ();
    }

    public Achievement(String name) {
        this();
        this.add(name);
    }

    public void add(String name) {
        String ach = Achievement.getAchievement(name);
        if (!this.achievements.contains(ach))
            this.achievements.add(ach);
    }

    public void toggle(String name, boolean condition) {
        String ach = Achievement.getAchievement(name);
        if (!condition && this.achievements.contains(ach)) {
            this.achievements.remove(ach);
        } else if (condition) {
            add(name);
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i<this.achievements.size(); i++) {
            s += this.achievements.get(i);
            if (i + 1<this.achievements.size())
                s += " ";
        }

        return s;
    }
}