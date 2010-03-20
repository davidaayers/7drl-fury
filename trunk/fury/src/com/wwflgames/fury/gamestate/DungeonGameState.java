package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.*;
import com.wwflgames.fury.main.AppState;
import com.wwflgames.fury.map.Direction;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class DungeonGameState extends BasicGameState {
    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private EntityManager entityManager;
    private AppState appState;
    private SpriteSheetCache spriteSheetCache;
    private PlayerController playerController;

    public DungeonGameState(AppState appState, SpriteSheetCache spriteSheetCache) {
        this.appState = appState;
        this.spriteSheetCache = spriteSheetCache;
    }


    @Override
    public int getID() {
        return Fury.DUNGEON_GAME_STATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;

        entityManager = new EntityManager(gameContainer, stateBasedGame);
    }

    public void enter(GameContainer container, StateBasedGame game) throws SlickException {

        playerController = new PlayerController(appState.getPlayer(), appState.getMap());

        // create a dungeonMap entity
        Entity mapEntity = new Entity("dungeonMap")
                .setPosition(new Vector2f(0, 0))
                .setScale(1)
                .addComponent(new DungeonMapRenderer("mapRender", appState.getMap(), playerController));

        entityManager.addEntity(mapEntity);

        // player entity
        Entity player = new Entity("player")
                .setPosition(new Vector2f(0, 0))
                .setScale(1)
                .addComponent(new SpriteSheetRenderer("playerRender",
                        spriteSheetCache.getSpriteSheet(appState.getPlayer().getProfession().getSpriteSheet()))
                        .useSprite(1, 2))
                .addComponent(new MobMapPositionAction("mapPosition", appState.getPlayer(), playerController));

        entityManager.addEntity(player);

        // grab all of the monsters on the dungeonMap
        for (Monster monster : appState.getMap().getMonsterList()) {
            Entity monsterEntity = new Entity("monster" + monster.name())
                    .setPosition(new Vector2f(0, 0))
                    .setScale(1)
                    .addComponent(new SpriteSheetRenderer("monsterRenderer",
                            spriteSheetCache.getSpriteSheet(monster.getSpriteSheet())).useSprite(1, 2))
                    .addComponent(new MobMapPositionAction("mapPosition", monster, playerController));

            entityManager.addEntity(monsterEntity);
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
        Direction d = Direction.forKey(key);
        if (d != null) {
            tryMoveAndMaybeAttack(d.getDx(), d.getDy());
        }
    }

    private void tryMoveAndMaybeAttack(int dx, int dy) {
        Player player = appState.getPlayer();
        Tile tile = player.getCurrentMapTile();
        int currX = tile.getX();
        int currY = tile.getY();
        int newX = currX + dx;
        int newY = currY + dy;
        DungeonMap dungeonMap = appState.getMap();

        Log.debug("Inspecting " + newX + "," + newY);

        // first, see if moving to newX,newY would cause combat
        if (!dungeonMap.inBounds(newX, newY)) {
            Log.debug("Out of bounds");
            return;
        }
        Mob enemy = dungeonMap.getTileAt(newX, newY).getMob();
        Log.debug("Enemy present, enemy was " + enemy);
        if (enemy != null) {
            Log.debug("about to initiate combat");
            initiateCombat(player);
        } else if (dungeonMap.inBounds(newX, newY) && dungeonMap.isWalkable(newX, newY)) {
            playerController.movePlayerTo(newX, newY);
        } else {
            Log.debug("Hit a wall!");
        }
    }

    private void initiateCombat(Mob initiator) {
        if (initiator instanceof Player) {
            appState.setPlayerInitiative(true);
        } else {
            appState.setPlayerInitiative(false);
        }
        stateBasedGame.enterState(Fury.BATTLE_STATE);
    }

}
