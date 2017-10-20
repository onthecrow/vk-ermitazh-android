package com.doubletapp.hermitage.hermitage.model.map;

/**
 * Created by navi9 on 20.10.2017.
 */

public class Position {
    private int x;
    private int y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Position)) {
            return false;
        }

        Position position = (Position) obj;

        return position.getX() == x && position.y == position.getY();
    }
}