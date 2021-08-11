package com.ufsm.rockstar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World
{
    /** The blocks making up the world **/

    Array<Block> blocks = new Array<Block>();

    /** Our player controlled hero **/

    Player player;



    // Getters -----------

    public Array<Block> getBlocks() {

        return blocks;

    }

    public Player getPlayer() {

        return player;

    }
    // --------------------

    public World() {
        createDemoWorld();
    }

    private void createDemoWorld()
    {
        player = new Player(new Vector2(7, 2));

        for (int i = 0; i < 20; i++) {
            blocks.add(new Block(new Vector2(i, 0),"Terra"));
            blocks.add(new Block(new Vector2(i, 1),"Grama"));
        }

    }
}
