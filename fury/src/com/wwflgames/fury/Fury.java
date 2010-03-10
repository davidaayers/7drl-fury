package com.wwflgames.fury;

import com.wwflgames.fury.gamestate.GamePlayState;
import com.wwflgames.fury.gamestate.TitleState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

public class Fury extends StateBasedGame {

    public static final int TITLE_STATE = 1;
    public static final int GAME_STATE = 2;


    private static AppGameContainer container;

    public Fury() {
        super("Fury - 7DRL");
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        addState(new TitleState());
        addState(new GamePlayState());
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
