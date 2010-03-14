package com.wwflgames.fury.main;

import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.player.Player;

// global game state object
public class AppStateImpl implements AppState {

    private Player player;
    private Map map;
    private boolean playerInitiative;

    @Override
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public boolean doesPlayerHaveInitiative() {
        return playerInitiative;
    }

    @Override
    public void setPlayerInitiative(boolean flag) {
        playerInitiative = flag;
    }


    public void setMap(Map map) {
        this.map = map;
    }
}
