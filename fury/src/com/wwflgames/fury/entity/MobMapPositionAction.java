package com.wwflgames.fury.entity;

import com.wwflgames.fury.mob.Mob;
import org.newdawn.slick.geom.Vector2f;

public class MobMapPositionAction extends Action {

    private Mob mob;

    public MobMapPositionAction(String id, Mob mob) {
        super(id);
        this.mob = mob;
    }

    @Override
    public void update(int delta) {
        // get the mob's current position on the dungeonMap, and update the entity
        // based on it
        int mapX = mob.getCurrentMapTile().getX();
        int mapY = mob.getCurrentMapTile().getY();

        float drawX = mapX * 32;
        float drawY = mapY * 32;
        owner.setPosition(new Vector2f(drawX, drawY));
    }
}
