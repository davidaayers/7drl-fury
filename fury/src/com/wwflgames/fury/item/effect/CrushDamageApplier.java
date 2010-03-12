package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.BattleEffect;
import com.wwflgames.fury.battle.BattleEffectList;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;

public class CrushDamageApplier extends AbstractDamageApplier {

    public CrushDamageApplier() {
        super(DamageType.MELEE_CRUSH);
    }

    @Override
    public void apply(ItemEffect effect, Mob applyTo, BattleEffectList list) {
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
        list.add(new BattleEffect(armorDesc, Stat.ARMOR, armorDelta))
                .add(new BattleEffect(healthDesc, Stat.HEALTH, healthDelta));

    }
}
