package com.wwflgames.fury;

import com.wwflgames.fury.entity.SpriteSheetCache;
import com.wwflgames.fury.gamestate.BattleGameState;
import com.wwflgames.fury.gamestate.DungeonGameState;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.item.effect.*;
import com.wwflgames.fury.main.AppStateImpl;
import com.wwflgames.fury.map.FixedMapCreator;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.monster.MonsterFactory;
import com.wwflgames.fury.player.Player;
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

    public Fury() {
        super("Fury - 7DRL");
        initAppState();
    }

    private void initAppState() {
        appState = new AppStateImpl();

        //TODO: move map creation somewhere else, later
        appState.setMap(new FixedMapCreator().createMap());

        //TODO: move player creation somewhere else, too
        appState.setPlayer(createPlayer());
    }

    private ItemDeck createDeck(int dmg) {
        Damage crushDamage = new Damage(DamageType.MELEE_CRUSH, dmg);
        Item mace = factory().createItemWithUsedAgainstEffects("Mace of crushing", new ItemEffect[]{crushDamage});
        ItemDeck deck = new ItemDeck();
        deck.addItem(mace);
        return deck;
    }

    private Player createPlayer() {
        Player player = new Player("Knight");
        player.setStatValue(Stat.HEALTH, 40);
        player.setStatValue(Stat.ARMOR, 5);
        // put the player in the upper right hand corner of the map
        appState.getMap().addMob(player, 1, 1);
        player.setDeck(createDeck(8));

        StatBuff buff = new StatBuff(Stat.ARMOR, 4);
        Item shield = factory().createItemWithUsedByEffects("Shield of Protection", new ItemEffect[]{buff});
        player.getDeck().addItem(shield);

        return player;
    }

    private ItemFactory factory() {
        EffectApplierFactory effectApplierFactory = new EffectApplierFactory();
        return new ItemFactory(effectApplierFactory);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

        // this is cheesy, but this is the only place we can hook into the init of game
        // and do anything.
        initMonsterFactory();
        initPlayerClassFactory();
        initSpriteSheetCache();
        // TEMP
        initMonsters();


        //addState(new TitleGameState());
        addState(createDungeonState());
        addState(createBattleState());
    }

    private void initPlayerClassFactory() throws SlickException {
        professionFactory = new ProfessionFactory();
    }

    private void initMonsters() {
        // an an enemy
        Monster monster = monsterFactory.createMonster(0);
        monster.setStatValue(Stat.HEALTH, 10);
        monster.setDeck(createDeck(1));
        appState.getMap().addMob(monster, 2, 1);

        Monster monster2 = monsterFactory.createMonster(0);
        monster2.setStatValue(Stat.HEALTH, 10);
        monster2.setDeck(createDeck(1));
        appState.getMap().addMob(monster2, 2, 2);

        Monster monster3 = monsterFactory.createMonster(0);
        monster3.setStatValue(Stat.HEALTH, 10);
        monster3.setDeck(createDeck(3));
        appState.getMap().addMob(monster3, 3, 3);
    }

    private void initMonsterFactory() throws SlickException {
        monsterFactory = new MonsterFactory();
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
