package com.wwflgames.fury.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class SpriteSheetRenderComponent extends RenderComponent {

    private SpriteSheet spriteSheet;
    private int spriteCol;
    private int spriteRow;

    public SpriteSheetRenderComponent(String id, SpriteSheet spriteSheet, int spriteCol, int spriteRow) {
        super(id);
        this.spriteSheet = spriteSheet;
        this.spriteCol = spriteCol;
        this.spriteRow = spriteRow;
    }

    public void useSprite(int spriteCol, int spriteRow) {
        this.spriteCol = spriteCol;
        this.spriteRow = spriteRow;
    }

    @Override
    public void render(Graphics gr) {
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();
        current().draw(pos.x,pos.y,scale);
    }

    @Override
    public void update(int delta) {
        current().rotate(owner.getRotation() - current().getRotation());

    }

    private Image current() {
        return spriteSheet.getSprite(spriteCol,spriteRow);
    }
}
