package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class DungeonGameState extends BasicGameState {
    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private EntityManager entityManager;
    private AppState appState;
    private SpriteSheet heroSprites;
    private MobMapPositionAction playerMapPosition;

    public DungeonGameState(AppState appState) {
        this.appState = appState;
    }


    @Override
    public int getID() {
        return Fury.GAME_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;

        // load sprites
        heroSprites = new SpriteSheet("warrior.png", 24, 32);


        entityManager = new EntityManager(gameContainer, stateBasedGame);
    }

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        // create a map entity
        Entity mapEntity = new Entity("map")
                .setPosition(new Vector2f(0, 0))
                .setScale(1)
                .addComponent(new DungeonMapRenderer("mapRender", appState.getMap()));

        entityManager.addEntity(mapEntity);

        playerMapPosition = new MobMapPositionAction("playerMapPosition", appState.getPlayer());

        // player entity
        Entity player = new Entity("player")
                .setPosition(new Vector2f(0, 0))
                .setScale(1)
                .addComponent(new SpriteSheetRenderer("playerRender", heroSprites).useSprite(1, 2))
                .addComponent(playerMapPosition);

        entityManager.addEntity(player);

        // grab all of the monsters on the map
        for (Monster monster : appState.getMap().getMonsterList()) {

        }

    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        Log.debug("DungeonGameState-> leaving state");
        entityManager.clear();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics)
            throws SlickException {

        entityManager.render(graphics);

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {

        entityManager.update(delta);

    }

    public void keyPressed(int key, char c) {


        Log.debug("key = " + key);
        // 7 key - NW
        if (key == 71) {
            tryMoveAndMaybeAttack(-1, -1);
        }

        // 8 key - N
        if (key == 72) {
            tryMoveAndMaybeAttack(0, -1);
        }

        // 9 key - NE
        if (key == 73) {
            tryMoveAndMaybeAttack(1, -1);
        }

        // 4 key - W
        if (key == 75) {
            tryMoveAndMaybeAttack(-1, 0);
        }

        // 6 key - E
        if (key == 77) {
            tryMoveAndMaybeAttack(1, 0);
        }

        // 1 key - SW
        if (key == 79) {
            tryMoveAndMaybeAttack(-1, 1);
        }

        // 2 key - S
        if (key == 80) {
            tryMoveAndMaybeAttack(0, 1);
        }

        // 3 key - SE
        if (key == 81) {
            tryMoveAndMaybeAttack(1, 1);
        }
    }

    private void tryMoveAndMaybeAttack(int dx, int dy) {
        Player player = appState.getPlayer();
        Tile tile = player.getCurrentMapTile();
        int currX = tile.getX();
        int currY = tile.getY();
        int newX = currX + dx;
        int newY = currY + dy;
        Map map = appState.getMap();

        // first, see if moving to newX,newY would cause combat
        Mob enemy = map.getTileAt(newX, newY).getMob();
        if (enemy != null) {
            initiateCombat(player, enemy);
        } else if (map.inBounds(newX, newY) && map.isWalkable(newX, newY)) {
            map.addMob(player, newX, newY);
        } else {
            Log.debug("Hit a wall!");
        }
    }

    private void initiateCombat(Mob initiator, Mob initialTarget) {

    }

}
