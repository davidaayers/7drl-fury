package com.wwflgames.fury.map;

public class FixedMapCreator implements MapCreator {
    @Override
    public Map createMap() {
        Map map = new Map(5,5);

        // add walls to the all around
        for ( int cnt = 0 ; cnt < 5 ; cnt ++ ) {
            map.getTileAt(cnt,0).setType(TileType.WALL);
            map.getTileAt(cnt,4).setType(TileType.WALL);
            map.getTileAt(0,cnt).setType(TileType.WALL);
            map.getTileAt(4,cnt).setType(TileType.WALL);
        }

        return map;
    }
}
