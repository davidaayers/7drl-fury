package com.wwflgames.fury.entity;

import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.map.TileType;
import com.wwflgames.fury.mob.Mob;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MobLocationComponentTest {
    private MobLocationComponent mobLocationComponent;

    @Before
    public void setUp() throws Exception {
        mobLocationComponent = new MobLocationComponent("test");
    }

    @Test
    public void updateShouldSetCorrectVector2f() throws Exception {
        Mob testMob = new Mob("test");
        Tile mapTile = new Tile(TileType.FLOOR,1,1);
        testMob.setCurrentMapTile(mapTile);

        mobLocationComponent
                .setMapOffset(0,0)
                .setScreenOffset(208,32)
                .setMob(testMob);

        Entity mobEntity = new Entity("mob")
                .setScale(4)
                .addComponent(mobLocationComponent);

        mobLocationComponent.update(10);

        assertEquals(352.0f,mobEntity.getPosition().getX());
        assertEquals(160.0f,mobEntity.getPosition().getY());
    }
}
