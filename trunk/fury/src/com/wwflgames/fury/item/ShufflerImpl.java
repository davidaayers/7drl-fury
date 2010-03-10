package com.wwflgames.fury.item;

import java.util.Collections;
import java.util.List;

public class ShufflerImpl implements Shuffler {
    @Override
    public void shuffle(List deck) {
        Collections.shuffle(deck);
    }
}
