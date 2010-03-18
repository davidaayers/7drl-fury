package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.Direction;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.map.TileType;
import com.wwflgames.fury.util.Rand;

public abstract class AbstractDigger implements Digger {

    protected Feature maybeDigFeature(DungeonMap map, int startX, int endX, int startY, int endY) throws DigException {
        Feature newFeature = new Feature();
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                // see what's already there on the map
                // what are we drawing?
                TileType drawTile = TileType.FLOOR;
                if (y == startY || y == endY - 1 || x == startX || x == endX - 1) {
                    drawTile = TileType.WALL;
                }

                if (!map.inBounds(x, y)) {
                    throw new DigException();
                }

                Tile tile = map.getTileAt(x, y);
                TileType existing = tile.getType();

                boolean okToDraw = false;

                if (drawTile == TileType.WALL) {
                    // walls can be drawn on empty squares, or on top of existing walls
                    okToDraw = existing == TileType.EMPTY || existing == TileType.WALL;
                } else {
                    okToDraw = existing == TileType.EMPTY;
                }

                if (okToDraw) {
                    tile.setType(drawTile);
                    if ( drawTile == TileType.FLOOR ) {
                        newFeature.addFloorTile(tile);
                    } else if ( drawTile == TileType.WALL ) {
                        newFeature.addWallTile(tile);
                    }
                } else {
                    throw new DigException();
                }
            }
        }

        return newFeature;
    }

    protected void addJoinPoints(int howMany, Feature feature, DungeonMap map) {
        Tile[] wallTiles = feature.getWallTiles();
        int c = 0;
        while ( c < howMany ) {
            int idx = Rand.get().nextInt(wallTiles.length);
            Tile t = wallTiles[idx];
            if ( t.getType() != TileType.JOIN ) {
                Direction dir = findDirection(t,map);
                if ( dir != null ) {
                    JoinPoint jp = new JoinPoint(t.getX(),t.getY(),dir);
                    t.setType(TileType.JOIN);
                    feature.addJoinPoint(jp);
                    c ++;
                }
            }
        }
    }

    private Direction findDirection(Tile tile, DungeonMap map) {

        // look in all of the cardinal directions. As soon as we fine one with a BLANK tile,
        // that's the direction our Join Point faces
        for ( Direction cardinal : Direction.CARDINALS ) {
            int checkX = tile.getX() + cardinal.getDx();
            int checkY = tile.getY() + cardinal.getDy();
            if ( map.inBounds( checkX , checkY  ) ) {
                if ( map.getTileAt(checkX,checkY).getType() == TileType.EMPTY ) {
                    return cardinal;
                }
            }
        }
        // may happen on edge of map
        return null;
    }

}
