package com.wwflgames.fury.map;

public class Map {

    private int width;
    private int height;
    private TileType[][] tileTypes;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tileTypes = new TileType[width][height];
        initTilesToFloor();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean inBounds(int x, int y ) {
        return x >=0 && x < width && y >=0 && y < height;
    }

    private void initTilesToFloor() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileTypes[x][y] = TileType.FLOOR;
            }
        }
    }

    public TileType getTileFor(int x, int y ) {
        return tileTypes[x][y];
    }

    public void setTileAt(int x, int y , TileType tileType) {
        tileTypes[x][y] = tileType;
    }

}
