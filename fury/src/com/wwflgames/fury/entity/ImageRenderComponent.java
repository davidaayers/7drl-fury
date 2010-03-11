package com.wwflgames.fury.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class ImageRenderComponent extends RenderComponent {

    private Image image;

    public ImageRenderComponent(String id, Image image) {
        super(id);
        this.image = image;
    }

    @Override
    public void render(Graphics gr) {
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();
        image.draw(pos.x, pos.y, scale);
    }

    @Override
    public void update(int delta) {
        image.rotate(owner.getRotation() - image.getRotation());
    }
}
