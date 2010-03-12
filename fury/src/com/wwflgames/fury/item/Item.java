package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.BattleEffectBag;
import com.wwflgames.fury.mob.Mob;

public interface Item {
    String name();

    BattleEffectBag usedBy(Mob mob);

    BattleEffectBag usedAgainst(Mob mob);
}
