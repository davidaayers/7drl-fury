package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.effect.NItemEffect;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

public class ItemEffectResult {
    private String desc;
    private Stat stat;
    private Integer delta;
    private Mob effectedMob;
    private NItemEffect effect;

    public ItemEffectResult(String desc, Mob effectedMob, NItemEffect effect) {
        this.desc = desc;
        this.effectedMob = effectedMob;
        this.effect = effect;
    }

    public ItemEffectResult(String desc, Stat stat, Integer delta, Mob effectedMob, NItemEffect effect) {
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

    public NItemEffect getEffect() {
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
