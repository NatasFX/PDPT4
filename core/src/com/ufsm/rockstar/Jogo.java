package com.ufsm.rockstar;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sun.jvm.hotspot.debugger.cdbg.TemplateType;


public class Jogo implements Screen, InputProcessor {

    UfsmRockstar jogo;
    //screen stuff
    private Camera camera;      //the camera to gdx make their projections
    private Viewport viewport;  //our window to display the game
    private SpriteBatch batch;  //sprite batches

    Color c; //Background color

    //música
    Music music;

    //pontos
    private int score = 0;

    //animação
    Animation<TextureRegion> animacao;

    Array<Texture> pads;
    TextureRegion[][] tiles;
    Texture pad3;
    Texture pad5;

    float[][] tilePositions; //primeira coordenada é de 0 a 4, significa os botoes
    //segunda coordenada é a quantidade de tiles dentro dessa linha, cada um é um float
    //que representa a distancia percorrida (mais longe ou mais perto)

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private boolean pressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    private void pressedPadAction(int padNumber) {

        for (int j = 0; tilePositions[padNumber][j] != 0; j++) {
            float f = tilePositions[padNumber][j];

            if (f == 0) break;

            else if (f > 55 && f < 70) {
                tilePositions[padNumber][j] = 0;
                score++;
            }
        }
    }

    private void advanceTiles() {
        for (int i = 0; i < tilePositions.length; i++) {
            for (int j = 0; j < tilePositions.length; j++) {
                if (tilePositions[i][j] > 0)
                    tilePositions[i][j] +=.45;

                if (tilePositions[i][j] >= 82) {
                    tilePositions[i][j] = 0;    //resetar se passou da borda da tela
                    //colocar aqui a logica de quando perdeu o tile
                }

            }
        }
    }

    public Jogo(UfsmRockstar jogo) {
        this.jogo = jogo;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH, HEIGHT, camera);

        batch = new SpriteBatch();

        c = new Color();

        music = Gdx.audio.newMusic(Gdx.files.internal("music/here.mp3"));
        music.play();
        music.setVolume(.5f);

        animacao = utils.loadDancarino();

        pads = new Array<Texture>();

        for (int i = 1; i <= 5; i++) {
            pads.add(new Texture(Gdx.files.internal("imagens/" + i + ".png")));
        }

        tiles = TextureRegion.split(new Texture(Gdx.files.internal("sprites/tiles.png")),25,12);

        pad3 = new Texture(Gdx.files.internal("imagens/middle_3.png"));
        pad5 = new Texture(Gdx.files.internal("imagens/outs_5.png"));

        tilePositions = new float[5][128];
        tilePositions[0][0] = 1;
        tilePositions[1][0] = 1;
        tilePositions[2][0] = 1;

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(c);

        advanceTiles();

        batch.begin();
        batch.draw(pad5, 100, 25, 600, 340);
        batch.draw(pad3, 100, 25, 600, 340);

        //desenhar pad pressionado
        for (int i = 0; i < 5; i++) {
            if (pressed(Input.Keys.G)) {
                batch.draw(pads.get(0), 100, 25, 600, 340);
                pressedPadAction(0);
            }
            if (pressed(Input.Keys.H)) {
                batch.draw(pads.get(1), 100, 25, 600, 340);
                pressedPadAction(1);
            }
            if (pressed(Input.Keys.J)) {
                batch.draw(pads.get(2), 100, 25, 600, 340);
                pressedPadAction(2);
            }
            if (pressed(Input.Keys.K)) {
                batch.draw(pads.get(3), 100, 25, 600, 340);
                pressedPadAction(3);
            }
            if (pressed(Input.Keys.L)) {
                batch.draw(pads.get(4), 100, 25, 600, 340);
                pressedPadAction(4);
            }
        }

        //desenhar todos os tiles ativos
        for (int i = 0; i < 5; i++) {
            for (int j = 0; tilePositions[i][j] != 0; j++) {

                float d = tilePositions[i][j];

                switch (i) {
                    case 0:
                        batch.draw(tiles[0][(int)(3-d/26)], 270-d*2.5f, 340-d*5, 75,36);
                        break;
                    case 1:
                        batch.draw(tiles[1][(int)(3-d/26)], 310-d/.9f, 340-d*5, 75,36);
                        break;
                    case 2:
                        batch.draw(tiles[2][(int)(3-d/26)], 363, 340-d*5, 75,36 );
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }

            }
        }

        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        //System.out.println(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        //System.out.println(Character.getNumericValue(character));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
