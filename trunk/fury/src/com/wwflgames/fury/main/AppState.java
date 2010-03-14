package com.wwflgames.fury.main;

import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.player.Player;

public interface AppState {
    Player getPlayer();

    Map getMap();

    boolean doesPlayerHaveInitiative();

    void setPlayerInitiative(boolean flag);
}
