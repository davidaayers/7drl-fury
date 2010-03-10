package com.wwflgames.fury.item;

import com.wwflgames.fury.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ItemDeck {

    private List<Item> deck = new ArrayList<Item>();
    private int idx;
    private Shuffler shuffler;

    public ItemDeck(Shuffler shuffler) {
        this.shuffler = shuffler;
    }

    public void addItem(Item item) {
        deck.add(item);
    }

    public void removeItem(Item item ) {
        deck.remove(item);
    }

    public void shuffle() {  
        shuffler.shuffle(deck);
        idx = 0;
    }

    public Item nextItem()  {
        if ( idx == deck.size() ) {
            Log.debug("Reshuffling deck");
            shuffle();
        }
        Item item = deck.get(idx);
        idx++;
        return item;
    }

}
