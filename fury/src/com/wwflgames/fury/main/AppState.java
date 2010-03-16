package com.wwflgames.fury.main;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.player.Player;

public interface AppState {
    Player getPlayer();

    DungeonMap getMap();

    boolean doesPlayerHaveInitiative();

    void setPlayerInitiative(boolean flag);

    void setPlayer(Player player);

    void setMap(DungeonMap dungeonMap);
}
