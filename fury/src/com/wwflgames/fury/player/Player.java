package com.wwflgames.fury.player;

import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Mob {

    private Profession profession;
    private List<ItemDeck> decks = new ArrayList<ItemDeck>();
    private Map<Integer, ItemDeck> deckMap = new HashMap<Integer, ItemDeck>();

    public Player(String name, Profession profession) {
        super(name);
        this.profession = profession;
    }

    public Profession getProfession() {
        return profession;
    }

    public void installDeck(int deckNo, ItemDeck deck) {
        deckMap.put(deckNo, deck);
    }

    public void setDefaultDeck(int deckNo) {
        setDeck(deckMap.get(deckNo));
    }


}
