package com.wwflgames.fury;

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
import com.wwflgames.fury.player.Player;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

public class Fury extends StateBasedGame {

    public static final int TITLE_STATE = 1;
    public static final int GAME_STATE = 2;
    public static final int BATTLE_STATE = 3;

    private static AppGameContainer container;

    private AppStateImpl appState;

    public Fury() {
        super("Fury - 7DRL");
        initAppState();
    }

    private void initAppState() {
        appState = new AppStateImpl();

        //TODO: move map creation somewhere else, later
        appState.setMap(new FixedMapCreator().createMap());

        // an an enemy
        Monster monster = new Monster("Scary Skeleton");
        monster.setStatValue(Stat.HEALTH, 10);
        monster.setDeck(createDeck(1));
        appState.getMap().addMob(monster, 2, 1);

        Monster monster2 = new Monster("Slavering Skeleton");
        monster2.setStatValue(Stat.HEALTH, 10);
        monster2.setDeck(createDeck(1));
        appState.getMap().addMob(monster2, 2, 2);
//
//        Monster monster3 = new Monster("Slavering Skeleton");
//        monster3.setStatValue(Stat.HEALTH,10);
//        monster3.setDeck(createDeck());
//        appState.getMap().addMob(monster3, 2, 2);

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
        player.setDeck(createDeck(2));

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
        //addState(new TitleGameState());
        addState(createDungeonState());
        addState(createBattleState());
    }

    private GameState createDungeonState() {
        return new DungeonGameState(appState);
    }

    private BattleGameState createBattleState() {
        return new BattleGameState(appState);
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
