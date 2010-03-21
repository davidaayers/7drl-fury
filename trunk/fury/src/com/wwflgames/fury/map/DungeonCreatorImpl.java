package com.wwflgames.fury.map;

public class DungeonCreatorImpl implements DungeonCreator {

    private DungeonMapCreator mapCreator;

    public DungeonCreatorImpl(DungeonMapCreator mapCreator) {
        this.mapCreator = mapCreator;
    }

    @Override
    public Dungeon createDungeon(DifficultyLevel difficulty) {

        int floorsToCreate = difficulty.getNumLevelsDeep();

        Dungeon dungeon = new Dungeon();

        for (int idx = 0; idx < floorsToCreate; idx++) {
            dungeon.addLevel(mapCreator.createMap(difficulty, idx));
        }

        return dungeon;
    }
}
