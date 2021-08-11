package com.ufsm.rockstar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    public enum State
    {
        IDLE, WALKING
    }
    static final float SPEED = 2f; // unit per second

    static final float SIZE = 0.5f; // half a unit

    Vector2  position = new Vector2();
    Vector2  acceleration = new Vector2();
    Vector2  velocity = new Vector2();
    Rectangle  bounds = new Rectangle();

    State  state = State.IDLE;
    boolean  facingLeft = true;
    public Player(Vector2 position)
    {
        this.position = position;
        this.bounds.height = SIZE;
        this.bounds.width = SIZE;
    }

    public Vector2 getPosition()
    {
        return position;
    }
    public Rectangle getBounds()
    {
        return bounds;
    }
}
