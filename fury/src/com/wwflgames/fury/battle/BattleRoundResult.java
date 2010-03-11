package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Mob;

// created by the battle system to communicate the events that occurred during the battle round,
// so that can be shown on the UI
public class BattleRoundResult {

    private Mob enemy;
    private int battleRound;


    public BattleRoundResult(int battleRound) {
        this.battleRound = battleRound;
    }
    
}
