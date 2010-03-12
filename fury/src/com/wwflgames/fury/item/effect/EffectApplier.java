package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.BattleEffectBag;
import com.wwflgames.fury.mob.Mob;

public interface EffectApplier {
    void apply(ItemEffect effect, Mob applyTo, BattleEffectBag bag);
}
