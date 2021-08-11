package com.ufsm.rockstar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    public enum State
    {
        IDLE, WALKING
    }
    public static final float SPEED = 2f; // unit per second

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

    public void update(float delta)
    {
        position.add(velocity.scl(delta));
    }

    public void setState(State newState){ this.state=newState; }
    public void setFacingLeft(boolean facingLeft)
    {
        this.facingLeft=facingLeft;
    }
    public Vector2 getVelocity()
    {
        return velocity;
    }

    public Vector2 getAcceleration()
    {
        return acceleration;
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
