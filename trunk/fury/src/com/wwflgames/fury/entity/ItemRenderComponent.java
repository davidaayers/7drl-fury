package com.wwflgames.fury.entity;

import com.wwflgames.fury.item.Item;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;

public class ItemRenderComponent extends CardRenderComponent {

    private Item item;
    private UnicodeFont font;

    public ItemRenderComponent(Item item, UnicodeFont font) {
        super(item.name());
        this.item = item;
        this.font = font;
    }

    @Override
    protected void maybeRenderItemText(Graphics g) {
        Vector2f pos = owner.getPosition();
        int width = 32 * 4;
        String itemName = item.name();
        int strWidth = font.getWidth(itemName);
        font.drawString(pos.x + (width / 2) - (strWidth / 2), pos.y + 4, itemName, Color.white);
    }

}
