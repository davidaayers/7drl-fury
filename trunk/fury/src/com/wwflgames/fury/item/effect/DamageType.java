package com.wwflgames.fury.item.effect;

public enum DamageType {
    MELEE_CRUSH("Crush"),
    MELEE_STAB("Stab"),
    MAGIC_FIRE("Fire"),
    MAGIC_ICE("Ice"),
    POISON("Poison");

    private String desc;

    DamageType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
