package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.BattleEffectBag;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CrushBattleDamageApplierTest {

    private CrushDamageApplier crushBattleDamageApplier;

    @Before
    public void setUp() throws Exception {
        crushBattleDamageApplier = new CrushDamageApplier();
    }

    @Test
    public void testAppliesToArmorBeforeDamage() {
        Mob victim = newMob(5, 10);
        crushBattleDamageApplier.apply(new Damage(DamageType.MELEE_CRUSH, 10), victim, new BattleEffectBag(null, victim));
        assertEquals(new Integer(0), victim.getBattleStatValue(Stat.ARMOR));
        assertEquals(new Integer(5), victim.getStatValue(Stat.HEALTH));
    }

    @Test
    public void testMobWithNoArmorTakesFullDamage() {
        Mob victim = newMob(0, 10);
        crushBattleDamageApplier.apply(new Damage(DamageType.MELEE_CRUSH, 5), victim, new BattleEffectBag(null, victim));
        assertEquals(new Integer(0), victim.getBattleStatValue(Stat.ARMOR));
        assertEquals(new Integer(5), victim.getStatValue(Stat.HEALTH));
    }

    @Test
    public void testMobWithLotsOfArmorAbsorbsAllDamage() {
        Mob victim = newMob(100, 10);
        crushBattleDamageApplier.apply(new Damage(DamageType.MELEE_CRUSH, 5), victim, new BattleEffectBag(null, victim));
        assertEquals(new Integer(95), victim.getBattleStatValue(Stat.ARMOR));
        assertEquals(new Integer(10), victim.getStatValue(Stat.HEALTH));
    }

    private Mob newMob(int armor, int health) {
        Mob victim = new Mob("test");
        victim.setBattleStatValue(Stat.ARMOR, armor);
        victim.setStatValue(Stat.HEALTH, health);
        return victim;
    }

}
