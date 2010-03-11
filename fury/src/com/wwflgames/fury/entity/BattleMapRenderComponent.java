package com.wwflgames.fury.entity;

import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.map.TileType;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class BattleMapRenderComponent extends RenderComponent {

    private SpriteSheet spriteSheet;
    private Map map;
    private int renderX;
    private int renderY;

    public BattleMapRenderComponent(String id, Map map, int renderX, int renderY)
            throws SlickException {
        super(id);
        this.map = map;
        this.renderX = renderX;
        this.renderY = renderY;
        this.spriteSheet = new SpriteSheet("dg_dungeon32.gif", 32, 32);
    }

    @Override
    public void render(Graphics gr) {
        // starting at renderX, render Y, we're going to render 9 squares of the map
        // if any of the squares would be out of bounds, don't draw them
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        Image floorImage = spriteSheet.getSprite(0,8);
        Image wallImage = spriteSheet.getSprite(0,0);

        int tw = floorImage.getWidth();
        int th = floorImage.getHeight();

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int mapX = renderX+x;
                int mapY = renderY+y;
                if ( map.inBounds(mapX,mapY)) {
                    TileType tileType = map.getTileFor(mapX,mapY);
                    Image drawImage = floorImage;
                    if ( tileType == TileType.WALL ) {
                        drawImage = wallImage;
                    }

                    drawImage.draw(pos.x+(x*tw*scale),pos.y+(y*th*scale),scale);
                }
            }
        }
    }

    @Override
    public void update(int delta) {
        // nothing to do here
    }
}
