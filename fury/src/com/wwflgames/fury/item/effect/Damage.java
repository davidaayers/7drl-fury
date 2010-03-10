package com.wwflgames.fury.item.effect;

public class Damage implements ItemEffect {
    private DamageType type;
    private int amount;

    public Damage(DamageType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public DamageType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
