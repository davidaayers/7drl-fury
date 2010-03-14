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
    private Map<Mob, List<ItemUsageResult>> mobResultsMap = new HashMap<Mob, List<ItemUsageResult>>();
    private Map<Mob, Item> itemUsedMap = new HashMap<Mob, Item>();

    public BattleRoundResult(int battleRound) {
        this.battleRound = battleRound;
    }

    public void addEffectList(Mob mob, ItemUsageResult itemUsageResult) {

        // if there are no effects in the bag, don't do anything
        if (itemUsageResult.get().isEmpty()) {
            return;
        }

        // grab the bagList for this mob
        List<ItemUsageResult> bagList = mobResultsMap.get(mob);
        if (bagList == null) {
            bagList = new ArrayList<ItemUsageResult>();
            mobResultsMap.put(mob, bagList);
        }
        bagList.add(itemUsageResult);
    }

    public List<ItemUsageResult> monsterItemEffectList() {
        for (Mob mob : mobResultsMap.keySet()) {
            if (mob instanceof Player) {
                return mobResultsMap.get(mob);
            }
        }
        System.out.println("***** no effects found for player ******");
        // didn't find any effects for the player, return an empty one
        return new ArrayList<ItemUsageResult>();
    }

    public List<ItemUsageResult> playerItemEffectList() {
        List<ItemUsageResult> allMonsterEffectBags = new ArrayList<ItemUsageResult>();
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
