package com.wwflgames.fury.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import javax.naming.ldap.Rdn;
import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String id;
    private Vector2f position;
    private float scale;
    private float rotation;
    private Integer zIndex = 0;

    private RenderComponent renderComponent;
    private List<Component> components = new ArrayList<Component>();

    private GameContainer container;
    private StateBasedGame game;

    public Entity(String id) {
        this.id = id;
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

    public Vector2f getPosition() {
        return position;
    }

    public Entity setPosition(Vector2f position) {
        this.position = position;
        return this;
    }

    public float getScale() {
        return scale;
    }

    public Entity setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public Integer getZIndex() {
        return zIndex;
    }

    public Entity setZIndex(Integer zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    public Entity setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public GameContainer getContainer() {
        return container;
    }

    public Entity setContainer(GameContainer container) {
        this.container = container;
        return this;
    }

    public StateBasedGame getGame() {
        return game;
    }

    public Entity setGame(StateBasedGame game) {
        this.game = game;
        return this;
    }

    public void update(int delta) {
        for (Component component : components) {
            component.update(delta);
        }
    }

    public void render(Graphics gr) {
        if (renderComponent != null) {
            renderComponent.render(gr);
        }
    }

}
