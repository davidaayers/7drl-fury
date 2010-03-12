package com.wwflgames.fury.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

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
        return   package com.wwflgames.fury.gamestate;

        import com.wwflgames.fury.Fury;
        import com.wwflgames.fury.battle.Battle;
        import com.wwflgames.fury.battle.BattleSystem;
        import com.wwflgames.fury.entity.BattleMapRenderComponent;
        import com.wwflgames.fury.entity.Entity;
        import com.wwflgames.fury.entity.EntityManager;
        import com.wwflgames.fury.entity.MobLocationComponent;
        import com.wwflgames.fury.entity.SpriteSheetRenderComponent;
        import com.wwflgames.fury.main.AppState;
        import com.wwflgames.fury.map.Map;
        import com.wwflgames.fury.mob.Mob;
        import com.wwflgames.fury.monster.Monster;
        import com.wwflgames.fury.player.Player;
        import com.wwflgames.fury.util.Log;
        import com.wwflgames.fury.util.TextUtil;
        import org.newdawn.slick.Color;
        import org.newdawn.slick. *;
        import org.newdawn.slick.Graphics;
        import org.newdawn.slick.font.effects.ColorEffect;
        import org.newdawn.slick.geom.Vector2f;
        import org.newdawn.slick.state.BasicGameState;
        import org.newdawn.slick.state.StateBasedGame;

        import java.awt.