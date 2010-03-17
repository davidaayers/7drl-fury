package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.map.TileType;
import com.wwflgames.fury.util.Rand;

import static com.wwflgames.fury.map.Direction.E;
import static com.wwflgames.fury.map.Direction.N;

public class RoomDigger implements Digger {

    private int width;
    private int height;

    public RoomDigger(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean canDig(DungeonMap map, JoinPoint joinPoint) {
        DungeonMap mapClone = map.duplicate();
        return doDigging(mapClone, joinPoint);
    }

    @Override
    public JoinPoint[] dig(DungeonMap map, JoinPoint joinPoint) {
        doDigging(map, joinPoint);

        // now, add new join points

        return null;
    }

    private boolean doDigging(DungeonMap map, JoinPoint joinPoint) {

        int startX = 0;
        int endX = 0;
        int startY = 0;
        int endY = 0;

        switch (joinPoint.getDirection()) {
            case N:
            case S:
                int halfWidth = width / 2;
                startX = joinPoint.getX() - Rand.between(1, width - halfWidth + 1);
                endX = startX + width;

                if (joinPoint.getDirection() == N) {
                    startY = joinPoint.getY() - height + 1;
                    endY = joinPoint.getY() + 1;
                } else {
                    startY = joinPoint.getY();
                    endY = joinPoint.getY() + height;
                }

                break;

            case E:
            case W:
                int halfHeight = height / 2;
                startY = joinPoint.getY() - Rand.between(1, height - halfHeight + 1);
                endY = startY + height;

                if (joinPoint.getDirection() == E) {
                    startX = joinPoint.getX();
                    endX = joinPoint.getX() + width;
                } else {
                    startX = joinPoint.getX() - width + 1;
                    endX = joinPoint.getX() + 1;
                }

                break;
        }

        boolean didDig = maybeDigRoom(map, startX, endX, startY, endY);

        if (!didDig) {
            return false;
        }

        // finally, draw the join point
        map.getTileAt(joinPoint.getX(), joinPoint.getY()).setType(TileType.JOIN);
        return true;


//        if (joinPoint.getDirection() == N) {
//            // pick a point in the middle of "width"
//            int halfWidth = width / 2;
//            int startX = joinPoint.getX() - Rand.between(1, width - halfWidth + 1);
//            int endX = startX + width;
//            int startY = joinPoint.getY() - height + 1;
//            int endY = joinPoint.getY() + 1;
//
//            boolean didDig = maybeDigRoom(map, startX, endX, startY, endY);
//
//            if (!didDig) {
//                return false;
//            }
//
//            // finally, draw the join point
//            map.getTileAt(joinPoint.getX(), joinPoint.getY()).setType(TileType.JOIN);
//        }
    }

    private boolean maybeDigRoom(DungeonMap map, int startX, int endX, int startY, int endY) {
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                // see what's already there on the map
                // what are we drawing?
                TileType drawTile = TileType.FLOOR;
                if (y == startY || y == endY - 1 || x == startX || x == endX - 1) {
                    drawTile = TileType.WALL;
                }

                if (!map.inBounds(x, y)) {
                    return false;
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
                    map.getTileAt(x, y).setType(drawTile);
                } else {
                    return false;
                }
            }
        }
        return true;
    }


}
