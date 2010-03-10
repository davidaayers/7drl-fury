package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.item.Shuffler;
import com.wwflgames.fury.item.effect.Damage;
import com.wwflgames.fury.item.effect.DamageType;
import com.wwflgames.fury.item.effect.EffectApplierFactory;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class BattleSystemTest {

    private BattleSystem battleSystem;
    private Shuffler shufflerStub;
    private ItemFactory itemFactory;

    @Before
    public void setUp() throws Exception {
        EffectApplierFactory effectApplierFactory = new EffectApplierFactory();
        itemFactory = new ItemFactory(effectApplierFactory);
        shufflerStub = new Shuffler() {
            @Override
            public void shuffle(List list) {
            }
        };
    }

    @Test
    public void testOneRoundBattleAgainstOneMobWithPlayerInitiative() {
        Battle b = createBattle(10,10,5,1,true);
        battleSystem = new BattleSystem(shufflerStub,b);

        doBattle();

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(10),b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(-5),b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    @Test
    public void testOneRoundBattleAgainstOneMobWithMobInitiative() {
        Battle b = createBattle(10,10,5,1,false);
        battleSystem = new BattleSystem(shufflerStub,b);

        doBattle();

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(9),b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(-5),b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    @Test
    public void testTwoRoundBattleAgainstOneMobWithPlayerInitiative() {
        Battle b = createBattle(10,5,10,1,true);
        battleSystem = new BattleSystem(shufflerStub,b);

        doBattle();

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(9),b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0),b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    @Test
    public void testTwoRoundBattleAgainstOneMobWithMobInitiative() {
        Battle b = createBattle(10,5,10,1,false);
        battleSystem = new BattleSystem(shufflerStub,b);

        doBattle();

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(8),b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0),b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    private Battle createBattle(int playerHealth,int playerDmg, int mobHealth, int mobDamage ,
                                boolean playerInitiative) {
        Mob player = newMob("Player", playerHealth);
        player.getDeck().addItem(newItem(playerDmg));
        Mob enemy = newMob("Enemy 1", mobHealth);
        enemy.getDeck().addItem(newItem(mobDamage));
        List<Mob> mobs = new ArrayList<Mob>();
        mobs.add(enemy);

        Battle b = new Battle(player,mobs,playerInitiative);
        return b;
    }

    private void doBattle() {
        battleSystem.startBattle();
        while ( !battleSystem.isBattleOver() ) {
            battleSystem.performBattleRound();
        }
    }

    private Mob newMob(String name, int health) {
        Mob mob = new Mob(name);
        mob.setStatValue(Stat.HEALTH,health);
        mob.setDeck(new ItemDeck(shufflerStub));
        return mob;
    }

    private Item newItem(final int damage) {
        Damage crushDamage = new Damage(DamageType.MELEE_CRUSH, damage);
        Item mace = itemFactory.createItemWithUsedAgainstEffects("Mace of crushing", new ItemEffect[]{crushDamage});
        return mace;
    }

}
