package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.damage.*;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MeleeDamageEffect implements ItemEffect {

    private MeleeDamage damage;
    private int damageAmount;

    public MeleeDamageEffect(MeleeDamage damage, int damageAmount) {
        this.damage = damage;
        this.damageAmount = damageAmount;
    }

    @Override
    public String getDesc() {
        return "" + damageAmount + " " + damage.getType() + " dmg";
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result) {
        if (damage instanceof CrushDamage) {
            applyCrushDamage(itemUsedUpon, result);
        } else if (damage instanceof SlashDamage) {
            applySlashDamage(itemUsedUpon, result);
        } else if (damage instanceof StabDamage) {
            applyStabDamage(itemUsedUpon, result);
        }
    }

    // crush damage gets applied to armor first (and destroys it, at least
    // for this combat round), then to health
    private void applyCrushDamage(Mob mob, ItemUsageResult result) {

        List<AttackBuffEffect> attackBuffs = findAndRemoveApplicableBuffs(mob, Damage.CRUSH_DAMAGE);

        Log.debug("Got " + attackBuffs + " attack buffs");

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
            result.add(new ItemEffectResult(armorDesc, armorDelta, mob, this));
        }
        if (healthDelta != 0) {
            result.add(new ItemEffectResult(healthDesc, healthDelta, mob, this));
        } else {
            result.add(new ItemEffectResult("{0} armor absorbed all damage!", armorDelta, mob, this));
        }
    }

    private List<AttackBuffEffect> findAndRemoveApplicableBuffs(Mob mob, Damage damageType) {
        List<AttackBuffEffect> applicableBuffs = new ArrayList<AttackBuffEffect>();
        for (BuffEffect buff : mob.getBuffs()) {
            if (buff instanceof AttackBuffEffect) {
                AttackBuffEffect attackBuff = (AttackBuffEffect) buff;
                Log.debug("attackBuff = " + attackBuff.getDesc());
                if (attackBuff.getClass().isAssignableFrom(damageType.getClass())) {
                    Log.debug("It's assignable");
                    applicableBuffs.add(attackBuff);
                }

            }
        }
        // now, remove the buffs from the mob, because we're about to use them
        for (AttackBuffEffect buff : applicableBuffs) {
            mob.removeBuff(buff);
        }
        return applicableBuffs;
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