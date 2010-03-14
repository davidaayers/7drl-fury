package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.battle.*;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.item.Item;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BattleGameState extends BasicGameState {

    enum State {
        PLAYER_CHOOSE_MONSTER,
        MONSTER_CHOSEN,
        ANIMATION_PLAY,
        ANIMATION_DONE
    }

    ;

    enum ReplayState {
        CREATE_PLAYER_CARD,
        SHOW_PLAYER_DAMAGE,
        CREATE_MONSTER_CARD,
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
    private boolean lastAnimationComplete;
    private StateBag stateBag;
    private List<String> playerEffects;
    private List<String> monsterEffects;
    private List<Entity> cardsInPlay;

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

        // create a new state bag
        stateBag = new StateBag();

        // damage stacks
        playerEffects = new Stack<String>();
        monsterEffects = new Stack<String>();

        // cards in play
        cardsInPlay = new ArrayList<Entity>();

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
            MobRenderComponent sprite = new MobRenderComponent(monster, monsterSprites);
            sprite.useSprite(1, 2);
            Entity mobEntity = createMobEntity(mapOffsetX, mapOffsetY, monster, sprite);
            entityManager.addEntity(mobEntity);
        }

        MobRenderComponent heroSprite = new MobRenderComponent(player, heroSprites);
        heroSprite.useSprite(1, 2);

        Entity playerEntity = createMobEntity(mapOffsetX, mapOffsetY, player, heroSprite);
        entityManager.addEntity(playerEntity);

        // Set up the battle
        //TODO: the "true" here is player initiative, it should be set somehow. For now,
        //we'll just always give the player initiative.
        battle = new Battle(player, monsters, true);
        battleSystem = new BattleSystem(battle);
        battleSystem.startBattle();

        currentState = State.PLAYER_CHOOSE_MONSTER;

        //TODO: remove this?
        //createFaceDownItemDecks();

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
            g.setColor(Color.green);
            String text = "Battle Round " + battleSystem.getBattleRound() + ", choose Monster to attack";
            TextUtil.centerText(container, g, text, 416);
        }

        g.setColor(Color.white);

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

        // render the player's stuff
        int effectY = 32 + 32 * scale + 42;
        for (String effectStr : playerEffects) {
            List<String> splitStr = maybeSplitString(effectStr, 200);
            for (String str : splitStr) {
                font.drawString(5, effectY, str, Color.white);
                effectY += 14;
            }
        }

        // render the monster's stuff
        int monEeffectY = 32 + 32 * scale + 42;
        for (String effectStr : monsterEffects) {
            List<String> splitStr = maybeSplitString(effectStr, 208);
            for (String str : splitStr) {
                font.drawString((x + TILE_WIDTH * 3) + 5, monEeffectY, str, Color.white);
                monEeffectY += 14;
            }
        }

        String monster = "Menacing Skeleton";
        width = font.getWidth(monster);
        int mw = 800 - (x + TILE_WIDTH * 3);
        int mx = (x + TILE_WIDTH * 3) + mw / 2 - width / 2;
        font.drawString(mx, y, monster, Color.white);
    }

    private List<String> maybeSplitString(String effectStr, int maxWidth) {

        String[] parts = effectStr.split(" ");
        List<String> splitString = new ArrayList<String>();
        int width = 0;
        String current = "";
        for (String part : parts) {
            width += font.getWidth(part + " ");
            if (width < maxWidth) {
                current = current + part + " ";
            } else {
                splitString.add(current);
                width = 0;
                current = part + " ";
            }
        }
        splitString.add(current);

        return splitString;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        entityManager.update(delta);

        switch (currentState) {
            case MONSTER_CHOSEN:
                handleMonsterChosen();
                break;
            case ANIMATION_PLAY:
                handleAnimation(delta);
                break;
            case ANIMATION_DONE:
                Log.debug("ANIMATION_DONE");
                currentState = State.PLAYER_CHOOSE_MONSTER;
        }
    }

    private void handleMonsterChosen() {
        Log.debug("MONSTER_CHOSEN");
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
            replayState = ReplayState.CREATE_PLAYER_CARD;
            // clear out all of the cards in play
            clearCardsInPlay();
            currentState = State.ANIMATION_PLAY;
        } else {
            Log.debug("Monster was null or map was out of bounds, resetting state");
            currentState = State.PLAYER_CHOOSE_MONSTER;
        }
    }

    private void clearCardsInPlay() {

        // remove them all from the entity manager so they stop
        // rendering
        for (Entity entity : cardsInPlay) {
            entityManager.removeEntity(entity);
        }

        cardsInPlay.clear();
    }

    private void handleAnimation(int delta) {
        switch (replayState) {

            case CREATE_PLAYER_CARD:
                Log.debug("CREATE_PLAYER_CARD");
                Entity playerCard = createCard(lastResult.getItemUsedBy(appState.getPlayer()), 42, 64);
                //playerCard.addComponent(new DisplayForTimeComponent("disp3sec", 3000));
                entityManager.addEntity(playerCard);
                cardsInPlay.add(playerCard);
                changeReplayState(ReplayState.SHOW_PLAYER_DAMAGE);
                break;

            case SHOW_PLAYER_DAMAGE:
                Log.debug("SHOW_PLAYER_DAMAGE");
                // add the player's effects to the damage stack
                for (BattleEffectBag bag : lastResult.playerItemEffectList()) {
                    for (ItemEffect effect : bag.get()) {
                        playerEffects.add(0, createDesc(bag, effect));
                    }
                }
                changeReplayState(ReplayState.CREATE_MONSTER_CARD);
                break;


            case CREATE_MONSTER_CARD:
                Log.debug("SHOW_MONSTER_CARD");
                changeReplayState(ReplayState.SHOW_MONSTER_DAMAGE);
                break;

            case SHOW_MONSTER_DAMAGE:
                Log.debug("SHOW_MONSTER_DAMAGE");
                for (BattleEffectBag bag : lastResult.monsterItemEffectList()) {
                    for (ItemEffect effect : bag.get()) {
                        monsterEffects.add(0, createDesc(bag, effect));
                    }
                }
                currentState = State.ANIMATION_DONE;
                break;
        }
    }

    private String createDesc(BattleEffectBag bag, ItemEffect effect) {
        String s0 = bag.mob().name() + "'s";
        String s1 = bag.mob().name();
        String s2 = effect.getDelta().toString();
        return MessageFormat.format(effect.getDesc(), new Object[]{s0, s1, s2});
    }

    private void changeReplayState(ReplayState newState) {
        stateBag.clearAll();
        replayState = newState;
    }

    private Entity createCard(Item item, float x, float y) {
        ItemRenderComponent card = new ItemRenderComponent(item, font);
        Entity cardEntity = new Entity("playerCard")
                .addComponent(card)
                .setPosition(new Vector2f(x, y));
        return cardEntity;
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
