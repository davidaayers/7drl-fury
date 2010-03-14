package com.wwflgames.fury.monster;

import com.wwflgames.fury.mob.Mob;

public class Monster extends Mob {

    private String spriteSheet;

    public Monster(String name, String spriteSheet) {
        super(name);
        this.spriteSheet = spriteSheet;
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name() + '\'' + " " +
                "spriteSheet='" + spriteSheet + '\'' +
                '}';
    }
}
