package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.SpriteSheetCache;
import com.wwflgames.fury.player.Profession;
import com.wwflgames.fury.player.ProfessionFactory;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.TextUtil;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.util.ArrayList;
import java.util.List;

public class TitleGameState extends BasicGameState {
    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private Image titleImage;
    private ProfessionFactory professionFactory;
    private SpriteSheetCache spriteSheetCache;
    private List<MouseOverArea> professionChoices = new ArrayList<MouseOverArea>();

    public TitleGameState(ProfessionFactory professionFactory, SpriteSheetCache spriteSheetCache) {
        this.professionFactory = professionFactory;
        this.spriteSheetCache = spriteSheetCache;
    }

    @Override
    public int getID() {
        return Fury.TITLE_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;
        titleImage = new Image("title-image.png");

        // create mouse over areas for the professions
        List<Profession> allProfessions = professionFactory.getAllProfessions();
        int numProfessions = allProfessions.size();
        // figure out how much room we have, and space them out evenly
        float pixelsPerProfession = 800f / (float) numProfessions;
        float x = pixelsPerProfession / 2;
        float y = 250;
        for (final Profession profession : professionFactory.getAllProfessions()) {
            Image img = spriteSheetCache.getSpriteSheet(profession.getSpriteSheet()).getSprite(1, 2);
            img = img.getScaledCopy(4f);

            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            Image realImage = new Image(imgWidth, imgHeight + 20);
            Graphics g1 = realImage.getGraphics();
            g1.drawImage(img, 0, 0);
            g1.flush();

            // create an image for the mouse over Image, which includes the name of
            // the profession
            Image mouseOver = new Image(imgWidth, imgHeight + 20);
            Graphics g = mouseOver.getGraphics();
            g.drawImage(img, 0, 0, Color.red);
            g.setColor(Color.white);
            String prof = profession.getName();
            int strWidth = g.getFont().getWidth(prof);
            int strX = imgWidth / 2 - strWidth / 2;
            g.drawString(prof, strX, imgHeight);
            g.flush();

            int imgx = (int) x - img.getWidth() / 2;
            MouseOverArea moa = new MouseOverArea(gameContainer, realImage, imgx, (int) y, new ComponentListener() {
                @Override
                public void componentActivated(AbstractComponent source) {
                    professionChosen(profession);
                }
            });
            //moa.setMouseOverColor(Color.red);
            moa.setMouseOverImage(mouseOver);


            professionChoices.add(moa);
            x += pixelsPerProfession;
        }

    }

    private void professionChosen(Profession profession) {
        Log.debug("Profession chosen: " + profession);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
            throws SlickException {

        graphics.drawImage(titleImage, 400 - titleImage.getWidth() / 2, 25);
        TextUtil.centerText(gameContainer, graphics, "Choose your profession:", 220);

        for (MouseOverArea moa : professionChoices) {
            moa.render(gameContainer, graphics);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }

    @Override
    public void keyPressed(int key, char c) {
        stateBasedGame.enterState(Fury.DUNGEON_GAME_STATE, new FadeOutTransition(Color.black),
                new FadeInTransition(Color.black));
    }
}
