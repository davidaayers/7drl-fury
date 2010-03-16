package com.wwflgames.fury.item.neffect;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;

public interface NItemEffect {
    String getDesc();

    void applyEffect(Mob mob, ItemUsageResult result);
}
