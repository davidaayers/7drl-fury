package com.wwflgames.fury.map;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private int currentLevel = 0;
    private List<DungeonMap> levels = new ArrayList<DungeonMap>();

    public void addLevel(DungeonMap level) {
        levels.add(level);
    }

    public DungeonMap currentLevelMap() {
        return levels.get(currentLevel);
    }

    public int currentLevel() {
        return currentLevel + 1;
    }

}
