package com.wwflgames.fury.main;

import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.player.Player;

// global game state object
public class AppStateImpl implements AppState {

    private Player player;
    private Map map;

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

    public void setMap(Map map) {
        this.map = map;
    }
}
