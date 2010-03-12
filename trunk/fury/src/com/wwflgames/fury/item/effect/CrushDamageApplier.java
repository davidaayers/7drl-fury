package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;

public class CrushDamageApplier extends AbstractDamageApplier {

    public CrushDamageApplier() {
        super(DamageType.MELEE_CRUSH);
    }

    @Override
    public void apply(ItemEffect effect, Mob applyTo) {
        Damage damage = (Damage) effect;
        // crush damage applies first to armor (i.e. it is absorbed by armor),
        // and any remaining damage left after the armor is applied to the victim
        int armor = applyTo.getBattleStatValue(Stat.ARMOR);

        int dmg = damage.getAmount();

        Log.debug("Armor before: " + armor);
        Log.debug("Dmg before  : " + dmg);

        if (dmg > armor) {
            dmg -= armor;
            armor = 0;
        } else {
            armor -= dmg;
            dmg = 0;
        }

        Log.debug("Armor after : " + armor);
        Log.debug("Dmg after   : " + dmg);

        applyTo.modifyStatValue(Stat.HEALTH, -dmg);
        applyTo.setBattleStatValue(Stat.ARMOR, armor);
    }

}
