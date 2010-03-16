package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.mob.Mob;

public class AttackBuffEffect extends BuffEffect {

    private Damage damage;
    private int amount;

    public AttackBuffEffect(Damage damage, int amount) {
        this.damage = damage;
        this.amount = amount;
    }

    @Override
    public String getDesc() {
        return "+" + amount + " " + damage.getType() + " damage";
    }

    @Override
    public void applyEffect(Mob mob, ItemUsageResult result) {
        // add the buff to the mob, and report it
        mob.addBuff(this);
        String desc = "{0} next " + damage.getType() + " is increased by {2}";
        result.add(new ItemEffectResult(desc, amount, mob, this));
    }

    public Damage getDamage() {
        return damage;
    }

    public int getAmount() {
        return amount;
    }
}
