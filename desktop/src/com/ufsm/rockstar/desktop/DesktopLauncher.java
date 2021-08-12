package com.ufsm.rockstar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ufsm.rockstar.UfsmRockstar;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new LwjglApplication(new UfsmRockstar(), "UFSM ROCKSTAR", 800, 600);
		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//new LwjglApplication(new UfsmRockstar(), config);
	}
}
