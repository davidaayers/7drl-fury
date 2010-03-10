package com.wwflgames.fury.item.effect;

public abstract class AbstractDamageApplier implements DamageApplier {

    private DamageType type;

    public AbstractDamageApplier(DamageType type) {
        this.type = type;
    }

    @Override
    public DamageType appliesFor() {
        return type;
    }

}
