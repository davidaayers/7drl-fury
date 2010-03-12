package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.util.TextUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TitleGameState extends BasicGameState {
    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;

    @Override
    public int getID() {
        return Fury.TITLE_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
            throws SlickException {
        TextUtil.centerText(gameContainer, graphics, "Fury - A 7 Day Roguelike", 150);
        TextUtil.centerText(gameContainer, graphics, "Press any key to start", 180);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {
        stateBasedGame.enterState(Fury.BATTLE_STATE, new FadeOutTransition(Color.black),
                new FadeInTransition(Color.black));
    }
}
