package com.wwflgames.fury.map;

import org.junit.Test;

public class DungeonMapCreatorImplTest {

    @Test
    public void testCreateMap() throws Exception {
        DungeonMapCreatorImpl mapCreator = new DungeonMapCreatorImpl();
        DungeonMap map = mapCreator.createMap(DifficultyLevel.EASY,5);
        AsciiMapPrinter.printMap(map);
    }

}