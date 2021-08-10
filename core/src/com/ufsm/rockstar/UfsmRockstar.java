package com.ufsm.rockstar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;

import com.ufsm.rockstar.GameScreen;

public class UfsmRockstar extends Game {
	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
