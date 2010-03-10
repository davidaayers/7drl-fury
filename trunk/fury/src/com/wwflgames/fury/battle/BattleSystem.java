package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Shuffler;

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
        battleRound = 0;
        // prepare all of the mobs for battle. This will set their
        // battle stats. Also, shuffle all of their decks.
        for ( Mob mob: battle.getAllBattleParticipants()) {
            mob.prepareForBattle();
            mob.getDeck().shuffle();
        }
    }

    public void performBattleRound() {
        // increment the battle Round
        battleRound ++;
        Log.debug("=========( Round " + battleRound + " )==============");
        

        // see who got initiate and let them go first
        if ( battle.isPlayerInitiate() ) {
            doPlayerRoundAndCheckIfPlayerWon();
        } else {
            doEnemyRoundAndCheckIfPlayerLost();
        }

        // now, let the other side attack
        if ( battle.isPlayerInitiate() ) {
            doEnemyRoundAndCheckIfPlayerLost();
        } else {
            doPlayerRoundAndCheckIfPlayerWon();
        }

        Log.debug("Round " + battleRound + " is over");
    }

    private void doPlayerRoundAndCheckIfPlayerWon() {
        doPlayerRound();
        removeDeadMonstersFromBattle();
        
        if ( battle.allEnemiesDead() ) {
            playerWon();
        }
    }

    private void doEnemyRoundAndCheckIfPlayerLost() {
        doEnemyRound();
        if ( battle.getPlayer().isDead() ) {
            playerLost();
        }
    }

    private void removeDeadMonstersFromBattle() {
        List<Mob> enemiesToRemove = new ArrayList<Mob>();
        for ( Mob enemy : battle.getEnemies() ) {
            Log.debug("Seeing if " + enemy.name() + " is dead");
            if ( enemy.isDead() ) {
                Log.debug(enemy + " was dead, removing");
                enemiesToRemove.add(enemy);
            }
        }

        for ( Mob enemyToRemove : enemiesToRemove ) {
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

    private void doPlayerRound() {
        // pick an enemy randomly.
        List<Mob> mobs = new ArrayList<Mob>();
        mobs.addAll(battle.getEnemies());
        Log.debug("Enemies in battle: " + mobs);
        Shuffler.shuffle(mobs);
        // grab the first one in the list
        Mob first = mobs.get(0);
        Log.debug("Enemy player is facing now: " + first.name());
        doNextItemInDeck(battle.getPlayer(),first);
    }

    private void doEnemyRound() {
        for ( Mob enemy : battle.getEnemies() ) {
            doNextItemInDeck(enemy,battle.getPlayer());
        }
    }

    private void doNextItemInDeck(Mob attacker, Mob defender) {
        Log.debug("Next item in deck, attacker = " + attacker.name() + ", defender = " + defender.name());
        // grab the next item from the attackers deck
        Item item = attacker.getDeck().nextItem();
        Log.debug("Item chosen from deck is " + item.name() );
        item.usedBy(attacker);
        item.usedAgainst(defender);
    }

    public int getBattleRound() {
        return battleRound;
    }
}
