package com.wwflgames.fury.item;

import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.item.effect.MeleeDamageEffect;
import com.wwflgames.fury.item.effect.damage.MeleeDamage;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFactory {

    private Map<String, Item> itemNameMap = new HashMap<String, Item>();

    public ItemFactory() throws SlickException {
        parseXml();
    }

    private void parseXml() throws SlickException {
        // read in the monsters xml file
        XMLParser parser = new XMLParser();
        XMLElement element = parser.parse("items.xml");
        XMLElementList children = element.getChildren();
        for (int idx = 0; idx < children.size(); idx++) {
            XMLElement childNode = children.get(idx);
            Log.debug("childNode = " + childNode.getName());
            String itemName = childNode.getAttribute("name");
            Log.debug("itemName = " + itemName);
            createNItemFromXml(childNode);
        }
    }

    private void createNItemFromXml(XMLElement itemNode) throws SlickXMLException {
        // grab the usedByEffects
        String itemName = itemNode.getAttribute("name");
        ItemEffect[] usedByEffects = getNEffectsForNode(itemNode, "usedByEffects");
        ItemEffect[] usedAgainstEffects = getNEffectsForNode(itemNode, "usedAgainstEffects");
        Item item = createNItem(itemName, usedByEffects, usedAgainstEffects);
        itemNameMap.put(item.name(), item);
        Log.debug("created item " + item);
    }

    private ItemEffect[] getNEffectsForNode(XMLElement itemNode, String nodeName) throws SlickXMLException {
        List<ItemEffect> effectsList = new ArrayList<ItemEffect>();
        XMLElementList usedByParent = itemNode.getChildrenByName(nodeName);
        XMLElement usedBy = usedByParent.get(0);
        Log.debug(nodeName + " = " + usedBy.getName());
        XMLElementList usedByChildren = usedBy.getChildren();
        Log.debug(nodeName + "Children.size() = " + usedByChildren.size());
        if (usedByChildren.size() != 0) {
            for (int idx = 0; idx < usedByChildren.size(); idx++) {
                XMLElement usedByChild = usedByChildren.get(idx);
                String name = usedByChild.getName();
                Log.debug(nodeName + "Child.name = " + name);
                ItemEffect effect = createNItemEffectFromNode(usedByChild);
                effectsList.add(effect);
            }
        }
        ItemEffect[] effects = null;
        if (!effectsList.isEmpty()) {
            effects = effectsList.toArray(new ItemEffect[effectsList.size()]);
        }
        return effects;

    }

    private ItemEffect createNItemEffectFromNode(XMLElement childNode) throws SlickXMLException {
        if (childNode.getName().equals("damage")) {
            String type = childNode.getAttribute("type");
            int value = childNode.getIntAttribute("value");
            Log.debug("Looking for damage matching: " + type);
            com.wwflgames.fury.item.effect.damage.Damage dmg = com.wwflgames.fury.item.effect.damage.Damage.forType(type);
            Log.debug("type was " + dmg);
            if (dmg instanceof MeleeDamage) {
                MeleeDamageEffect damageEffect = new MeleeDamageEffect((MeleeDamage) dmg, value);
                Log.debug("Created meleeDamageEffect = " + damageEffect.getDesc());
                return damageEffect;
            }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        new ItemFactory();
    }

    public Item createNItem(String name, ItemEffect[] usedByEffects, ItemEffect[] usedAgainstEffects) {
        return new ItemImpl(name, usedByEffects, usedAgainstEffects);
    }


    public Item getItemByName(String name) {
        return itemNameMap.get(name);
    }

}
