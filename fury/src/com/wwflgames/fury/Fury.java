package com.wwflgames.fury;

import com.wwflgames.fury.gamestate.BattleState;
import com.wwflgames.fury.gamestate.GamePlayState;
import com.wwflgames.fury.gamestate.TitleState;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.FixedMapCreator;
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

    private AppState appState;

    public Fury() {
        super("Fury - 7DRL");
        initAppState();
    }

    private void initAppState() {
        appState = new AppState();

        //TODO: move map creation somewhere else, later
        appState.setMap(new FixedMapCreator().createMap());

        //TODO: move player creation somewhere else, too
        appState.setPlayer(new Player("Valiant Knight"));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        //addState(new TitleState());
        //addState(new GamePlayState());
        addState(createBattleState());
    }

    private BattleState createBattleState() {
        return new BattleState(appState);
    }

    public static void main ( String[] args ) {
        ResourceLoader.removeAllResourceLocations();
		ResourceLoader.addResourceLocation(new ClasspathLocation());

		try {
			container = new AppGameContainer(new Fury());
			container.setDisplayMode(800,600,false);
			container.setShowFPS(true);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

    }



}
