package com.wwflgames.fury.mob;

import com.wwflgames.fury.item.ItemDeck;

import java.util.HashMap;
import java.util.Map;

public class Mob {

    private Map<Stat, Integer> stats = new HashMap<Stat, Integer>();
    private Map<Stat, Integer> battleStats;
    private ItemDeck deck;
    private String name;

    public Mob(String name) {
        this.name = name;
        resetBattleStats();
    }

    public Integer getStatValue(Stat stat) {
        Integer statValue = stats.get(stat);
        if ( statValue == null ) {
            return 0;
        }
        return statValue;
    }

    public void setStatValue(Stat stat, Integer value) {
        stats.put(stat, value);
    }

    public void modifyStatValue(Stat stat, Integer value) {
        int oldValue = getStatValue(stat);
        int newValue = oldValue + value;
        stats.put(stat, newValue);
    }

    public Integer getBattleStatValue(Stat stat) {
        Integer statValue = battleStats.get(stat);
        if ( statValue == null ) {
            return 0;
        }
        return statValue;
    }

    public void setBattleStatValue(Stat stat, Integer value) {
        battleStats.put(stat, value);
    }

    public void modifyBattleStatValue(Stat stat, Integer value) {
        int oldValue = getBattleStatValue(stat);
        int newValue = oldValue + value;
        battleStats.put(stat, newValue);
    }

    public void resetBattleStats() {
        battleStats = new HashMap<Stat, Integer>();    
    }

    public void prepareForBattle() {
        // clear out anything that's already in battle stats
        battleStats.clear();

        // add anything in stats that's a battle stat to battle stats. This
        // is the mob's initial battle stats.
        for ( Stat stat : stats.keySet() ) {
            battleStats.put(stat,getStatValue(stat));
        }
    }

    public ItemDeck getDeck() {
        return deck;
    }

    public void setDeck(ItemDeck deck) {
        this.deck = deck;
    }

    public boolean isDead() {
        return stats.get(Stat.HEALTH) <= 0;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "Mob{" +
                "name='" + name + '\'' +
                "health='" + stats.get(Stat.HEALTH) + '\'' +
                '}';
    }
}
