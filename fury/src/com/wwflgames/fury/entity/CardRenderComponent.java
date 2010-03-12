package com.wwflgames.fury.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class CardRenderComponent extends RenderComponent {

    public CardRenderComponent(String id) {
        super(id);
    }

    @Override
    public void render(Graphics g) {
        Vector2f pos = owner.getPosition();

        int width = 32 * 4;
        int height = 32 * 4;

        g.setColor(Color.gray);
        g.fillRoundRect(pos.x, pos.y, width, height, 15);
        g.setColor(Color.white);
        g.drawRoundRect(pos.x, pos.y, width, height, 15);

        maybeRenderItemText(g);
    }

    protected void maybeRenderItemText(Graphics g) {
    }

    @Override
    public void update(int delta) {

    }
}
