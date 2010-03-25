package com.wwflgames.fury.map;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private DungeonMap currentLevel;
    private List<DungeonMap> levels = new ArrayList<DungeonMap>();

    public Dungeon(List<DungeonMap> levels) {
        this.levels = levels;
        currentLevel = levels.get(0);
    }

    public DungeonMap currentLevelMap() {
        return currentLevel;
    }

    public void takeStairsFrom(DungeonMap map) {
        Stairs stairs = map.getStairs();
        currentLevel = stairs.mapAtOtherEndFrom(map);
    }




}
