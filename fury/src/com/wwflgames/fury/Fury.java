package com.wwflgames.fury;

import com.wwflgames.fury.gamestate.BattleGameState;
import com.wwflgames.fury.main.AppStateImpl;
import com.wwflgames.fury.map.FixedMapCreator;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
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
        Monster monster = new Monster("Slavering Skeleton");
        appState.getMap().addMob(monster, 1, 2);

        Monster monster2 = new Monster("Slavering Skeleton");
        appState.getMap().addMob(monster2, 2, 1);

        Monster monster3 = new Monster("Slavering Skeleton");
        appState.getMap().addMob(monster3, 2, 2);

        //TODO: move player creation somewhere else, too
        appState.setPlayer(createPlayer());
    }

    private Player createPlayer() {
        Player player = new Player("Valiant Knight");
        // put the player in the upper right hand corner of the map
        appState.getMap().addMob(player, 1, 1);
        return player;
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        //addState(new TitleGameState());
        //addState(new DungeonGameState());
        addState(createBattleState());
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
