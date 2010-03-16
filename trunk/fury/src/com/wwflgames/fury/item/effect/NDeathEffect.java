package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;

public class NDeathEffect implements NItemEffect {
    @Override
    public String getDesc() {
        return "Death";
    }

    @Override
    public void applyEffect(Mob mob, ItemUsageResult result) {
        // they are already dead
    }
}
