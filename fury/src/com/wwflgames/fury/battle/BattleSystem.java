package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BattleSystem {

    private Battle battle;
    private int battleRound;
    private boolean battleOver = false;
    private boolean playerWon = false;

    public BattleSystem(Battle battle) {
        this.battle = battle;
    }

    public void startBattle() {
        Log.debug("Starting battle...");
        battleRound = 1;
        // prepare all of the mobs for battle. This will set their
        // battle stats. Also, shuffle all of their decks.
        for (Mob mob : battle.getAllBattleParticipants()) {
            mob.prepareForBattle();
            mob.getDeck().shuffle();
        }
    }

    public BattleRoundResult performBattleRound(Monster monster) {
        // increment the battle Round
        battleRound++;
        BattleRoundResult battleRoundResult = new BattleRoundResult(battleRound);
        Log.debug("=========( Round " + battleRound + " )==============");


        // see who got initiate and let them go first
        if (battle.isPlayerInitiate()) {
            doPlayerRoundAndCheckIfPlayerWon(monster, battleRoundResult);
        } else {
            doEnemyRoundAndCheckIfPlayerLost(battleRoundResult);
        }

        // now, let the other side attack
        if (battle.isPlayerInitiate()) {
            doEnemyRoundAndCheckIfPlayerLost(battleRoundResult);
        } else {
            doPlayerRoundAndCheckIfPlayerWon(monster, battleRoundResult);
        }

        Log.debug("Round " + battleRound + " is over");
        return battleRoundResult;
    }

    private void doPlayerRoundAndCheckIfPlayerWon(Monster monster, BattleRoundResult result) {
        doNextItemInDeck(battle.getPlayer(), monster, result);
        removeDeadMonstersFromBattle();

        if (battle.allEnemiesDead()) {
            playerWon();
        }
    }

    private void doEnemyRoundAndCheckIfPlayerLost(BattleRoundResult result) {
        for (Mob enemy : battle.getEnemies()) {
            doNextItemInDeck(enemy, battle.getPlayer(), result);
        }

        if (battle.getPlayer().isDead()) {
            playerLost();
        }
    }

    private void doNextItemInDeck(Mob attacker, Mob defender, BattleRoundResult result) {
        Log.debug("Next item in deck, attacker = " + attacker.name() + ", defender = " + defender.name());
        // grab the next item from the attackers deck
        Item item = attacker.getDeck().nextItem();
        result.addItemUsedBy(attacker, item);

        Log.debug("Item chosen from deck is " + item.name());
        BattleEffectBag usedByBag = item.usedBy(attacker);
        BattleEffectBag usedAgainstBag = item.usedAgainst(defender);
        //TODO: this isn't right, need to figure this out. defender and attacker are mixed
        //up in the battle results
        result.addEffectList(defender, usedByBag);
        result.addEffectList(defender, usedAgainstBag);
        Log.debug("Used against list = " + usedAgainstBag.get());
    }

    private void removeDeadMonstersFromBattle() {
        List<Mob> enemiesToRemove = new ArrayList<Mob>();
        for (Mob enemy : battle.getEnemies()) {
            Log.debug("Seeing if " + enemy.name() + " is dead");
            if (enemy.isDead()) {
                Log.debug(enemy + " was dead, removing");
                enemiesToRemove.add(enemy);
            }
        }

        for (Mob enemyToRemove : enemiesToRemove) {
            battle.removeEnemy(enemyToRemove);
        }
    }

    private void playerWon() {
        battleOver = true;
        playerWon = true;
        Log.debug("Player won :)");
    }

    private void playerLost() {
        battleOver = true;
        playerWon = false;
        Log.debug("Player lost :(");
    }

    public boolean isBattleOver() {
        return battleOver;
    }

    public boolean didPlayerWin() {
        return playerWon;
    }

    public int getBattleRound() {
        return battleRound;
    }
}
