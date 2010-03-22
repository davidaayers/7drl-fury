package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.main.AppState;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class ManageDeckGameState extends BasicGameState {

    private AppState appState;
    private GameContainer container;
    private StateBasedGame game;
    private List<ItemContainer> currentDeckItems = new ArrayList<ItemContainer>();
    private List<ItemContainer> allPlayerItems = new ArrayList<ItemContainer>();
    private List<MouseOverArea> mouseOvers = new ArrayList<MouseOverArea>();
    private Image plusImage;
    private Image minusImage;
    private boolean shouldRebuildButtons;


    public ManageDeckGameState(AppState appState) {
        this.appState = appState;
    }

    @Override
    public int getID() {
        return Fury.MANAGE_DECK_STATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;

        plusImage = new Image("plus.png");
        minusImage = new Image("minus.png");
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        choosePlayerDeck(1);
        createAllItemList();
        createButtons();

    }

    private void createButtons() {
        int x = 10;
        int y = 200;
        mouseOvers.clear();
        for (ItemContainer itemContainer : currentDeckItems) {
            final Item item = itemContainer.getItem();
            MouseOverArea moa = new MouseOverArea(container, minusImage, x, y, new ComponentListener() {
                @Override
                public void componentActivated(AbstractComponent source) {
                    removeItemFromCurrentDeck(item);
                }
            });
            moa.setMouseOverColor(Color.red);
            mouseOvers.add(moa);
            y += 20;
        }
    }

    private void removeItemFromCurrentDeck(Item item) {
        ItemContainer container = findContainerForItem(item, currentDeckItems);
        container.setQty(container.getQty() - 1);
        if (container.getQty() == 0) {
            currentDeckItems.remove(container);
        }

        ItemContainer container2 = findContainerForItem(item, allPlayerItems);
        if (container2 != null) {
            container2.incQty();
        } else {
            ItemContainer newItem = new ItemContainer(item);
            newItem.incQty();
            allPlayerItems.add(newItem);
        }
        shouldRebuildButtons = true;

    }

    private void createAllItemList() {
        createItemContainerList(appState.getPlayer().getAllItems(), allPlayerItems);

        // now, remove from the list any items that are in the current deck
        for (ItemContainer itemContainer : currentDeckItems) {
            ItemContainer c = findContainerForItem(itemContainer.getItem(), allPlayerItems);
            if (c != null) {
                c.setQty(c.getQty() - itemContainer.getQty());
                if (c.getQty() == 0) {
                    allPlayerItems.remove(c);
                }
            }
        }
    }

    private void choosePlayerDeck(int deckNo) {
        createItemContainerList(appState.getPlayer().deckForDeckNo(deckNo).getDeck(), currentDeckItems);
    }

    private void createItemContainerList(List<Item> items, List<ItemContainer> itemContainers) {
        itemContainers.clear();
        for (Item item : items) {
            ItemContainer container = findContainerForItem(item, itemContainers);
            if (container == null) {
                container = new ItemContainer(item);
                container.incQty();
                itemContainers.add(container);
            } else {
                container.incQty();
            }
        }
    }

    private ItemContainer findContainerForItem(Item item, List<ItemContainer> itemContainers) {
        for (ItemContainer container : itemContainers) {
            if (container.getItem() == item) {
                return container;
            }
        }
        return null;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // render left side, which contains all items in the current deck
        int x = 35;
        int y = 200;
        for (ItemContainer itemContainer : currentDeckItems) {
            String str = itemContainer.getItem().name() + " " + itemContainer.getQty();
            g.drawString(str, x, y);
            y += 20;

        }


        x = 400;
        y = 200;
        // render right side, which contains all available items
        for (ItemContainer itemContainer : allPlayerItems) {
            String str = itemContainer.getItem().name() + " " + itemContainer.getQty();
            g.drawString(str, x, y);
            y += 20;
        }

        for (MouseOverArea moa : mouseOvers) {
            moa.render(container, g);
        }

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (shouldRebuildButtons) {
            // re-create the buttons
            createButtons();
            shouldRebuildButtons = false;
        }

    }

    @Override
    public void keyPressed(int key, char c) {
        if (c == ' ') {
            // go back to dungeon screen
            game.enterState(Fury.DUNGEON_GAME_STATE);
        }
    }

    private class ItemContainer {
        private Item item;
        private int qty;

        private ItemContainer(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public void incQty() {
            qty++;
        }


    }
}
