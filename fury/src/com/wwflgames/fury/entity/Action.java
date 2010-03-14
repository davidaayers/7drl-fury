package com.wwflgames.fury.entity;

public abstract class Action extends Component {

    protected boolean isActionComplete;

    public Action(String id) {
        super(id);
        isActionComplete = false;
    }

    public boolean isActionComplete() {
        return isActionComplete;
    }

}
