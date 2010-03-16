package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;

public class StatBuffApplier extends AbstractBuffApplier {

    public StatBuffApplier() {
        super(BuffType.STAT);
    }

    @Override
    public void apply(ItemEffect effect, Mob mob, ItemUsageResult result) {
        StatBuff buff = (StatBuff) effect;
        mob.modifyBattleStatValue(buff.getStat(), buff.getAmount());

        String desc = "{0} " + buff.getStat().getDesc() + " is increased by {2}";
        result.add(new ItemEffectResult(desc, buff.getStat(), buff.getAmount(), mob, effect));
    }
}
