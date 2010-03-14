package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

public class ItemEffectResult {
    private String desc;
    private Stat stat;
    private Integer delta;
    private Mob effectedMob;
    private ItemEffect effect;

    public ItemEffectResult(String desc, Stat stat, Integer delta, Mob effectedMob, ItemEffect effect) {
        this.desc = desc;
        this.stat = stat;
        this.delta = delta;
        this.effectedMob = effectedMob;
        this.effect = effect;
    }

    public String getDesc() {
        return desc;
    }

    public Stat getStat() {
        return stat;
    }

    public Integer getDelta() {
        return delta;
    }

    public Mob getEffectedMob() {
        return effectedMob;
    }

    public ItemEffect getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return "ItemEffectResult{" +
                "desc='" + desc + '\'' +
                ", stat=" + stat +
                ", delta=" + delta +
                '}';
    }
}
