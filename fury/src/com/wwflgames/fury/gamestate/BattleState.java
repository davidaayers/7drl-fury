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
        monsterSprites = new SpriteSheet("horned_skelly.png", 24, 32);

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

        int scale = 4;

        int TILE_WIDTH = 32 * scale;
        int TILE_HEIGHT = 32 * scale;
        int x = 400 - ((TILE_WIDTH * 3) / 2);
        int y = 32;
        // draw the dungeon floor under the mobs
        // first row
        dungeonTiles.getSprite(0, 0).draw(x, y, scale);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH, y, scale);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH * 2, y, scale);
        // second row
        dungeonTiles.getSprite(0, 0).draw(x, y + TILE_HEIGHT, scale);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH, y + TILE_HEIGHT, scale);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH * 2, y + TILE_HEIGHT, scale);
        // third row
        dungeonTiles.getSprite(0, 0).draw(x, y + TILE_HEIGHT * 2, scale);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH, y + TILE_HEIGHT * 2, scale);
        dungeonTiles.getSprite(0, 8).draw(x + TILE_WIDTH * 2, y + TILE_HEIGHT * 2, scale);

        if (false) {
            g.setColor(Color.red);
            g.drawRect(x, y, TILE_WIDTH * 3, TILE_HEIGHT * 3);
            g.drawLine(x, y + TILE_HEIGHT, x + TILE_WIDTH * 3, y + TILE_HEIGHT);
            g.drawLine(x, y + TILE_HEIGHT * 2, x + TILE_WIDTH * 3, y + TILE_HEIGHT * 2);
            g.drawLine(x + TILE_WIDTH, y, x + TILE_WIDTH, y + TILE_HEIGHT * 3);
            g.drawLine(x + TILE_WIDTH * 2, y, x + TILE_WIDTH * 2, y + TILE_HEIGHT * 3);
        }


        //g.drawImage(heroSprites.getSprite(1,2),100,100);
        heroSprites.getSprite(1, 2).draw(x + TILE_WIDTH + 4 * scale, y + TILE_HEIGHT, scale);
        monsterSprites.getSprite(1, 2).draw(x + TILE_WIDTH + 4 * scale, y, scale);
        monsterSprites.getSprite(1, 2).draw(x + TILE_WIDTH * 2 + 4 * scale, y + TILE_HEIGHT * 2, scale);

        // draw an item at the top
        if (false) {
            g.setColor(Color.gray);
            g.fillRoundRect(x + TILE_WIDTH, y - 96 - 4, 96, 96, 15);
            g.fillRoundRect(x + TILE_WIDTH, y + TILE_HEIGHT * 3 + 4, 96, 96, 15);

            g.fillRoundRect(x - TILE_WIDTH - 4, y - 96 - 4, 96, 96, 15);
            g.fillRoundRect(x - TILE_WIDTH - 4, y + TILE_HEIGHT, 96, 96, 15);
            g.fillRoundRect(x - TILE_WIDTH - 4, y + TILE_HEIGHT * 3 + 4, 96, 96, 15);

            g.fillRoundRect(x + TILE_WIDTH * 3 + 4, y - 96 - 4, 96, 96, 15);
            g.fillRoundRect(x + TILE_WIDTH * 3 + 4, y + TILE_HEIGHT, 96, 96, 15);
            g.fillRoundRect(x + TILE_WIDTH * 3 + 4, y + TILE_HEIGHT * 3 + 4, 96, 96, 15);

            g.setColor(Color.white);
            String itemName = "Spikey Mace";
            int width = tfont.getWidth(itemName);
            tfont.drawString(x + TILE_WIDTH + 48 - (width / 2), y - 96 - 4, itemName, Color.white);
        }

        String player = "Player: Warrior";
        int width = tfont.getWidth(player);
        tfont.drawString(x/2-width/2, y , player, Color.white);

        // draw a "deck" at the top left
        g.setColor(Color.gray);
        int deckOffset = 20;
        int dx = 10;
        int dy = y + 32;
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        dx += deckOffset;
        g.setColor(Color.gray);
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        dx += deckOffset;
        g.setColor(Color.gray);
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        dx += deckOffset;
        g.setColor(Color.gray);
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);

        String monster = "Menacing Skeleton";
        width = tfont.getWidth(monster);
        int mw = 800-(x+TILE_WIDTH*3);
        System.out.println("mw = " + mw );
        int mx = (x+TILE_WIDTH*3) + mw/2 - width/2;
        tfont.drawString(mx, y , monster, Color.white);


        // draw a "deck" at the top right
        g.setColor(Color.darkGray);
        dx = 800 - TILE_WIDTH - 10;
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        dx -= deckOffset;
        g.setColor(Color.darkGray);
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        dx -= deckOffset;
        g.setColor(Color.darkGray);
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        dx -= deckOffset;
        g.setColor(Color.darkGray);
        g.fillRoundRect(dx, dy, 32 * scale, 32 * scale, 15);
        g.setColor(Color.white);
        g.drawRoundRect(dx, dy, 32 * scale, 32 * scale, 15);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }
}
