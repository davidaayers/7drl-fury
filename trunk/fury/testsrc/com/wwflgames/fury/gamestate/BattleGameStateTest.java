package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.map.FixedMapCreator;
import com.wwflgames.fury.map.Map;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class BattleGameStateTest {
    private BattleGameState battleGameState;

    @Before
    public void setUp() throws Exception {
        // don't need app state for anything yet in this test
        battleGameState = new BattleGameState(null, null);
    }

    @Test
    public void findMonstersActuallyFindsMonstersAndIgnoresPlayer() {
        // set up a test map
        Map map = new FixedMapCreator().createMap();
        Player p1 = new Player("hero", null);
        Monster m1 = new Monster("foo", "foo");
        Monster m2 = new Monster("bar", "foo");
        map.addMob(m1, 1, 1);
        map.addMob(m2, 3, 3);
        map.addMob(p1, 2, 2);

        assertEquals(new Integer(2), p1.getMapX());
        assertEquals(new Integer(2), p1.getMapY());

        List<Monster> monsters = battleGameState.findMonsters(map, p1.getMapX(), p1.getMapY());
        assertEquals(2, monsters.size());
    }
}
