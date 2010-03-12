package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.BattleEffectList;
import com.wwflgames.fury.mob.Mob;

public interface Item {
    String name();

    BattleEffectList usedBy(Mob mob);

    BattleEffectList usedAgainst(Mob mob);
}
