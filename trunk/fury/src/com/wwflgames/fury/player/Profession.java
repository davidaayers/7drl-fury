package com.wwflgames.fury.player;

public class Profession {
    private String name;
    private String spriteSheet;

    public Profession(String name, String spriteSheet) {
        this.name = name;
        this.spriteSheet = spriteSheet;
    }

    public String getName() {
        return name;
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    @Override
    public String toString() {
        return "Profession{" +
                "name='" + name + '\'' +
                ", spriteSheet='" + spriteSheet + '\'' +
                '}';
    }
}
