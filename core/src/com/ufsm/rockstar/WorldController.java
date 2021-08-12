package com.ufsm.rockstar;

import java.util.HashMap;
import java.util.Map;

import com.ufsm.rockstar.Player.State;
import com.ufsm.rockstar.Player;
import com.ufsm.rockstar.World;
public class WorldController {
    enum Keys {
        LEFT, RIGHT
    }
    private World  world;
    private Player  player;
    static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();

    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
    };

    public WorldController(World world) {
        this.world = world;
        this.player = world.getPlayer();
    }

    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }
    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }
    public void leftReleased() {
        keys.get(keys.put(Keys.LEFT, false));
    }
    public void rightReleased() {
        keys.get(keys.put(Keys.RIGHT, false));
    }
    /** The main update method **/

    public void update(float delta) {
        processInput();
        player.update(delta);
    }

    private void processInput() {
        if (keys.get(Keys.LEFT)) {
            player.setFacingLeft(true);
            player.setState(State.WALKING);
            player.getVelocity().x = -player.SPEED;
        }

        if (keys.get(Keys.RIGHT)) {
            player.setFacingLeft(false);
            player.setState(State.WALKING);
            player.getVelocity().x = player.SPEED;
        }
        if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) || (!keys.get(Keys.LEFT) && !(keys.get(Keys.RIGHT)))) {
            player.setState(State.IDLE);
            // acceleration is 0 on the x
            player.getAcceleration().x = 0;
            // horizontal speed is 0
            //bob.getVelocity().x = 0;

        }

    }

}
