package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.mob.Stat;

public class StatBuff extends Buff {
    private Stat stat;
    private int amount;

    public StatBuff(Stat stat, int amount) {
        this.stat = stat;
        this.amount = amount;
    }

    public Stat getStat() {
        return stat;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String getEffectDesc() {
        return "+" + amount + " " + stat.getDesc();
    }
}
