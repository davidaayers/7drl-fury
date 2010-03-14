package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;

public class CrushDamageApplier extends AbstractDamageApplier {

    public CrushDamageApplier() {
        super(DamageType.MELEE_CRUSH);
    }

    @Override
    public void apply(com.wwflgames.fury.item.effect.ItemEffect effect, Mob applyTo, ItemUsageResult bag) {
        Damage damage = (Damage) effect;
        // crush damage applies first to armor (i.e. it is absorbed by armor),
        // and any remaining damage left after the armor is applied to the victim
        int armor = applyTo.getBattleStatValue(Stat.ARMOR);
        int healthBefore = applyTo.getStatValue(Stat.HEALTH);
        int armorBefore = armor;

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

        int armorAfter = applyTo.getBattleStatValue(Stat.ARMOR);
        int healthAfter = applyTo.getStatValue(Stat.HEALTH);

        int armorDelta = armorBefore - armorAfter;
        int healthDelta = healthBefore - healthAfter;

        String armorDesc = "{0} armor is crushed for {2}";
        String healthDesc = "{1} takes {2} damage!";
        if (armorBefore != 0 && armorDelta != 0) {
            bag.add(new ItemEffectResult(armorDesc, Stat.ARMOR, armorDelta));
        }
        if (healthDelta != 0) {
            bag.add(new ItemEffectResult(healthDesc, Stat.HEALTH, healthDelta));
        } else {
            bag.add(new ItemEffectResult("{0} armor absorbed all damage!", Stat.ARMOR, armorDelta));
        }
    }
}
