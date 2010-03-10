package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.util.TextUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;

public class BattleState extends BasicGameState {

    private GameContainer container;
    private StateBasedGame game;
    private SpriteSheet heroSprites;
    private SpriteSheet dungeonTiles;
    private SpriteSheet monsterSprites;
    private UnicodeFont tfont;
    private Font font;

    @Override
    public int getID() {
        return Fury.BATTLE_STATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;

        heroSprites = new SpriteSheet("warrior.png", 24, 32);
        dungeonTiles = new SpriteSheet("dg_dungeon32.gif", 32, 32);
        monsterSprites = new SpriteSheet("horned_skelly.png",24,32);

        font = new Font("Verdana", Font.PLAIN, 12);
        tfont = new UnicodeFont(font);
        tfont.getEffects().add(new ColorEffect(java.awt.Color.white));
        tfont.addAsciiGlyphs();
        tfont.loadGlyphs();

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.white);
        TextUtil.centerText(container, g, "Battle Screen", 0);

        int TILE_WIDTH = 96;
        int TILE_HEIGHT = 96;
        int x = 256;
        int y = 96 + 32;
        // draw the dungeon floor under the mobs
        // first row
        dungeonTiles.getSprite(0, 0).draw(x, y, 3);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH, y, 3);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH * 2, y, 3);
        // second row
        dungeonTiles.getSprite(0, 0).draw(x, y + TILE_HEIGHT, 3);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH, y + TILE_HEIGHT, 3);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH * 2, y + TILE_HEIGHT, 3);
        // third row
        dungeonTiles.getSprite(0, 0).draw(x, y + TILE_HEIGHT * 2, 3);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH, y + TILE_HEIGHT * 2, 3);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH * 2, y + TILE_HEIGHT * 2, 3);

        if (false) {
            g.setColor(Color.red);
            g.drawRect(x, y, TILE_WIDTH * 3, TILE_HEIGHT * 3);
            g.drawLine(x, y + TILE_HEIGHT, x + TILE_WIDTH * 3, y + TILE_HEIGHT);
            g.drawLine(x, y + TILE_HEIGHT * 2, x + TILE_WIDTH * 3, y + TILE_HEIGHT * 2);
            g.drawLine(x + TILE_WIDTH, y, x + TILE_WIDTH, y + TILE_HEIGHT * 3);
            g.drawLine(x + TILE_WIDTH * 2, y, x + TILE_WIDTH * 2, y + TILE_HEIGHT * 3);
        }


        //g.drawImage(heroSprites.getSprite(1,2),100,100);
        heroSprites.getSprite(1, 2).draw(x + TILE_WIDTH + 12, y + TILE_HEIGHT, 3);
        monsterSprites.getSprite(1,2).draw(x + TILE_WIDTH + 12, y,3);
        monsterSprites.getSprite(1,2).draw(x + TILE_WIDTH*2 + 12, y+TILE_HEIGHT*2,3);

        // draw an item at the top
        g.setColor(Color.gray);
        g.fillRoundRect(x + TILE_WIDTH, y - 96 - 4, 96, 96, 15);
        g.fillRoundRect(x + TILE_WIDTH, y + TILE_HEIGHT*3 + 4, 96, 96, 15);

        g.fillRoundRect(x - TILE_WIDTH-4, y - 96 -4, 96, 96, 15);
        g.fillRoundRect(x - TILE_WIDTH-4, y + TILE_HEIGHT, 96, 96, 15);
        g.fillRoundRect(x - TILE_WIDTH-4, y + TILE_HEIGHT*3 + 4 , 96, 96, 15);

        g.fillRoundRect(x + TILE_WIDTH*3 +4, y -96-4, 96, 96, 15);
        g.fillRoundRect(x + TILE_WIDTH*3 +4, y + TILE_HEIGHT, 96, 96, 15);
        g.fillRoundRect(x + TILE_WIDTH*3 +4, y + TILE_HEIGHT*3+4, 96, 96, 15);

        g.setColor(Color.white);
        String itemName = "Spikey Mace";
        int width = tfont.getWidth(itemName);
        tfont.drawString(x + TILE_WIDTH + 48 - (width / 2), y - 96 - 4, itemName, Color.white);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }
}
