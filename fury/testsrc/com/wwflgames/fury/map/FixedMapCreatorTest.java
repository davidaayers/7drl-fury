package com.wwflgames.fury.map;

import org.junit.Before;
import org.junit.Test;

public class FixedMapCreatorTest {
    private FixedMapCreator mapCreator;

    @Before
    public void setUp() throws Exception {
        mapCreator = new FixedMapCreator();
    }

    @Test
    public void testCreateMap() {
        Map m = mapCreator.createMap();

        for ( int y = 0 ; y < 5 ; y ++ ) {
            for ( int x = 0 ; x < 5 ; x ++ ) {
                System.out.print(m.getTileAt(x,y).getType().getAscii());
            }
            System.out.println("");
        }
    }
}
