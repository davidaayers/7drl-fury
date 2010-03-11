package com.wwflgames.fury.map;

public class FixedMapCreator implements MapCreator {
    @Override
    public Map createMap() {
        Map map = new Map(5,5);

        // add walls to the all around
        for ( int cnt = 0 ; cnt < 5 ; cnt ++ ) {
            map.setTileAt(cnt,0, TileType.WALL);
            map.setTileAt(cnt,4, TileType.WALL);
            map.setTileAt(0,cnt, TileType.WALL);
            map.setTileAt(4,cnt, TileType.WALL);
        }

        return map;
    }
}
