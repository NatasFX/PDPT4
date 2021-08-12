package com.ufsm.rockstar;

import com.ufsm.rockstar.WorldController;
import com.ufsm.rockstar.World;
import com.ufsm.rockstar.WorldRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.ufsm.rockstar.World;
import com.ufsm.rockstar.WorldRenderer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;


public class GameScreen implements Screen, InputProcessor
{
    private World world;
    private WorldRenderer renderer;
    private WorldController controller;

    private int width, height;

    @Override
    public void show()
    {
        world = new World();
        renderer = new WorldRenderer(world,true);
        controller = new WorldController(world);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {

        renderer.setSize(width,height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    //metodos de processamento de input

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.LEFT)
            controller.leftPressed();

        if (keycode == Keys.RIGHT)
            controller.rightPressed();

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Keys.LEFT)
            controller.leftReleased();

        if (keycode == Keys.RIGHT)
            controller.rightReleased();

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
