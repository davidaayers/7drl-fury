package com.wwflgames.fury.player;

public class Profession {
    private String className;
    private String spriteSheet;

    public Profession(String className, String spriteSheet) {
        this.className = className;
        this.spriteSheet = spriteSheet;
    }

    public String getProfessionName() {
        return className;
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }
}
