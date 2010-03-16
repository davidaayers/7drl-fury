package com.wwflgames.fury.main;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.player.Player;

// global game state object
public class AppStateImpl implements AppState {

    private Player player;
    private DungeonMap dungeonMap;
    private boolean playerInitiative;

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public DungeonMap getMap() {
        return dungeonMap;
    }

    @Override
    public boolean doesPlayerHaveInitiative() {
        return playerInitiative;
    }

    @Override
    public void setPlayerInitiative(boolean flag) {
        playerInitiative = flag;
    }


    @Override
    public void setMap(DungeonMap dungeonMap) {
        this.dungeonMap = dungeonMap;
    }
}
