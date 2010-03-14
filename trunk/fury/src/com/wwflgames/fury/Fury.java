package com.wwflgames.fury;

import com.wwflgames.fury.entity.SpriteSheetCache;
import com.wwflgames.fury.gamestate.BattleGameState;
import com.wwflgames.fury.gamestate.DungeonGameState;
import com.wwflgames.fury.gamestate.TitleGameState;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.item.effect.Damage;
import com.wwflgames.fury.item.effect.DamageType;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.main.AppStateImpl;
import com.wwflgames.fury.map.FixedMapCreator;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.monster.MonsterFactory;
import com.wwflgames.fury.player.ProfessionFactory;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

public class Fury extends StateBasedGame {

    public static final int TITLE_STATE = 1;
    public static final int DUNGEON_GAME_STATE = 2;
    public static final int BATTLE_STATE = 3;

    private static AppGameContainer container;

    private AppStateImpl appState;
    private SpriteSheetCache spriteSheetCache;
    private MonsterFactory monsterFactory;
    private ProfessionFactory professionFactory;
    private ItemFactory itemFactory;

    public Fury() {
        super("Fury - 7DRL");
        initAppState();
    }

    private void initAppState() {
        appState = new AppStateImpl();

        //TODO: move map creation somewhere else, later
        appState.setMap(new FixedMapCreator().createMap());
    }

    private ItemDeck createDeck(int dmg) throws SlickException {
        Damage crushDamage = new Damage(DamageType.MELEE_CRUSH, dmg);
        Item mace = itemFactory.createItemWithUsedAgainstEffects("Mace of crushing", new ItemEffect[]{crushDamage});
        ItemDeck deck = new ItemDeck();
        deck.addItem(mace);
        return deck;
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        // this is cheesy, but this is the only place we can hook into the init of game
        // and do anything.
        initItemFactory();
        initMonsterFactory();
        initPlayerClassFactory();
        initSpriteSheetCache();
        // TEMP
        initMonsters();

        installGameStates();
    }

    private void initItemFactory() throws SlickException {
        itemFactory = new ItemFactory();
    }

    private void installGameStates() {
        addState(createTitleGameState());
        addState(createDungeonState());
        addState(createBattleState());
    }

    private void initPlayerClassFactory() throws SlickException {
        professionFactory = new ProfessionFactory();
    }

    private void initMonsters() throws SlickException {
        // an an enemy
        Monster monster = monsterFactory.createMonster(0);
//        monster.setStatValue(Stat.HEALTH, 10);
//        monster.setDeck(createDeck(1));
        appState.getMap().addMob(monster, 2, 1);

        Monster monster2 = monsterFactory.createMonster(0);
//        monster2.setStatValue(Stat.HEALTH, 10);
//        monster2.setDeck(createDeck(1));
        appState.getMap().addMob(monster2, 2, 2);

        Monster monster3 = monsterFactory.createMonster(0);
//        monster3.setStatValue(Stat.HEALTH, 10);
//        monster3.setDeck(createDeck(3));
        appState.getMap().addMob(monster3, 3, 3);
    }

    private void initMonsterFactory() throws SlickException {
        monsterFactory = new MonsterFactory(itemFactory);
    }

    private void initSpriteSheetCache() throws SlickException {
        spriteSheetCache = new SpriteSheetCache();
        for (String spriteSheetName : monsterFactory.getAllSpriteSheetNames()) {
            spriteSheetCache.loadSprite(spriteSheetName);
        }
        for (String spriteSheetName : professionFactory.getAllSpriteSheetNames()) {
            spriteSheetCache.loadSprite(spriteSheetName);
        }
    }

    private TitleGameState createTitleGameState() {
        return new TitleGameState(professionFactory, spriteSheetCache, appState);
    }

    private GameState createDungeonState() {
        return new DungeonGameState(appState, spriteSheetCache);
    }

    private BattleGameState createBattleState() {
        return new BattleGameState(appState, spriteSheetCache);
    }

    public static void main(String[] args) {
        ResourceLoader.removeAllResourceLocations();
        ResourceLoader.addResourceLocation(new ClasspathLocation());

        try {
            container = new AppGameContainer(new Fury());
            container.setDisplayMode(800, 600, false);
            container.setShowFPS(true);
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }


}
