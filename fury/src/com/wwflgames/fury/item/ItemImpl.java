package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.BattleEffectBag;
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
    public BattleEffectBag usedBy(Mob mob) {
        BattleEffectBag bag = new BattleEffectBag(this, mob);
        applyItemEffects(usedByEffects, mob, bag);
        return bag;
    }

    @Override
    public BattleEffectBag usedAgainst(Mob mob) {
        BattleEffectBag bag = new BattleEffectBag(this, mob);
        applyItemEffects(usedAgainstEffects, mob, bag);
        return bag;

    }

    private void applyItemEffects(ItemEffect[] effects, Mob mob, BattleEffectBag bag) {
        if (effects != null) {
            for (ItemEffect effect : effects) {
                effectApplierFactory.applierFor(effect).apply(effect, mob, bag);
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

}
