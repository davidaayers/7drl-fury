package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.battle.Battle;
import com.wwflgames.fury.battle.BattleRoundResult;
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

    enum State {
        PLAYER_CHOOSE_MONSTER,
        MONSTER_CHOSEN,
        ROUND_FINISHED
    }

    ;

    enum ReplayState {
        SHOW_PLAYER_CARD,
        SHOW_PLAYER_DAMAGE,
        SHOW_MONSTER_CARD,
        SHOW_MONSTER_DAMAGE
    }

    ;

    private GameContainer container;
    private StateBasedGame game;
    private SpriteSheet heroSprites;
    private SpriteSheet monsterSprites;
    private UnicodeFont font;
    private AppState appState;
    private Battle battle;
    private BattleSystem battleSystem;
    private EntityManager entityManager;
    private int attackX;
    private int attackY;
    private State currentState;
    private ReplayState replayState;
    private BattleRoundResult lastResult;

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
        battleSystem.startBattle();

        currentState = State.PLAYER_CHOOSE_MONSTER;

        createFaceDownItemDecks();

        Log.debug("BattleGameState-> complete.");
    }

    private void createFaceDownItemDecks() {
        int deckOffset = 20;
        int dx = 10;
        int dy = 64;

        int dx2 = 800 - (32 * 4) - 10;

        for (int cnt = 0; cnt < 4; cnt++) {
            Entity cardEntity = createBlankCard(dx, dy, cnt);
            entityManager.addEntity(cardEntity);
            dx += deckOffset;
            Entity cardEntity2 = createBlankCard(dx2, dy, cnt);
            entityManager.addEntity(cardEntity2);
            dx2 -= deckOffset;
        }

    }

    private Entity createBlankCard(int dx, int dy, int cnt) {
        CardRenderComponent card = new CardRenderComponent("cardrender" + cnt);
        Entity cardEntity = new Entity("card" + cnt)
                .addComponent(card)
                .setPosition(new Vector2f(dx, dy));
        return cardEntity;
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

        if (currentState == State.PLAYER_CHOOSE_MONSTER) {
            String text = "Battle Round " + battleSystem.getBattleRound() + ", choose Monster to attack";
            TextUtil.centerText(container, g, text, 416);
        }

        /////////////////////////////////////////////////////////////////////
        // Not sure how to do the word stuff yet -- entities? or just draw em
        /////////////////////////////////////////////////////////////////////

        int scale = 4;
        int TILE_WIDTH = 32 * scale;
        int x = 400 - ((TILE_WIDTH * 3) / 2);
        int y = 32;

        String player = appState.getPlayer().name();
        int width = font.getWidth(player);
        font.drawString(x / 2 - width / 2, y, player, Color.white);

        String monster = "Menacing Skeleton";
        width = font.getWidth(monster);
        int mw = 800 - (x + TILE_WIDTH * 3);
        int mx = (x + TILE_WIDTH * 3) + mw / 2 - width / 2;
        font.drawString(mx, y, monster, Color.white);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        entityManager.update(delta);

        switch (currentState) {
            case MONSTER_CHOSEN:
                Player player = appState.getPlayer();
                Map map = appState.getMap();
                int monsterX = player.getMapX() + attackX;
                int monsterY = player.getMapY() + attackY;
                Monster monster = null;
                if (map.inBounds(monsterX, monsterY)) {
                    monster = (Monster) map.getTileAt(monsterX, monsterY).getMob();
                }
                if (monster != null) {
                    lastResult = battleSystem.performBattleRound(monster);
                    currentState = State.ROUND_FINISHED;
                } else {
                    Log.debug("Monster was null or map was out of bounds, resetting state");
                    currentState = State.PLAYER_CHOOSE_MONSTER;
                }
                break;
            case ROUND_FINISHED:
                Log.debug("here is where we'd show the previous round animations");
                // after we're done updating the UI with the battle round info,
                // 
                currentState = State.PLAYER_CHOOSE_MONSTER;
                break;
        }
    }

    @Override
    public void keyPressed(int key, char c) {

        if (currentState != State.PLAYER_CHOOSE_MONSTER) {
            Log.debug("Key pressed when it's not time to press keys!");
            return;
        }

        Log.debug("key = " + key);
        // 7 key - NW
        if (key == 71) {
            attackX = -1;
            attackY = -1;
            currentState = State.MONSTER_CHOSEN;
        }

        // 8 key - N
        if (key == 72) {
            attackX = 0;
            attackY = -1;
            currentState = State.MONSTER_CHOSEN;
        }

        // 9 key - NE
        if (key == 73) {
            attackX = 1;
            attackY = -1;
            currentState = State.MONSTER_CHOSEN;
        }

        // 4 key - W
        if (key == 75) {
            attackX = -1;
            attackY = 0;
            currentState = State.MONSTER_CHOSEN;
        }

        // 6 key - E
        if (key == 77) {
            attackX = 1;
            attackY = 0;
            currentState = State.MONSTER_CHOSEN;
        }

        // 1 key - SW
        if (key == 79) {
            attackX = -1;
            attackY = 1;
            currentState = State.MONSTER_CHOSEN;
        }

        // 2 key - S
        if (key == 80) {
            attackX = 0;
            attackY = 1;
            currentState = State.MONSTER_CHOSEN;
        }

        // 3 key - SE
        if (key == 81) {
            attackX = 1;
            attackY = 1;
            currentState = State.MONSTER_CHOSEN;
        }
    }
}
