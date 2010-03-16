package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.NItemEffect;
import com.wwflgames.fury.mob.Mob;

public class NItemImpl implements Item {

    private String name;
    private NItemEffect[] usedByEffects;
    private NItemEffect[] usedAgainstEffects;

    public NItemImpl(String name, NItemEffect[] usedByEffects, NItemEffect[] usedAgainstEffects) {
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

    private void applyEffects(NItemEffect[] effects, Mob mob, ItemUsageResult result) {
        for (NItemEffect effect : effects) {
            effect.applyEffect(mob, result);
        }
    }

    public NItemEffect[] getUsedByEffects() {
        return usedByEffects;
    }

    public NItemEffect[] getUsedAgainstEffects() {
        return usedAgainstEffects;
    }

    @Override
    public String toString() {
        return "NItemImpl: " + name;
    }
}
