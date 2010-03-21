package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.damage.MagicDamage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;

public class MagicDamageEffect extends AbstractDamageEffect {

    public MagicDamageEffect(MagicDamage damage, int damageAmount) {
        super(damage, damageAmount);
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result) {
        int dmg = damageAmount;

        float multiplier = multiplierFor(itemUser, Stat.MAGIC);
        Log.debug("multiplier was " + multiplier);
        int origDmg = dmg;
        dmg *= multiplier;
        if (origDmg != dmg) {
            String msg = "{0} magic increased the attack by {2}!";
            result.add(new ItemEffectResult(msg, dmg - origDmg, itemUser, this));
        }

        String healthDesc = "{1} takes {2} damage!";
        itemUsedUpon.modifyStatValue(Stat.HEALTH, -dmg);
        Log.debug(itemUsedUpon.name() + " health is now " + itemUsedUpon.getStatValue(Stat.HEALTH));
        result.add(new ItemEffectResult(healthDesc, dmg, itemUsedUpon, this));
    }
}
