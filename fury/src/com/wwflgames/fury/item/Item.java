package com.wwflgames.fury.item;

import com.wwflgames.fury.mob.Mob;

public interface Item {
    String name();

    void usedBy(Mob mob);

    void usedAgainst(Mob mob);
}
