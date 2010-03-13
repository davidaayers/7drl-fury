package com.wwflgames.fury.item.effect;

public class DamageOverTime implements ItemEffect {

    private String name;
    private int numBattleRounds;
    private DamageType damageType;
    private int damage;

    public DamageOverTime(String name, int numBattleRounds, DamageType damageType, int damage) {
        this.name = name;
        this.numBattleRounds = numBattleRounds;
        this.damageType = damageType;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public int getNumBattleRounds() {
        return numBattleRounds;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String getEffectDesc() {
        return null;
    }
}
