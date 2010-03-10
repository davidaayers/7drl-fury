package com.wwflgames.fury.item;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.effect.EffectApplierFactory;
import com.wwflgames.fury.item.effect.ItemEffect;

public class ItemFactory {

    private EffectApplierFactory effectsApplierFactory;

    public ItemFactory(EffectApplierFactory effectsApplierFactory) {
        this.effectsApplierFactory = effectsApplierFactory;
    }

    public Item createItemWithUsedAgainstEffects(String name, ItemEffect[] effects ) {
        return new ItemImpl(effectsApplierFactory,name,null,effects);        
    }

}
