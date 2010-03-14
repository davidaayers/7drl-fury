package com.wwflgames.fury.monster;

import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Shuffler;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

public class MonsterFactory {

    List<Monster> allMonsters = new ArrayList<Monster>();
    List<String> allSpriteSheetNames = new ArrayList<String>();

    public MonsterFactory() throws SlickException {
        parseXml();
    }

    private void parseXml() throws SlickException {
        // read in the monsters xml file
        XMLParser parser = new XMLParser();
        XMLElement element = parser.parse("monsters.xml");
        XMLElementList children = element.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement childNode = children.get(idx);
            Log.debug("childNode = " + childNode.getName());
            String name = childNode.getAttribute("name");
            String spriteSheet = childNode.getAttribute("sprite-sheet");
            Monster monster = new Monster(name, spriteSheet);
            allMonsters.add(monster);
            allSpriteSheetNames.add(spriteSheet);
            Log.debug("monster created = " + monster);
        }
    }

    public List<String> getAllSpriteSheetNames() {
        return allSpriteSheetNames;
    }

    public Monster createMonster(int points) {
        Shuffler.shuffle(allMonsters);

        Monster monster = allMonsters.get(0);
        Log.debug("About to return " + monster);
        return monster;
    }


}
