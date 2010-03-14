package com.wwflgames.fury.item;

import com.wwflgames.fury.item.effect.*;
import com.wwflgames.fury.mob.Stat;
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

    private EffectApplierFactory effectsApplierFactory;
    private Map<String, Item> itemNameMap = new HashMap<String, Item>();

    public ItemFactory() throws SlickException {
        this.effectsApplierFactory = new EffectApplierFactory();
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
            createItemFromXml(childNode);

        }
    }

    private void createItemFromXml(XMLElement itemNode) throws SlickXMLException {
        // grab the usedByEffects
        String itemName = itemNode.getAttribute("name");
        ItemEffect[] usedByEffects = getEffectsForNode(itemNode, "usedByEffects");
        ItemEffect[] usedAgainstEffects = getEffectsForNode(itemNode, "usedAgainstEffects");
        Item item = createItem(itemName, usedByEffects, usedAgainstEffects);
        itemNameMap.put(item.name(), item);
        Log.debug("created item " + item);
    }

    private ItemEffect[] getEffectsForNode(XMLElement itemNode, String nodeName) throws SlickXMLException {
        List<ItemEffect> effectsList = new ArrayList<ItemEffect>();
        XMLElementList usedByParent = itemNode.getChildrenByName(nodeName);
        XMLElement usedBy = usedByParent.get(0);
        Log.debug("usedBy = " + usedBy.getName());
        XMLElementList usedByChildren = usedBy.getChildren();
        Log.debug("usedByChildren.size() = " + usedByChildren.size());
        if (usedByChildren.size() != 0) {
            for (int idx = 0; idx < usedByChildren.size(); idx++) {
                XMLElement usedByChild = usedByChildren.get(idx);
                String name = usedByChild.getName();
                Log.debug("usedByChild.name = " + name);
                ItemEffect effect = createItemEffectFromNode(usedByChild);
                effectsList.add(effect);
            }
        }
        ItemEffect[] effects = null;
        if (!effectsList.isEmpty()) {
            effects = effectsList.toArray(new ItemEffect[effectsList.size()]);
        }
        return effects;

    }


    private ItemEffect createItemEffectFromNode(XMLElement childNode) throws SlickXMLException {
        if (childNode.getName().equals("stat-buff")) {
            Stat stat = Stat.valueOf(childNode.getAttribute("stat").toUpperCase());
            Log.debug("stat = " + stat);
            int value = childNode.getIntAttribute("value");
            Log.debug("value = " + value);
            return new StatBuff(stat, value);
        }
        if (childNode.getName().equals("damage")) {
            String type = childNode.getAttribute("type");
            int value = childNode.getIntAttribute("value");
            DamageType damageType = DamageType.valueOf(type.toUpperCase());
            return new Damage(damageType, value);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        new ItemFactory();
    }

    public Item createItemWithUsedAgainstEffects(String name, ItemEffect[] effects) {
        return new ItemImpl(effectsApplierFactory, name, null, effects);
    }

    public Item createItemWithUsedByEffects(String name, ItemEffect[] effects) {
        return new ItemImpl(effectsApplierFactory, name, effects, null);
    }

    public Item createItem(String name, ItemEffect[] usedByEffects, ItemEffect[] usedAgainstEffects) {
        return new ItemImpl(effectsApplierFactory, name, usedByEffects, usedAgainstEffects);
    }

    public Item getItemByName(String name) {
        return itemNameMap.get(name);
    }

}
