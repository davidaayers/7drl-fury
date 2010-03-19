package com.wwflgames.fury.map;

import com.wwflgames.fury.map.generation.DigException;
import com.wwflgames.fury.map.generation.Digger;
import com.wwflgames.fury.map.generation.Feature;
import com.wwflgames.fury.map.generation.JoinPoint;
import com.wwflgames.fury.map.generation.RoomDigger;
import com.wwflgames.fury.map.generation.SquareRoomDigger;
import com.wwflgames.fury.util.AsciiMapPrinter;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Rand;
import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.List;

public class DungeonMapCreatorImpl implements DungeonMapCreator {
    @Override
    public DungeonMap createMap(DifficultyLevel difficulty, int level) {

        // create a map of random size, based on the difficulty
        int min = difficulty.getLevelSizeMin();
        int max = difficulty.getLevelSizeMax();
        int height = Rand.between(min, max);
        int width = Rand.between(min, max);

        DungeonMap map = new DungeonMap(width, height);

        List<Digger> diggers = new ArrayList<Digger>();
        diggers.add(new RoomDigger(5, 5, 8, 8));
        diggers.add(new RoomDigger(6, 6, 10, 10));
        diggers.add(new SquareRoomDigger(4, 7));

        Log.debug("width = " + width);
        Log.debug("height = " + height);

        // pick a location in the middle-ish of the map,
        int middleX = (width / 2) + Rand.between(-5, 5);
        int middleY = (height / 2) + Rand.between(-5, 5);

        Log.debug("middleX = " + middleX);
        Log.debug("middleY = " + middleY);

        //TODO: random starting direction        
        JoinPoint starterPoint = new JoinPoint(middleX, middleY, Direction.N);

        int numFeatures = 0;
        int tryCount = 0;
        List<JoinPoint> unconnectedPoints = new ArrayList<JoinPoint>();
        unconnectedPoints.add(starterPoint);
        while (numFeatures < 10 && tryCount < 100) {
            Shuffler.shuffle(diggers);
            Digger d = diggers.get(0);
            Log.debug("Using digger " + d);
            try {
                // grab a random unconnected point
                Shuffler.shuffle(unconnectedPoints);
                JoinPoint p = unconnectedPoints.get(0);
                Log.debug("Trying join point " + p );
                Feature f = d.dig(map, p);
                if (f != null) {
                    Log.debug("Sucessfully dug a feature!");
                    AsciiMapPrinter.printMap(map);
                    for (JoinPoint point : f.getJoinPoints()) {
                        if (!point.isConnected()) {
                            unconnectedPoints.add(point);
                        }
                    }
                    numFeatures++;
                }
            } catch (DigException e) {
                Log.debug("Couldn't dig");
            }
            tryCount++;
        }


        return map;
    }

}
