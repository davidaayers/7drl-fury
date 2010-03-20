package com.wwflgames.fury.entity;

import com.wwflgames.fury.gamestate.PlayerController;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class DungeonMapRenderer extends MapRenderer {

    private PlayerController playerController;

    public DungeonMapRenderer(String id, DungeonMap dungeonMap, PlayerController playerController) throws SlickException {
        super(id, dungeonMap);
        this.playerController = playerController;
    }

    @Override
    public void render(Graphics gr) {
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        for (int y = 0; y < dungeonMap.getHeight(); y++) {
            for (int x = 0; x < dungeonMap.getWidth(); x++) {
                int mapx = x + playerController.getOffsetX();
                int mapy = y + playerController.getOffsetY();
                if (mapx > dungeonMap.getWidth() - 1) {
                    mapx = dungeonMap.getWidth() - 1;
                }
                if (mapx < 0) {
                    mapx = 0;
                }
                if (mapy > dungeonMap.getHeight() - 1) {
                    mapy = dungeonMap.getHeight() - 1;
                }
                if (mapy < 0) {
                    mapy = 0;
                }
                Tile mapTile = dungeonMap.getTileAt(mapx, mapy);
                Image drawImage = determineImageForTile(mapTile.getType());
                int tw = drawImage.getWidth();
                int th = drawImage.getHeight();
                drawImage.draw(pos.x + (x * tw * scale), pos.y + (y * th * scale), scale);
            }
        }
    }

    @Override
    public void update(int delta) {
        // based on the player's location on the map, determine our offsets


    }
}
