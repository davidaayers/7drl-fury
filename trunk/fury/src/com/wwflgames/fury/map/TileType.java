package com.wwflgames.fury.map;

public enum TileType {
    FLOOR("."),
    WALL("#");

    String ascii;

    TileType(String ascii) {
        this.ascii = ascii;
    }

    public String getAscii() {
        return ascii;
    }
}
