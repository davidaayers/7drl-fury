package com.wwflgames.fury.map;

import com.wwflgames.fury.mob.Mob;

public class Tile {

    private TileType type;
    private int x;
    private int y;
    // the mob that's occupying this tile
    private Mob mob;

    public Tile(TileType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Mob getMob() {
        return mob;
    }

    // package private so it can only be called from DungeonMap
    void setMob(Mob mob) {
        this.mob = mob;
    }

    public boolean isWalkable() {
        return type.isWalkable();
    }

    @Override
    public String toString() {
        return "Tile{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", mob=" + mob +
                '}';
    }
}
