package com.wwflgames.fury.map;

import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Rand;

public class DungeonMapCreatorImpl implements DungeonMapCreator {
    @Override
    public DungeonMap createMap(DifficultyLevel difficulty, int level) {

        // create a map of random size, based on the difficulty
        int min = difficulty.getLevelSizeMin();
        int max = difficulty.getLevelSizeMax();
        int height = Rand.between(min, max);
        int width = Rand.between(min, max);

        DungeonMap map = new DungeonMap(width, height);

        Log.debug("width = " + width);
        Log.debug("height = " + height);

        // pick a location in the middle-ish of the map,
        int middleX = (width / 2) + Rand.between(-5, 5);
        int middleY = (height / 2) + Rand.between(-5, 5);

        Log.debug("middleX = " + middleX);
        Log.debug("middleY = " + middleY);

        drawRandomRoomAt(middleX, middleY, map);


        return map;
    }

    private void drawRandomRoomAt(int middleX, int middleY, DungeonMap map) {
        int width = Rand.between(3, 8);
        int height = Rand.between(3, 8);

        for (int y = middleY; y < middleY + height; y++) {
            for (int x = middleX; x < middleX + width; x++) {
                if ( map.inBounds(x,y)) {
                    Tile tile = map.getTileAt(x, y);
                    tile.setType(TileType.FLOOR);
                }
            }
        }
    }
}
