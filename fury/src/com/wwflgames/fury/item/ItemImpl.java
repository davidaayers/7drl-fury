package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.EffectApplierFactory;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemImpl implements Item {

    private EffectApplierFactory effectApplierFactory;
    private String name;
    private ItemEffect[] usedByEffects;
    private ItemEffect[] usedAgainstEffects;

    public ItemImpl(EffectApplierFactory effectApplierFactory, String name,
                    ItemEffect[] usedByEffects, ItemEffect[] usedAgainstEffects) {
        this.effectApplierFactory = effectApplierFactory;
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
        applyItemEffects(usedByEffects, mob, result);
        return this;
    }

    @Override
    public Item usedAgainst(Mob mob, ItemUsageResult result) {
        applyItemEffects(usedAgainstEffects, mob, result);
        return this;

    }

    private void applyItemEffects(ItemEffect[] effects, Mob mob, ItemUsageResult result) {
        if (effects != null) {
            for (ItemEffect effect : effects) {
                effectApplierFactory.applierFor(effect).apply(effect, mob, result);
            }
        }
    }

    public ItemEffect[] getUsedByEffects() {
        return usedByEffects;
    }

    public ItemEffect[] getUsedAgainstEffects() {
        return usedAgainstEffects;
    }

    public List<ItemEffect> allEffects() {
        List<ItemEffect> list = new ArrayList<ItemEffect>();
        if (usedByEffects != null) {
            list.addAll(Arrays.asList(usedByEffects));
        }
        if (usedAgainstEffects != null) {
            list.addAll(Arrays.asList(usedAgainstEffects));
        }
        return list;
    }

    @Override
    public String toString() {
        return "ItemImpl{" +
                "name='" + name + '\'' +
                '}';
    }
}
