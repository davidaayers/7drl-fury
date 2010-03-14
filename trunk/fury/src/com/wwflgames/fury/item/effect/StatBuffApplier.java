package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffect;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

public class StatBuffApplier extends AbstractBuffApplier {

    public StatBuffApplier() {
        super(BuffType.STAT);
    }

    @Override
    public void apply(com.wwflgames.fury.item.effect.ItemEffect effect, Mob applyTo, ItemUsageResult bag) {
        StatBuff buff = (StatBuff) effect;
        applyTo.modifyBattleStatValue(buff.getStat(), buff.getAmount());

        String armorDesc = "{0} armor is increased by {2}";
        bag.add(new ItemEffect(armorDesc, Stat.ARMOR, buff.getAmount()));
    }
}
