package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Log;

public class PlayerController {

    private int offsetX;
    private int offsetY;
    private int eastEdgeX = 23;
    private int westEdgeX = 2;
    private int northEdgeY = 2;
    private int southEdgeY = 23;
    private Player player;
    private DungeonMap map;

    public PlayerController(Player player, DungeonMap map) {
        this.player = player;
        this.map = map;
        setInitialOffsets();
    }

    private void setInitialOffsets() {
        // we want to center the player on the screen, so set the offsets
        // appropriately
        Integer playerX = player.getMapX();
        Integer playerY = player.getMapY();
        offsetX = playerX - 12;
        offsetY = playerY - 9;
        eastEdgeX = playerX + 11;
        westEdgeX = playerX - 10;
        southEdgeY = playerY + 6;
        northEdgeY = playerY - 7;
        printStuff();
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public Player getPlayer() {
        return player;
    }

    public void movePlayerTo(int x, int y) {
        Log.debug("Moving to " + x + "," + y);
        int oldX = player.getMapX();
        int oldY = player.getMapY();

        map.removeMob(player);
        map.addMob(player, x, y);

        int dx = x - oldX;
        int dy = y - oldY;

        // handle all of the offset cases
        // move EAST
        if (dx == 1) {
            if (x > eastEdgeX) {
                offsetX++;
                eastEdgeX++;
                westEdgeX++;
            }
        }
        if (dx == -1) {
            if (x < westEdgeX) {
                offsetX--;
                westEdgeX--;
                eastEdgeX--;
            }
        }
        if (dy == 1) {
            if (y > southEdgeY) {
                offsetY++;
                southEdgeY++;
                northEdgeY++;
            }
        }
        if (dy == -1) {
            if (y < northEdgeY) {
                offsetY--;
                southEdgeY--;
                northEdgeY--;
            }
        }

        printStuff();
    }

    private void printStuff() {
        Log.debug("offsetX=" + offsetX);
        Log.debug("offsetY=" + offsetY);
        Log.debug("westEdgeX=" + westEdgeX);
        Log.debug("eastEdgeX=" + eastEdgeX);
        Log.debug("northEdgeY=" + northEdgeY);
        Log.debug("southEdgeY=" + southEdgeY);
    }


}
