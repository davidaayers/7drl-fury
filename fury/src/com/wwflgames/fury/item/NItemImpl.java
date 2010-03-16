package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;

public class NItemImpl implements Item {

    private String name;
    private ItemEffect[] usedByEffects;
    private ItemEffect[] usedAgainstEffects;

    public NItemImpl(String name, ItemEffect[] usedByEffects, ItemEffect[] usedAgainstEffects) {
        this.name = name;
        this.usedByEffects = usedByEffects;
        this.usedAgainstEffects = usedAgainstEffects;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Item usedBy(Mob mob, ItemUsageResult result) {
        applyEffects(usedByEffects, mob, result);
        return this;
    }

    @Override
    public Item usedAgainst(Mob mob, ItemUsageResult result) {
        applyEffects(usedAgainstEffects, mob, result);
        return this;
    }

    private void applyEffects(ItemEffect[] effects, Mob mob, ItemUsageResult result) {
        for (ItemEffect effect : effects) {
            effect.applyEffect(mob, result);
        }
    }

    public ItemEffect[] getUsedByEffects() {
        return usedByEffects;
    }

    public ItemEffect[] getUsedAgainstEffects() {
        return usedAgainstEffects;
    }

    @Override
    public String toString() {
        return "NItemImpl: " + name;
    }
}
