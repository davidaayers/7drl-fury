package com.wwflgames.fury.monster;

import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Mob;

public class Monster extends Mob {

    private String spriteSheet;

    public Monster(String name, String spriteSheet) {
        super(name);
        this.spriteSheet = spriteSheet;
    }

    public Monster(Monster other) {
        this(other.name(), other.getSpriteSheet());
        setDeck(new ItemDeck(other.getDeck()));
        this.stats.putAll(other.stats);
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
