package com.wwflgames.fury.entity;

import com.wwflgames.fury.gamestate.PlayerController;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class MapFogOfWarRenderer extends AbstractDungeonMapRenderer {

    public MapFogOfWarRenderer(String id, DungeonMap dungeonMap, PlayerController playerController)
            throws SlickException {
        super(id, dungeonMap, playerController);
    }

    @Override
    protected void doRender(int x, int y, Tile mapTile, Graphics graphics) {
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        float tw = 32 * scale;
        float th = 32 * scale;
        float drawX = pos.x + (x * tw * scale);
        float drawY = pos.y + (y * th * scale);

        int visibility = mapTile.getPlayerVisibility();

        float alpha = 1f;

        switch (visibility) {
            case 1:
                alpha = .50f;
                break;
            case 2:
                alpha = .40f;
                break;
            case 3:
                alpha = .30f;
                break;
            case 4:
                alpha = .20f;
                break;
            case 5:
            case 6:
            case 7:
                alpha = 0f;
                break;
        }

        // if the player has "seen" a square, it always gets rendered at the lowest alpha, so the parts
        // of the map the player has visited are always visible (i.e. automapping)
        if (mapTile.hasPlayerSeen() && alpha > .50f) {
            alpha = .50f;
        }

        Color saveColor = graphics.getColor();
        Color black = new Color(Color.black.r, Color.black.g, Color.black.b, alpha);
        graphics.setColor(black);
        graphics.fillRect(drawX, drawY, tw, th);
        graphics.setColor(saveColor);

    }
}
