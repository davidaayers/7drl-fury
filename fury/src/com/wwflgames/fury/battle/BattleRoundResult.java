package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
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
    private Map<Mob, List<BattleEffectBag>> mobResultsMap = new HashMap<Mob, List<BattleEffectBag>>();
    private Map<Mob, Item> itemUsedMap = new HashMap<Mob, Item>();

    public BattleRoundResult(int battleRound) {
        this.battleRound = battleRound;
    }

    public void addEffectList(Mob mob, BattleEffectBag battleEffectBag) {

        // if there are no effects in the bag, don't do anything
        if (battleEffectBag.get().isEmpty()) {
            return;
        }

        // grab the bagList for this mob
        List<BattleEffectBag> bagList = mobResultsMap.get(mob);
        if (bagList == null) {
            bagList = new ArrayList<BattleEffectBag>();
            mobResultsMap.put(mob, bagList);
        }
        bagList.add(battleEffectBag);
    }

    public List<BattleEffectBag> playerEffectList() {
        for (Mob mob : mobResultsMap.keySet()) {
            if (mob instanceof Player) {
                return mobResultsMap.get(mob);
            }
        }
        System.out.println("***** no effects found for player ******");
        // didn't find any effects for the player, return an empty one
        return new ArrayList<BattleEffectBag>();
    }

    public List<BattleEffectBag> monsterEffectList() {
        List<BattleEffectBag> allMonsterEffectBags = new ArrayList<BattleEffectBag>();
        for (Mob mob : mobResultsMap.keySet()) {
            if (mob instanceof Monster) {
                allMonsterEffectBags.addAll(mobResultsMap.get(mob));
            }
        }
        return allMonsterEffectBags;
    }

    public void addItemUsedBy(Mob mob, Item item) {
        itemUsedMap.put(mob, item);
    }

    public Item getItemUsedBy(Mob mob) {
        return itemUsedMap.get(mob);
    }


}
