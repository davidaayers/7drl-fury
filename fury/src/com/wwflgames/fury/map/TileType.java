package com.wwflgames.fury.map;

public enum TileType {
    FLOOR(".", true),
    WALL("#", false);

    String ascii;
    boolean walkable;

    TileType(String ascii, boolean walkable) {
        this.ascii = ascii;
        this.walkable = walkable;
    }

    public String getAscii() {
        return ascii;
    }

    public boolean isWalkable() {
        return walkable;
    }
}
