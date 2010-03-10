package com.wwflgames.fury.item;

import com.wwflgames.fury.item.effect.EffectApplierFactory;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;

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
    public void usedBy(Mob mob) {
        applyItemEffects(usedByEffects,mob);
    }

    @Override
    public void usedAgainst(Mob mob) {
        applyItemEffects(usedAgainstEffects,mob);

    }

    private void applyItemEffects(ItemEffect[] effects, Mob mob) {
        if ( effects != null ) {
            for ( ItemEffect effect: effects ) {
                effectApplierFactory.applierFor(effect).apply(effect,mob);
            }
        }
    }

}
