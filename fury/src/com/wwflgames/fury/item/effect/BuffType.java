package com.wwflgames.fury.item.effect;

public enum BuffType {

    STAT("Stat boost"),
    MELEE_DMG("Melee bonus"),
    MAGIC_DMG("Magic bonus");

    private String desc;

    BuffType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
