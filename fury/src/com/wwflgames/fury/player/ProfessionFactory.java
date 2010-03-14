package com.wwflgames.fury.player;

import com.wwflgames.fury.util.Log;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.List;

public class ProfessionFactory {
    private List<Profession> allProfessions = new ArrayList<Profession>();
    List<String> allSpriteSheetNames = new ArrayList<String>();


    public ProfessionFactory() throws SlickException {
        parseXml();
    }

    private void parseXml() throws SlickException {
        // read in the monsters xml file
        XMLParser parser = new XMLParser();
        XMLElement element = parser.parse("player-classes.xml");
        XMLElementList children = element.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement childNode = children.get(idx);
            Log.debug("childNode = " + childNode.getName());
            String name = childNode.getAttribute("name");
            String spriteSheet = childNode.getAttribute("sprite-sheet");
            Profession profession = new Profession(name, spriteSheet);
            allProfessions.add(profession);
            allSpriteSheetNames.add(spriteSheet);
            Log.debug("class created = " + profession);
        }
    }

    public List<String> getAllSpriteSheetNames() {
        return allSpriteSheetNames;
    }

    public List<Profession> getAllProfessions() {
        return allProfessions;
    }
}
