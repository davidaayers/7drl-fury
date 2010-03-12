package com.wwflgames.fury.map;

import com.wwflgames.fury.mob.Mob;

public class Map {

    private int width;
    private int height;
    private Tile[][] tiles;

    public Map(int width, int height) {
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

    private void initTilesToFloor() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // default tiles to floor
                tiles[x][y] = new Tile(TileType.FLOOR, x, y);
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
    }
}
