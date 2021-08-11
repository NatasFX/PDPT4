package com.ufsm.rockstar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Block
{
    static final float SIZE = 1f;
    Vector2  position = new Vector2();
    Rectangle  bounds = new Rectangle();
    String name;
    public Vector2 getPosition()
    {
        return position;
    }
    public Rectangle getBounds()
    {
        return bounds;
    }
    public String getName(){ return name; }

    public Block(Vector2 pos,String name)
    {
        this.name=name;
        this.position = pos;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }

}
