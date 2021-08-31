package com.ufsm.rockstar;


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

class textFader extends Thread {

    WelcomeScreen game;
    int fadingTime;
    int initialSleep;
    String text;

    textFader(WelcomeScreen game, int initialSleep, int fadingTime, String text) {
        this.game = game;
        this.fadingTime = fadingTime;
        this.initialSleep = initialSleep;
        this.text = text;
    }

    private void dormir(long n) {
        try {
            this.sleep(n);
        } catch (InterruptedException ex) {}
    }

    @Override
    public void run() {
        dormir(initialSleep);

        for (float i = 1; i <= fadingTime; i++) {
            dormir(40);
            game.textFader((fadingTime-i)/fadingTime);
        }

        game.rewriteText(text);

        dormir(300);
        for (float i = fadingTime; i > 0; i--) {
            dormir(15);
            game.textFader((fadingTime-i)/fadingTime);
        }

        this.interrupt();
    }

}

class textSync extends Thread {
    private void dormir(long n) {
        try {
            this.sleep(n);
        } catch (InterruptedException ex) {}
    }

    @Override
    public void run() {
        dormir(8460);
        WelcomeScreen.change_bg_color(20);
        WelcomeScreen.rewriteText("BEM-VINDOS");
        dormir(550);
        WelcomeScreen.change_bg_color(30);
        WelcomeScreen.rewriteText("AO");
        dormir(330);
        WelcomeScreen.change_bg_color(30);

        WelcomeScreen.setBgTransitionTime(2f);
        WelcomeScreen.rewriteText("UFSM ROCKSTAR!");
        WelcomeScreen.setShowDancarino(true);

        this.interrupt();
    }

}

class textWriter extends Thread {
    String str, tmp = "";

    BitmapFontCache cache;
    Sound snd;


    textWriter(String str, BitmapFontCache cache){
        this.str = str;
        this.cache = cache;
        snd = Gdx.audio.newSound(Gdx.files.internal("music/type.wav"));
    }

    private boolean dormir(long n) {
        try {
            this.sleep(n);
            return false;
        } catch (InterruptedException ex) {snd.dispose(); return true;}
    }

    @Override
    public void run() {
        for (char r: str.toCharArray()) {
            if (dormir(r == ' ' ? 200 : 80)) {
                break;
            }
            System.out.println(""+r);
            tmp = tmp + r;
            cache.setText(tmp, 75, 525, 0, tmp.length(), 675, 75, true);
            snd.play(1.0f);
        }

    }

}

public class WelcomeScreen implements Screen {

    UfsmRockstar jogo;

    //screen stuff
    private Camera camera;      //the camera to gdx make their projections
    private Viewport viewport;  //our window to display the game

    Color c; //Background color

    //música
    Music music;

    //animação
    Animation<TextureRegion> animacao;
    static boolean showDancarino = false;

    //Text
    BitmapFontCache littleFontCache;
    static BitmapFont font; //font to draw text
    static BitmapFontCache fontCache;  //render texts faster
    ShapeRenderer shape;

    //texture stuff
    private SpriteBatch batch;  //sprite batches

    //timing stuff
    float time = 0;
    static float bgTransitionTime = .5f;
    static float bgBrightness = .8f;

    //debug
    boolean debug = false;

    private static float k = 0;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    Thread txt;

    public static void rewriteText(String text) {
        fontCache.setText(text, 0, 0);
        fontCache.translate(WIDTH/2-utils.textWidth(fontCache)/2, HEIGHT/2+font.getCapHeight()/2);
    }

    public static void textFader(float amount) {
        //sets the current text color
        fontCache.setColors(1, 1, 1, amount);
        //sets the cache color
        fontCache.setColor(1, 1, 1, amount);
    }

    public static void change_bg_color(float add) {
        k += add;
    }

    public static void setShowDancarino(boolean show) {
        showDancarino = show;
    }

    public static void setBgTransitionTime(float t) {
        bgTransitionTime = t;
    }

    private void draw3Dancarinos(float height) {
        batch.begin();
        batch.draw(animacao.getKeyFrame(time, true), WIDTH*1/4-47, height);
        batch.draw(animacao.getKeyFrame(time, true), WIDTH*2/4-47, height);
        batch.draw(animacao.getKeyFrame(time, true), WIDTH*3/4-47, height);
        batch.end();
    }

    public WelcomeScreen(UfsmRockstar jogo) {

        this.jogo = jogo;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH, HEIGHT, camera);

        batch = new SpriteBatch();

        c = new Color();

        font = utils.bigFont;
        fontCache = font.newFontCache();

        music = Gdx.audio.newMusic(Gdx.files.internal("music/intro.mp3"));
        music.play();
        new textSync().start();
        music.setVolume(.4f);

        rewriteText("HNGAMES apresenta");

        animacao = utils.loadDancarino();

        new textFader(this, 12000, 50, "Clique para continuar").start();


        shape = new ShapeRenderer();
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(c.fromHsv(k, .95f , bgBrightness));
        time += delta;

        //System.out.println(time);

        if (k < 360)
            k += bgTransitionTime;
        else
            k = 0;

        batch.begin();
        if (debug)
            utils.smallFont.draw(batch, "FPS: "+(int)(1/delta), 0, HEIGHT);

        fontCache.draw(batch);

        batch.end();

        if (music.getVolume() < .1f) {

            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.BLACK);
            shape.rect(50, 150, 700, 400);
            shape.end();
            batch.begin();
            littleFontCache.draw(batch);
            batch.end();
            if (showDancarino) draw3Dancarinos(WIDTH/15);
        } else if (showDancarino)
            draw3Dancarinos(HEIGHT/4);


        if (Gdx.input.isTouched() && time > 15 && bgBrightness != .1f) {
            new textFader(this, 0, 10, "Carregando...").start();
            bgBrightness = .1f;
            music.setVolume(.07f);
            showDancarino = true;
            littleFontCache = utils.smallFont.newFontCache();
            txt = new textWriter("Bem-vindo jogador!\n\nNesse jogo você é um estudante, que pretende entrar na faculdade do ROCK, e para isso você precisa passar no vestibular do rock.\n" +
                    "\n\nPressione ENTER para ir para o jogo."
                    , littleFontCache);
            txt.start();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F9)) {
            debug = !debug;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            jogo.setScreen(new GameScreen(jogo));           //se o cara pressionar o enter ele vai para o jogo pulando o tutorial
            this.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
    public void show() {

    }

    @Override
    public void dispose() {
        music.dispose();
        shape.dispose();
        batch.dispose();
        txt.interrupt();
    }
}
