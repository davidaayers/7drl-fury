package com.wwflgames.fury.item.neffect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.neffect.damage.CrushDamage;
import com.wwflgames.fury.item.neffect.damage.MeleeDamage;
import com.wwflgames.fury.item.neffect.damage.SlashDamage;
import com.wwflgames.fury.item.neffect.damage.StabDamage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;

public class NMeleeDamageEffect implements NItemEffect {

    private MeleeDamage damage;
    private int damageAmount;

    public NMeleeDamageEffect(MeleeDamage damage, int damageAmount) {
        this.damage = damage;
        this.damageAmount = damageAmount;
    }

    @Override
    public String getDesc() {
        return "" + damageAmount + " " + damage.getType() + " dmg";
    }

    @Override
    public void applyEffect(Mob mob, ItemUsageResult result) {
        if (damage instanceof CrushDamage) {
            applyCrushDamage(mob, result);
        } else if (damage instanceof SlashDamage) {
            applySlashDamage(mob, result);
        } else if (damage instanceof StabDamage) {
            applyStabDamage(mob, result);
        }
    }

    // crush damage gets applied to armor first (and destroys it, at least
    // for this combat round), then to health
    private void applyCrushDamage(Mob mob, ItemUsageResult result) {
        int armor = mob.getBattleStatValue(Stat.ARMOR);
        int healthBefore = mob.getStatValue(Stat.HEALTH);
        int armorBefore = armor;

        int dmg = damageAmount;

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

        mob.modifyStatValue(Stat.HEALTH, -dmg);
        mob.setBattleStatValue(Stat.ARMOR, armor);

        int armorAfter = mob.getBattleStatValue(Stat.ARMOR);
        int healthAfter = mob.getStatValue(Stat.HEALTH);

        int armorDelta = armorBefore - armorAfter;
        int healthDelta = healthBefore - healthAfter;

        String armorDesc = "{0} armor is crushed for {2}";
        String healthDesc = "{1} takes {2} damage!";
        if (armorBefore != 0 && armorDelta != 0) {
            result.add(new ItemEffectResult(armorDesc, Stat.ARMOR, armorDelta, mob, this));
        }
        if (healthDelta != 0) {
            result.add(new ItemEffectResult(healthDesc, Stat.HEALTH, healthDelta, mob, this));
        } else {
            result.add(new ItemEffectResult("{0} armor absorbed all damage!", Stat.ARMOR, armorDelta, mob, this));
        }
    }

    // slash damage is reduced by 10% for every 10 points of armor. So
    // if the mob has 100 armor or more, they are basically immune to
    // slash damage
    private void applySlashDamage(Mob mob, ItemUsageResult result) {

    }

    // not sure how stab damage should work. Ignore armor?
    private void applyStabDamage(Mob mob, ItemUsageResult result) {

    }

}
