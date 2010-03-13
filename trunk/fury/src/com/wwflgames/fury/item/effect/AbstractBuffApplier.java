package com.wwflgames.fury.item.effect;

public abstract class AbstractBuffApplier implements EffectApplier {

    private BuffType buffType;

    public AbstractBuffApplier(BuffType buffType) {
        this.buffType = buffType;
    }

    public BuffType getBuffType() {
        return buffType;
    }
}
