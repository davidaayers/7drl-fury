package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

public abstract class AbstractDamageEffect implements ItemEffect {
    protected Damage damage;
    protected int damageAmount;

    public AbstractDamageEffect(Damage damage, int damageAmount) {
        this.damage = damage;
        this.damageAmount = damageAmount;
    }

    @Override
    public String getDesc() {
        return "" + damageAmount + " " + damage.getType() + " dmg";
    }

    protected float multiplierFor(Mob itemUser, Stat stat) {
        int statValue = itemUser.getStatValue(stat);
        return 1f + ((float) statValue / 100f);
    }
}
