package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.DungeonMap;

public interface Digger {
    boolean canDig(DungeonMap map, JoinPoint point);

    JoinPoint[] dig(DungeonMap map, JoinPoint point);
}
