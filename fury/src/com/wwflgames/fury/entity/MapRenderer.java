package com.wwflgames.fury.entity;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.TileType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public abstract class MapRenderer extends Renderer {

    protected SpriteSheet spriteSheet;
    protected DungeonMap dungeonMap;
    private Image empty;

    public MapRenderer(String id, DungeonMap dungeonMap) throws SlickException {
        super(id);
        this.spriteSheet = new SpriteSheet("dg_dungeon32.gif", 32, 32);
        this.dungeonMap = dungeonMap;

        // create an empty, black square for rendering "EMPTY" tiles
        empty = new Image(32,32);
        Graphics g = empty.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0,0,32,32);
        g.flush();

    }

    protected Image determineImageForTile(TileType tileType) {
        Image floorImage = spriteSheet.getSprite(0, 8);
        Image wallImage = spriteSheet.getSprite(0, 0);
        Image drawImage = floorImage;
        if (tileType == TileType.WALL) {
            drawImage = wallImage;
        } else if ( tileType == TileType.EMPTY ) {
            drawImage = empty;
        }
        return drawImage;
    }

}
