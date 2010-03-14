package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;

public interface Item {
    String name();

    ItemUsageResult usedBy(Mob mob);

    ItemUsageResult usedAgainst(Mob mob);
}
