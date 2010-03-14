package com.wwflgames.fury.player;

import com.wwflgames.fury.mob.Mob;

public class Player extends Mob {

    private Profession profession;

    public Player(String name, Profession profession) {
        super(name);
        this.profession = profession;
    }

    public Profession getProfession() {
        return profession;
    }
}
