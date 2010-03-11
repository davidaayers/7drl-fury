package com.wwflgames.fury.entity;

import org.newdawn.slick.Graphics;

public abstract class RenderComponent extends Component {

    public RenderComponent(String id) {
        super(id);
    }

    public abstract void render(Graphics gr);

}
