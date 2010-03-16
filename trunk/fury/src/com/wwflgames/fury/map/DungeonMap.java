package com.wwflgames.fury.map;

import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;

import java.util.ArrayList;
import java.util.List;

public class DungeonMap {

    private int width;
    private int height;
    private Tile[][] tiles;
    private List<Monster> monsterList = new ArrayList<Monster>();

    public DungeonMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        initTilesToFloor();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isWalkable(int x, int y) {
        Tile t = getTileAt(x, y);
        return t.isWalkable();
    }

    private void initTilesToFloor() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // default tiles to floor
                tiles[x][y] = new Tile(TileType.EMPTY, x, y);
            }
        }
    }

    public Tile getTileAt(int x, int y) {
        return tiles[x][y];
    }

    public void setTileAt(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }

    public void addMob(Mob mob, int x, int y) {
        Tile tile = getTileAt(x, y);
        tile.setMob(mob);
        mob.setCurrentMapTile(tile);
        if (mob instanceof Monster) {
            monsterList.add((Monster) mob);
        }
    }

    public void removeMob(Mob mob) {
        Tile tile = mob.getCurrentMapTile();
        tile.setMob(null);
        mob.setCurrentMapTile(null);
        if (mob instanceof Monster) {
            monsterList.remove(mob);
        }
    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }
}
