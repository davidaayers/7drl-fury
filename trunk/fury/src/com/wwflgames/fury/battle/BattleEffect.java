package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Stat;

public class BattleEffect {
    private String desc;
    private Stat stat;
    private Integer delta;

    public BattleEffect(String desc, Stat stat, Integer delta) {
        this.desc = desc;
        this.stat = stat;
        this.delta = delta;
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

    @Override
    public String toString() {
        return "BattleEffect{" +
                "desc='" + desc + '\'' +
                ", stat=" + stat +
                ", delta=" + delta +
                '}';
    }
}
