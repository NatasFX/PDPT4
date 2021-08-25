package com.ufsm.rockstar;

import com.badlogic.gdx.Game;

import com.ufsm.rockstar.GameScreen;

public class UfsmRockstar extends Game {
	@Override
	public void create() {
        setScreen(new Jogo(this));
//		setScreen(new WelcomeScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
