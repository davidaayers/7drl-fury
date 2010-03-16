package com.wwflgames.fury.map;

public enum DifficultyLevel {
    EASY(5,20,25),
    NORMAL(10,20,30),
    HARD(15,30,40)
    ;

    private int numLevelsDeep;
    private int levelSizeMin;
    private int levelSizeMax;

    DifficultyLevel(int numLevelsDeep, int levelSizeMin, int levelSizeMax) {
        this.numLevelsDeep = numLevelsDeep;
        this.levelSizeMin = levelSizeMin;
        this.levelSizeMax = levelSizeMax;
    }

    public int getNumLevelsDeep() {
        return numLevelsDeep;
    }

    public int getLevelSizeMin() {
        return levelSizeMin;
    }

    public int getLevelSizeMax() {
        return levelSizeMax;
    }
}
