package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.BattleMapRenderComponent;
import com.wwflgames.fury.entity.Entity;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.util.TextUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;

public class BattleGameState extends BasicGameState {

    private GameContainer container;
    private StateBasedGame game;
    private SpriteSheet heroSprites;
    private SpriteSheet monsterSprites;
    private UnicodeFont font;
    private Entity mapEntity;
    private AppState appState;

    public BattleGameState(AppState appState) {
        this.appState = appState;
    }

    @Override
    public int getID() {
        return Fury.BATTLE_STATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        
        this.container = container;
        this.game = game;

        heroSprites = new SpriteSheet("warrior.png", 24, 32);
        monsterSprites = new SpriteSheet("horned_skelly.png", 24, 32);

        Font jFont = new Font("Verdana", Font.PLAIN, 12);
        font = new UnicodeFont(jFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        font.addAsciiGlyphs();
        font.loadGlyphs();

        mapEntity = new Entity("map", container, game)
                .setPosition(new Vector2f(208, 32))
                .setScale(4)
                .addComponent(new BattleMapRenderComponent("mapRender", appState.getMap(), 1, 0));
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

        mapEntity.render(g);

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
            int width = font.getWidth(itemName);
            font.drawString(x + TILE_WIDTH + 48 - (width / 2), y - 96 - 4, itemName, Color.white);
        }

        String player = appState.getPlayer().name();
        int width = font.getWidth(player);
        font.drawString(x / 2 - width / 2, y, player, Color.white);

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
        width = font.getWidth(monster);
        int mw = 800 - (x + TILE_WIDTH * 3);
        int mx = (x + TILE_WIDTH * 3) + mw / 2 - width / 2;
        font.drawString(mx, y, monster, Color.white);


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
