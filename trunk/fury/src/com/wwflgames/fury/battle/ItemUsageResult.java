package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.List;

public class ItemUsageResult {
    private List<ItemEffect> effects = new ArrayList<ItemEffect>();

    private Item item;
    private Mob mob;

    public ItemUsageResult(Item item, Mob mob) {
        this.item = item;
        this.mob = mob;
    }

    public ItemUsageResult add(ItemEffect effect) {
        effects.add(effect);
        return this;
    }

    public List<ItemEffect> get() {
        return effects;
    }

    public Item item() {
        return item;
    }

    public Mob mob() {
        return mob;
    }
}
