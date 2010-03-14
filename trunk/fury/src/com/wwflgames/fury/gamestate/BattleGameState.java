package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.battle.*;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.effect.Buff;
import com.wwflgames.fury.item.effect.Damage;
import com.wwflgames.fury.item.effect.Death;
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
import java.util.HashMap;
import java.util.List;

public class BattleGameState extends BasicGameState {

    enum State {
        PLAYER_CHOOSE_MONSTER,
        MONSTER_CHOSEN,
        ANIMATION_PLAY,
        ANIMATION_DONE,
        BATTLE_OVER;
    }

    ;

    enum ReplayState {
        CREATE_PLAYER_CARD,
        SHOW_PLAYER_DAMAGE,
        CREATE_MONSTER_CARD,
        SHOW_MONSTER_DAMAGE,
        WAIT
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
    private List<ItemLogMessage> playerEffects;
    private List<ItemLogMessage> monsterEffects;
    private List<Entity> cardsInPlay;
    private java.util.Map<Mob, Entity> mobEntities;
    private Entity playerEntity;

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
        playerEffects = new ArrayList<ItemLogMessage>();
        monsterEffects = new ArrayList<ItemLogMessage>();

        // cards in play
        cardsInPlay = new ArrayList<Entity>();

        mobEntities = new HashMap<Mob, Entity>();

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
                .addComponent(new BattleMapRenderer("mapRender", appState.getMap(), mapOffsetX, mapOffsetY));

        entityManager.addEntity(mapEntity);

        List<Mob> monsters = findMonsters(map, playerMapX, playerMapY);

        // finally, create entities for all of the monsters found, and the player, so
        // they can be rendered
        for (Mob monster : monsters) {
            Log.debug("monster x = " + monster.getMapX() + " , y = " + monster.getMapY());
            MobRenderer sprite = new MobRenderer(monster, monsterSprites);
            sprite.useSprite(1, 2);
            Entity mobEntity = createMobEntity(mapOffsetX, mapOffsetY, monster, sprite);
            entityManager.addEntity(mobEntity);
            mobEntities.put(monster, mobEntity);
        }

        MobRenderer heroSprite = new MobRenderer(player, heroSprites);
        heroSprite.useSprite(1, 2);

        playerEntity = createMobEntity(mapOffsetX, mapOffsetY, player, heroSprite);
        entityManager.addEntity(playerEntity);


        // Set up the battle
        //TODO: the "true" here is player initiative, it should be set somehow. For now,
        //we'll just always give the player initiative.
        battle = new Battle(player, monsters, true);
        battleSystem = new BattleSystem(battle);
        battleSystem.startBattle();

        currentState = State.PLAYER_CHOOSE_MONSTER;

        Log.debug("BattleGameState-> complete.");
    }

    private Entity createMobEntity(int mapOffsetX, int mapOffsetY, Mob mob, SpriteSheetRenderer sprite) {
        MobLocationAction mobLocationAction = new MobLocationAction(mob.name() + "location")
                .setMapOffset(mapOffsetX, mapOffsetY)
                .setScreenOffset(208, 32)
                .setMob(mob);

        Entity mobEntity = new Entity(mob.name() + "entity")
                .setScale(4)
                .addComponent(sprite)
                .addComponent(mobLocationAction);

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
        for (ItemLogMessage effectStr : playerEffects) {
            List<String> splitStr = maybeSplitString(effectStr.getString(), 180);
            for (String str : splitStr) {
                font.drawString(5, effectY, str, effectStr.getColor());
                effectY += 14;
            }
        }

        // render the monster's stuff
        int monEeffectY = 32 + 32 * scale + 42;
        for (ItemLogMessage effectStr : monsterEffects) {
            List<String> splitStr = maybeSplitString(effectStr.getString(), 208);
            for (String str : splitStr) {
                font.drawString((x + TILE_WIDTH * 3) + 5, monEeffectY, str, effectStr.getColor());
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

                List<Mob> deadMobs = new ArrayList<Mob>();
                // remove any monsters that were killed during combat
                for (Mob monster : mobEntities.keySet()) {
                    if (monster.isDead()) {
                        deadMobs.add(monster);
                    }
                }

                for (Mob monster : deadMobs) {
                    Entity mobEntity = mobEntities.remove(monster);
                    entityManager.removeEntity(mobEntity);
                    // remove it from the map too
                    appState.getMap().removeMob(monster);
                }

                if (battle.allEnemiesDead()) {
                    currentState = State.BATTLE_OVER;
                }

                currentState = State.PLAYER_CHOOSE_MONSTER;

            case BATTLE_OVER:

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
            greyOutItemMessages();
            currentState = State.ANIMATION_PLAY;
        } else {
            Log.debug("Monster was null or map was out of bounds, resetting state");
            currentState = State.PLAYER_CHOOSE_MONSTER;
        }
    }

    private void greyOutItemMessages() {
        for (ItemLogMessage str : playerEffects) {
            str.setColor(Color.darkGray);
        }
        for (ItemLogMessage str : monsterEffects) {
            str.setColor(Color.darkGray);
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
                Player player = appState.getPlayer();
                final Entity playerCard = new Entity("playerCard");
                ActionFinishedNotifier notifier = new ActionFinishedNotifier() {
                    @Override
                    public void actionComplete() {
                        playerCard.removeComponentById("moveTo");
                        changeReplayState(ReplayState.SHOW_PLAYER_DAMAGE);
                    }
                };
                createCard(playerCard, playerEntity, lastResult.getItemUsageResultFor(player).item(), 42, 64, notifier);
                //playerCard.addComponent(new DisplayForTimeAction("disp3sec", 3000));
                entityManager.addEntity(playerCard);
                cardsInPlay.add(playerCard);
                changeReplayState(ReplayState.WAIT);
                break;

            case SHOW_PLAYER_DAMAGE:
                Log.debug("SHOW_PLAYER_DAMAGE");
                // add the player's effects to the damage stack
                ItemUsageResult result = lastResult.getItemUsageResultFor(appState.getPlayer());
                for (ItemEffectResult effectResult : result.get()) {
                    playerEffects.add(0, createDesc(effectResult));
                }

                playerEffects.add(0, createItemUsedString(result));

                changeReplayState(ReplayState.CREATE_MONSTER_CARD);
                break;

            case CREATE_MONSTER_CARD:
                Log.debug("SHOW_MONSTER_CARD");
                changeReplayState(ReplayState.SHOW_MONSTER_DAMAGE);
                break;

            case SHOW_MONSTER_DAMAGE:
                Log.debug("SHOW_MONSTER_DAMAGE");
                for (Monster monster : lastResult.getMonsters()) {
                    ItemUsageResult monsterResult = lastResult.getItemUsageResultFor(monster);
                    for (ItemEffectResult effectResult : monsterResult.get()) {
                        monsterEffects.add(0, createDesc(effectResult));
                    }
                    monsterEffects.add(0, createItemUsedString(monsterResult));
                }
                currentState = State.ANIMATION_DONE;
                break;

            case WAIT:
                // do nothing
                Log.debug("Waiting...");
                break;
        }
    }

    private ItemLogMessage createItemUsedString(ItemUsageResult result) {
        String string = result.mob().name() + " uses " + result.item().name();
        return new ItemLogMessage(string, Color.white);
    }

    private ItemLogMessage createDesc(ItemEffectResult effectResult) {
        String s0 = effectResult.getEffectedMob().possessiveName();
        String s1 = effectResult.getEffectedMob().name();
        String s2 = "";
        if (effectResult.getDelta() != null) {
            s2 = effectResult.getDelta().toString();
        }
        String string = MessageFormat.format(effectResult.getDesc(), new Object[]{s0, s1, s2});
        ItemLogMessage str = new ItemLogMessage(string, determineColor(effectResult));
        return str;
    }

    private Color determineColor(ItemEffectResult effectResult) {
        if (effectResult.getEffect() instanceof Damage) {
            return Color.red;
        }
        if (effectResult.getEffect() instanceof Buff) {
            return Color.green;
        }
        if (effectResult.getEffect() instanceof Death) {
            return Color.yellow;
        }
        return Color.white;
    }

    private void changeReplayState(ReplayState newState) {
        stateBag.clearAll();
        replayState = newState;
    }

    private Entity createCard(Entity cardEntity, Entity mobEntity, Item item, float x, float y, ActionFinishedNotifier notifier) {
        ItemRenderer card = new ItemRenderer(item, font);

        MoveToAction action = new MoveToAction("moveTo", x, y, .5f, notifier);
        Vector2f mobPos = mobEntity.getPosition();
        cardEntity.addComponent(card)
                .addComponent(action)
                .setPosition(new Vector2f(mobPos.x, mobPos.y));
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

    private class ItemLogMessage {
        private String string;
        private Color color;

        private ItemLogMessage(String string, Color color) {
            this.string = string;
            this.color = color;
        }

        public String getString() {
            return string;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

}
