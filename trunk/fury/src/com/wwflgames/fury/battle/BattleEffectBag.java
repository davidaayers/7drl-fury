package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.List;

public class BattleEffectBag {
    private List<BattleEffect> effects = new ArrayList<BattleEffect>();

    private Item item;
    private Mob mob;

    public BattleEffectBag(Item item, Mob mob) {
        this.item = item;
        this.mob = mob;
    }

    public BattleEffectBag add(BattleEffect effect) {
        effects.add(effect);
        return this;
    }

    public List<BattleEffect> get() {
        return effects;
    }

    public Item item() {
        return item;
    }

    public Mob mob() {
        return mob;
    }
}
