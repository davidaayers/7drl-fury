package com.wwflgames.fury.item;

import com.wwflgames.fury.item.effect.*;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ItemImplTest {

    private ItemFactory itemFactory;

    @Before
    public void setUp() {
        EffectApplierFactory effectApplierFactory = new EffectApplierFactory();
        itemFactory = new ItemFactory(effectApplierFactory);
    }

    @Test
    public void testSimpleDamageItem() {
        Damage crushDamage = new Damage(DamageType.MELEE_CRUSH, 5);
        Item mace = itemFactory.createItemWithUsedAgainstEffects("Mace of crushing", new ItemEffect[]{crushDamage});
        Mob mob = new Mob("test mob");
        mob.setStatValue(Stat.HEALTH,10);
        mob.setBattleStatValue(Stat.ARMOR,5);
        mace.usedAgainst(mob);
        assertEquals(new Integer(10),mob.getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0),mob.getBattleStatValue(Stat.ARMOR));
        mace.usedAgainst(mob);
        assertEquals(new Integer(5),mob.getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0),mob.getBattleStatValue(Stat.ARMOR));
        mace.usedAgainst(mob);
        assertEquals(new Integer(0),mob.getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0),mob.getBattleStatValue(Stat.ARMOR));
        assertTrue(mob.isDead());

    }

    @Test
    public void testCompoundDamageItem() {
        Damage stabDamage = new Damage(DamageType.MELEE_STAB, 5);
        DamageOverTime poison = new DamageOverTime("Deadly Poison", 3, DamageType.POISON, 5);
        Item dagger = itemFactory.createItemWithUsedAgainstEffects("Poison dagger",
                new ItemEffect[]{stabDamage, poison});
    }


}
