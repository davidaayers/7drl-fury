package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// created by the battle system to communicate the events that occurred during the battle round,

// so that can be shown on the UI
public class BattleRoundResult {

    private Mob enemy;
    private int battleRound;
    private Map<Mob, List<BattleEffectList>> mobResults = new HashMap<Mob, List<BattleEffectList>>();

    public BattleRoundResult(int battleRound) {
        this.battleRound = battleRound;
    }

    public void addEffectList(Mob attacker, BattleEffectList battleEffectList) {
        // grab the list for this mob
        List<BattleEffectList> list = mobResults.get(attacker);
        if (list == null) {
            list = new ArrayList<BattleEffectList>();
            mobResults.put(attacker, list);
        }
        list.add(battleEffectList);
    }

    public List<BattleEffectList> playerEffectList() {
        for (Mob mob : mobResults.keySet()) {
            if (mob instanceof Player) {
                return mobResults.get(mob);
            }
        }
        // didn't find any effects for the player, return an empty one
        return new ArrayList<BattleEffectList>();
    }

    public List<BattleEffectList> monsterEffectList() {
        List<BattleEffectList> allMonsters = new ArrayList<BattleEffectList>();
        for (Mob mob : mobResults.keySet()) {
            if (mob instanceof Monster) {
                allMonsters.addAll(mobResults.get(mob));
            }
        }
        return allMonsters;
    }


}
