package com.wwflgames.fury.entity;

import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.map.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class DungeonMapRenderer extends MapRenderer {

    public DungeonMapRenderer(String id, Map map) throws SlickException {
        super(id, map);
    }

    @Override
    public void render(Graphics gr) {
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Tile mapTile = map.getTileAt(x, y);
                Image drawImage = determineImageForTile(mapTile.getType());
                int tw = drawImage.getWidth();
                int th = drawImage.getHeight();
                drawImage.draw(pos.x + (x * tw * scale), pos.y + (y * th * scale), scale);
            }
        }
    }

    @Override
    public void update(int delta) {
        // nothing to do
    }
}
