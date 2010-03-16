package com.wwflgames.fury.player;

import com.wwflgames.fury.item.ItemDeck;

public class Profession {
    private String name;
    private String spriteSheet;
    private ItemDeck starterDeck;

    public Profession(String name, String spriteSheet, ItemDeck starterDeck) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.starterDeck = starterDeck;
    }

    public String getName() {
        return name;
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    public ItemDeck getStarterDeck() {
        return starterDeck;
    }

    @Override
    public String toString() {
        return "Profession{" +
                "name='" + name + '\'' +
                ", spriteSheet='" + spriteSheet + '\'' +
                '}';
    }
}
