package com.wwflgames.fury.entity;

public class DisplayForTimeComponent extends Component {

    private int time;
    private int counter;

    public DisplayForTimeComponent(String id, int time) {
        super(id);
        this.time = time;
        counter = 0;
    }

    @Override
    public void update(int delta) {
        if (!owner.shouldRemove()) {
            counter += delta;
            if (counter > time) {
                owner.setRemove(true);
            }
        }
    }
}
