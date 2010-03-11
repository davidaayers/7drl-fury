package com.wwflgames.fury.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String id;
    private Vector2f position;
    private float scale;
    private float rotation;

    private RenderComponent renderComponent;
    private List<Component> components;

    private GameContainer container;
    private StateBasedGame game;

    public Entity(String id, GameContainer container, StateBasedGame game) {
        this.id = id;
        this.container = container;
        this.game = game;

        components = new ArrayList<Component>();

        position = new Vector2f(0, 0);
        scale = 1;
        rotation = 0;

    }

    public Entity addComponent(Component component) {
        if (component instanceof RenderComponent) {
            renderComponent = (RenderComponent) component;
        }
        component.installOwner(this);
        components.add(component);
        return this;
    }

    public Component getComponent(String id) {
        for (Component comp : components) {
            if (comp.getId().equals(id)) {
                return comp;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public GameContainer getContainer() {
        return container;
    }

    public StateBasedGame getGame() {
        return game;
    }

    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        for (Component component : components) {
            component.update(delta);
        }
    }

    public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
        if (renderComponent != null) {
            renderComponent.render(gr);
        }
    }

}
