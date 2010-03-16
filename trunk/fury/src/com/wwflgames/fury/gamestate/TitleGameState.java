package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.SpriteSheetCache;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.item.effect.Damage;
import com.wwflgames.fury.item.effect.DamageType;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.item.effect.StatBuff;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.player.Player;
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

    enum State {
        WAITING_FOR_PROFESSION_CHOICE,
        START_GENERATING_MAP,
        GENERATING_MAP,
        GAME_START
    }


    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private Image titleImage;
    private ProfessionFactory professionFactory;
    private SpriteSheetCache spriteSheetCache;
    private List<MouseOverArea> professionChoices = new ArrayList<MouseOverArea>();
    private State currentState;
    private AppState appState;

    public TitleGameState(ProfessionFactory professionFactory, SpriteSheetCache spriteSheetCache, AppState appState) {
        this.professionFactory = professionFactory;
        this.spriteSheetCache = spriteSheetCache;
        this.appState = appState;
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
            g1.drawImage(img, 0, 0, Color.gray);
            g1.flush();

            // create an image for the mouse over Image, which includes the name of
            // the profession
            Image mouseOver = new Image(imgWidth, imgHeight + 20);
            Graphics g = mouseOver.getGraphics();
            g.drawImage(img, 0, 0);
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

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        currentState = State.WAITING_FOR_PROFESSION_CHOICE;
    }

    private void professionChosen(Profession profession) {
        Log.debug("Profession chosen: " + profession);
        currentState = State.START_GENERATING_MAP;
        Player player = new Player(profession.getName(), profession);
        player.setStatValue(Stat.HEALTH, 40);
        player.setStatValue(Stat.ARMOR, 5);
        // put the player in the upper right hand corner of the map
        appState.getMap().addMob(player, 1, 1);

        ItemFactory factory = null;
        try {
            factory = new ItemFactory();
        } catch (SlickException e) {
            e.printStackTrace();
        }

        //player.setDeck(createDeck(8));
        Damage crushDamage = new Damage(DamageType.MELEE_CRUSH, 8);
        Item mace = factory.createItemWithUsedAgainstEffects("Mace of crushing", new ItemEffect[]{crushDamage});
        ItemDeck deck = new ItemDeck();
        deck.addItem(mace);
        player.setDeck(deck);

        StatBuff buff = new StatBuff(Stat.ARMOR, 4);
        Item shield = factory.createItemWithUsedByEffects("Shield of Protection", new ItemEffect[]{buff});
        player.getDeck().addItem(shield);

        appState.setPlayer(player);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
            throws SlickException {

        graphics.drawImage(titleImage, 400 - titleImage.getWidth() / 2, 25);
        if (currentState == State.WAITING_FOR_PROFESSION_CHOICE) {
            TextUtil.centerText(gameContainer, graphics, "Choose your profession:", 220);
        }

        for (MouseOverArea moa : professionChoices) {
            moa.render(gameContainer, graphics);
        }

        if (currentState == State.GENERATING_MAP || currentState == State.START_GENERATING_MAP) {
            TextUtil.centerText(gameContainer, graphics, "Generating dungeon...", 520);
        }

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        switch (currentState) {
            case WAITING_FOR_PROFESSION_CHOICE:

                break;

            case START_GENERATING_MAP:
                currentState = State.GENERATING_MAP;
                break;

            case GENERATING_MAP:
                // spin off a thread to generate the map, so the game will keep updaing
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disableMouseOverAreas();
                        generateMap();
                        currentState = State.GAME_START;
                    }
                }).start();
                break;

            case GAME_START:
                stateBasedGame.enterState(Fury.DUNGEON_GAME_STATE, new FadeOutTransition(Color.black),
                        new FadeInTransition(Color.black));
                break;
        }
    }

    private void disableMouseOverAreas() {
        for (MouseOverArea moa : professionChoices) {
            moa.setAcceptingInput(false);
        }
    }

    private void generateMap() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
