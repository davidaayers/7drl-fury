package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.battle.Battle;
import com.wwflgames.fury.battle.BattleSystem;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.TextUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class BattleGameState extends BasicGameState {

    private GameContainer container;
    private StateBasedGame game;
    private SpriteSheet heroSprites;
    private SpriteSheet monsterSprites;
    private UnicodeFont font;
    private AppState appState;
    private Battle battle;
    private BattleSystem battleSystem;
    private EntityManager entityManager;

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

        entityManager = new EntityManager(container, game);

    }

    // called when this state is entered. Here's where we'll setup our battle 
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        Log.debug("BattleGameState-> entered.");

        // grab the player
        Player player = appState.getPlayer();
        Map map = appState.getMap();

        int playerMapX = player.getMapX();
        int playerMapY = player.getMapY();

        int mapOffsetX = playerMapX - 1;
        int mapOffsetY = playerMapY - 1;

        Entity mapEntity = new Entity("map")
                .setPosition(new Vector2f(208, 32))
                .setScale(4)
                .addComponent(new BattleMapRenderComponent("mapRender", appState.getMap(), mapOffsetX, mapOffsetY));

        entityManager.addEntity(mapEntity);

        List<Mob> monsters = findMonsters(map, playerMapX, playerMapY);

        // finally, create entities for all of the monsters found, and the player, so
        // they can be rendered
        for (Mob monster : monsters) {
            Log.debug("monster x = " + monster.getMapX() + " , y = " + monster.getMapY());
            SpriteSheetRenderComponent sprite = new SpriteSheetRenderComponent(monster.name() + "sprite", monsterSprites)
                    .useSprite(1, 2);
            Entity mobEntity = createMobEntity(mapOffsetX, mapOffsetY, monster, sprite);
            entityManager.addEntity(mobEntity);
        }

        SpriteSheetRenderComponent heroSprite = new SpriteSheetRenderComponent(player.name() + "sprite", heroSprites)
                .useSprite(1, 2);

        Entity playerEntity = createMobEntity(mapOffsetX, mapOffsetY, player, heroSprite);
        entityManager.addEntity(playerEntity);

        // Set up the battle
        //TODO: the "true" here is player initiative, it should be set somehow. For now,
        //we'll just always give the player initiative.
        battle = new Battle(player, monsters, true);
        battleSystem = new BattleSystem(battle);

        Log.debug("BattleGameState-> complete.");
    }

    private Entity createMobEntity(int mapOffsetX, int mapOffsetY, Mob mob, SpriteSheetRenderComponent sprite) {
        MobLocationComponent mobLocationComponent = new MobLocationComponent(mob.name() + "location")
                .setMapOffset(mapOffsetX, mapOffsetY)
                .setScreenOffset(208, 32)
                .setMob(mob);

        Entity mobEntity = new Entity(mob.name() + "entity")
                .setScale(4)
                .addComponent(sprite)
                .addComponent(mobLocationComponent);

        return mobEntity;
    }

    // package private for testing
    List<Mob> findMonsters(Map map, int playerX, int playerY) {
        List<Mob> monsters = new ArrayList<Mob>();
        // look at all of the 8 squares around the player
        // and see if there are monsters there
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                int mx = playerX + x;
                int my = playerY + y;
                if (map.inBounds(mx, my)) {
                    Mob mob = map.getTileAt(playerX + x, playerY + y).getMob();
                    if (mob != null && mob instanceof Monster) {
                        monsters.add(mob);
                    }
                }
            }
        }
        return monsters;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.white);
        TextUtil.centerText(container, g, "Battle Screen", 0);

        entityManager.render(g);

        /////////////////////////////////////////////////////////////////////
        // Move all this stuff to entities
        /////////////////////////////////////////////////////////////////////

        int scale = 4;

        int TILE_WIDTH = 32 * scale;
        int TILE_HEIGHT = 32 * scale;
        int x = 400 - ((TILE_WIDTH * 3) / 2);
        int y = 32;

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
        entityManager.update(delta);
    }
}
