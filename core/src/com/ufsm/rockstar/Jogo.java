package com.ufsm.rockstar;

import static java.lang.Math.abs;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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

import java.io.DataInputStream;
import java.io.IOException;


public class Jogo implements Screen, InputProcessor {

    UfsmRockstar jogo;
    //screen stuff
    private Camera camera;      //the camera to gdx make their projections
    private Viewport viewport;  //our window to display the game
    private SpriteBatch batch;  //sprite batches
    float elapsed = 0;

    Color c; //Background color

    //música
    Music music;
    int[][] musicSync;
    int musicPos = 0;
    int totalTiles;
    int[] remiss;


    //pontos
    private int score = 0;
    private int missedTiles = 0;
    private int consecutivos = 0;

    BitmapFont font;
    BitmapFontCache fontCache;

    //animação
    Animation<TextureRegion> bg;
    Animation<Texture>[] padsAnim;
    Animation<TextureRegion> fireAnim;
    float[] timingsAnim;

    //pads
    Array<Texture> pads;
    TextureRegion[][] tiles;
    Texture pad3;
    Texture pad5;

     //lógica de pausa
    boolean pausa = true;


    float[][] tilePositions; //primeira coordenada é de 0 a 4, significa os botoes
    //segunda coordenada é a quantidade de tiles dentro dessa linha, cada um é um float
    //que representa a distancia percorrida (mais longe ou mais perto)

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private void startGame() {
        score = 0;
        missedTiles = 0;
        consecutivos = 0;
        musicPos = 0;
    }

    private boolean pressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    private void pressedPadAction(int padNumber) {

        for (int j = 0; j < tilePositions[padNumber].length; j++) {
            float f = tilePositions[padNumber][j];

            if (f > 50 && f < 65) {
                tilePositions[padNumber][j] = 0;    //limpa o tile
                score++;                            //aumenta o score
                remiss[padNumber] = 1;              //reseta tempo de remiss
                consecutivos += 1;                  //aumenta pontos consecutivos
                if (timingsAnim[padNumber] > .45)   //inicia animação
                    timingsAnim[padNumber] = 0;
            } else {
                if (remiss[padNumber]++ == 0) {
                    //essa lógica de remiss é para evitar misses enormes ao pressionar o botão
                    missedTiles++;
                    consecutivos = 0;
                } else if (remiss[padNumber] < 3500)
                    remiss[padNumber]++;
                else
                    remiss[padNumber] = 0;
            }
        }
    }

    private void advanceTiles() {
        for (int i = 0; i < tilePositions.length; i++) {
            for (int j = 0; j < tilePositions[i].length; j++) {
                if (tilePositions[i][j] > 0)
                    tilePositions[i][j] += .425;

                if (tilePositions[i][j] >= 82) {
                    tilePositions[i][j] = 0;    //resetar se passou da borda da tela
                    missedTiles++;
                    consecutivos = 0;
                }

            }
        }
    }

    private void addTile(int padPos) {
        for (int i = 0; i < 32; i++)
            if (tilePositions[padPos][i] == 0) {
                tilePositions[padPos][i] = 1;
                break;
            }
    }

    private int[][] readTiles() {
        int[][] times = new int[32768][2];
        int pos = 0;
        try {
            DataInputStream in = new DataInputStream(Gdx.files.internal("timestamps_here").read());
            try {
                while (true){
                    totalTiles += 1;
                    times[pos][0] = in.readInt();
                    times[pos][1] = in.readInt();
                    pos++;
                }
            } catch (IOException _) {
            }
            in.close();
        } catch (IOException _) {
        }

        System.out.println("tiles lidos" + totalTiles);

        return times;
    }

    public Jogo(UfsmRockstar jogo){
        this.jogo = jogo;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH, HEIGHT, camera);

        batch = new SpriteBatch();

        c = new Color();

        music = Gdx.audio.newMusic(Gdx.files.internal("music/here.mp3"));
        music.setVolume(.5f);

        //animacao = utils.loadDancarino();

        pads = new Array<Texture>();

        for (int i = 1; i <= 5; i++) {
            pads.add(new Texture(Gdx.files.internal("imagens/" + i + ".png")));//, Pixmap.Format.valueOf("LuminanceAlpha"), false));
        }

        tiles = TextureRegion.split(new Texture(Gdx.files.internal("sprites/tiles.png")),25,12);

        pad3 = new Texture(Gdx.files.internal("imagens/middle_3.png"));
        pad5 = new Texture(Gdx.files.internal("imagens/outs_5.png"));

        tilePositions = new float[5][32];
        musicSync = readTiles();

        font = utils.mediumFont;
        fontCache = font.newFontCache();
        fontCache.setText("Aperte ESPAÇO para tocar a música", 0,0);
        fontCache.translate(WIDTH/2-utils.textWidth(fontCache)/2, HEIGHT/2+font.getCapHeight()/2);

        bg = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("imagens/bg.gif").read());

        Array framesDaAnimacao0 = new Array();
        Array framesDaAnimacao1 = new Array();
        Array framesDaAnimacao2 = new Array();
        Array framesDaAnimacao3 = new Array();
        Array framesDaAnimacao4 = new Array();

        //sim, isso aqui vai fazer o programa consumir 565999 mb de memória
        //mas se roda na minha máquina de 12gb de ram, roda em todas as outras :)
        for (int i = 0; i < 27; i++) {
            framesDaAnimacao0.add(new Texture(Gdx.files.internal("imagens/0/0_0000" + String.format("%02d", i) + ".png")));
            framesDaAnimacao1.add(new Texture(Gdx.files.internal("imagens/1/1_0000" + String.format("%02d", i) + ".png")));
            framesDaAnimacao2.add(new Texture(Gdx.files.internal("imagens/2/2_0000" + String.format("%02d", i) + ".png")));
            framesDaAnimacao3.add(new Texture(Gdx.files.internal("imagens/3/3_0000" + String.format("%02d", i) + ".png")));
            framesDaAnimacao4.add(new Texture(Gdx.files.internal("imagens/4/4_0000" + String.format("%02d", i) + ".png")));
        }

        padsAnim = new Animation[5];

        padsAnim[0] = new Animation(1f/60f, framesDaAnimacao0);
        padsAnim[1] = new Animation(1f/60f, framesDaAnimacao1);
        padsAnim[2] = new Animation(1f/60f, framesDaAnimacao2);
        padsAnim[3] = new Animation(1f/60f, framesDaAnimacao3);
        padsAnim[4] = new Animation(1f/60f, framesDaAnimacao4);

        Array toUnroll = new Array();
        TextureRegion[][] frames = TextureRegion.split(new Texture(Gdx.files.internal("sprites/fire.png")), 23, 33);

        //matrix to 1d array
        for (int i=0; i<1;i++)
            for (int j=0;j<5;j++)
                toUnroll.add(frames[i][j]);

        fireAnim = new Animation(1f/24f, toUnroll);

        remiss = new int[5];
        timingsAnim = new float[5];
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(c);
        elapsed += delta;
        timingsAnim[0] += delta;
        timingsAnim[1] += delta;
        timingsAnim[2] += delta;
        timingsAnim[3] += delta;
        timingsAnim[4] += delta;

        if (pressed(Input.Keys.ESCAPE)) {
            pausa = !pausa;
        }

//        if (pausa) {
//
//            return;
//        }

        if (music.getPosition()*1000 > abs(musicSync[musicPos][0]-2050) && musicPos < totalTiles-1) {
            musicPos++;
            if (music.getPosition()*1000 < musicSync[musicPos][0]-2050) {
                //esse 2050 é o offset. por enquanto estático, mas se mudar a velocidade vai mudar isso tbm
                addTile(musicSync[musicPos][1]);
            }
        }

        advanceTiles();

        batch.begin();
        batch.draw(bg.getKeyFrame(elapsed), 0f, 0f);


        batch.draw(pad5, 100, 25, 600, 340);
        batch.draw(pad3, 100, 25, 600, 340);

        if (pressed(Input.Keys.SPACE) && !music.isPlaying()) {  //usuário iniciou a música
            music.play();
            startGame();
        } else if (!music.isPlaying()) {    //mostrar texto para usuário apertar espaço
            fontCache.draw(batch);
        } else {        //usuário está jogando
            utils.smallFont.draw(batch, "Pontos: " + score, 0, HEIGHT); //total de pontos
            utils.smallFont.draw(batch, "Perdidos: " + missedTiles, 0, HEIGHT-27); //tiles perdidos
            utils.smallFont.draw(   //desenho da porcentagem de acertos
                    batch,
                    "Acertos: "
                    + (int)(score/((score+missedTiles == 0f) ? 1f : score+missedTiles)*100)
                    + "%",
                    0,
                    HEIGHT-55
            );
            utils.smallFont.draw(   //desenho do tempo decorrido
                    batch,
                    "Tempo: " + String.format("%02d",
                    (int) music.getPosition()/60) + ":"
                    + String.format("%02d", (int)music.getPosition()%60),
                    0,
                    HEIGHT-82
            );
            utils.smallFont.draw(batch, "Pontos consecutivos: " + consecutivos, 0, HEIGHT-109); //pontos consecutivos
            utils.smallFont.draw(batch, "FPS: " +(int)(1/delta), 0, HEIGHT-136); //FPS

        }

        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //desenhar pad pressionado

        for (int i = 0; i < 5; i++) {

            if (!padsAnim[i].isAnimationFinished(timingsAnim[i])) {
                batch.draw(fireAnim.getKeyFrame(elapsed, true), 95 + 123 * i, 35, 115, 115);
                batch.draw(padsAnim[i].getKeyFrame(timingsAnim[i], false), 0f, 0f);
            }

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
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //desenhar todos os tiles ativos
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 32; j++) {

                float d = tilePositions[i][j];

                if (d == 0)
                    continue;

                switch (i) {
                    case 0:
                        batch.draw(tiles[0][(int)(2.9999-d/26)], 270-d*2.5f, 340-d*5, 75,36);
                        break;
                    case 1:
                        batch.draw(tiles[1][(int)(2.9999-d/26)], 310-d/.9f, 340-d*5, 75,36);
                        break;
                    case 2:
                        batch.draw(tiles[2][(int)(2.9999-d/26)], 363, 340-d*5, 75,36);
                        break;
                    case 3:
                        batch.draw(tiles[3][(int)(2.9999-d/26)], 410+d/.8f, 340-d*5, 75,36);
                        break;
                    case 4:
                        batch.draw(tiles[4][(int)(2.9999-d/26)], 453+d*2.5f, 340-d*5, 75,36);
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
