package com.ufsm.rockstar;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ufsm.rockstar.World;
import com.ufsm.rockstar.Player;
import com.ufsm.rockstar.Block;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class WorldRenderer {
    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;
    private World world;
    private OrthographicCamera cam;
    ShapeRenderer debugRenderer = new ShapeRenderer();
    ShapeRenderer blockFailRenderer = new ShapeRenderer();
    //private Texture playerTexture;
    private Texture gramaTexture, terraTexture, CantoDTexture,CantoETexture, QuinaDTexture,QuinaETexture;
    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX; // pixels per unit on the X axis
    private float ppuY; // pixels per unit on the Y axis

    public void setSize (int w, int h) {

        this.width = w;

        this.height = h;

        ppuX = (float)width / CAMERA_WIDTH;

        ppuY = (float)height / CAMERA_HEIGHT;

    }



    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();

        loadTextures();
    }



    private void loadTextures() {

        //playerTexture = new  Texture(Gdx.files.internal("images/bob_01.png"));

        gramaTexture = new Texture(Gdx.files.internal("imagens/Grama.png"));
        terraTexture = new Texture(Gdx.files.internal("imagens/Terra.png"));
        CantoDTexture = new Texture(Gdx.files.internal("imagens/CantoDireito.png"));
        CantoETexture = new Texture(Gdx.files.internal("imagens/CantoEsquerdo.png"));
        QuinaDTexture = new Texture(Gdx.files.internal("imagens/QuinaDireita.png"));
        QuinaETexture = new Texture(Gdx.files.internal("imagens/QuinaEsquerda.png"));

    }



    public void render() {
        drawBackground();
        spriteBatch.begin();
        drawBlocks();
        //drawPlayer();
        spriteBatch.end();
        if (debug)
            drawDebug();
    }

    private void drawBackground()
    {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        for (Block block : world.getBlocks()) {
            Rectangle rect = block.getBounds();
            float x1 = block.getPosition().x + rect.x;
            float y1 = block.getPosition().y + rect.y;
            debugRenderer.setColor(new Color(0.78f, 0.69f, 0.545f, 1));
            debugRenderer.rect(x1, y1, rect.width, rect.height);
        }
        debugRenderer.end();
    }

    private void drawBlocks() {
        for (Block block : world.getBlocks()) {
            if (block.getName().equals("Grama"))
                spriteBatch.draw(gramaTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);

            if (block.getName().equals("Terra"))
                spriteBatch.draw(terraTexture, block.getPosition().x * ppuX, (block.getPosition().y * ppuY)+5, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }
    }



    private void drawPlayer() {

        Player player = world.getPlayer();

        //spriteBatch.draw(playerTexture, player.getPosition().x * ppuX, player.getPosition().y * ppuY, player.SIZE * ppuX, player.SIZE * ppuY);

    }


    private void drawDebug() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        for (Block block : world.getBlocks()) {
            Rectangle rect = block.getBounds();
            float x1 = block.getPosition().x + rect.x;
            float y1 = block.getPosition().y + rect.y;
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(x1, y1, rect.width, rect.height);
        }
        Player player = world.getPlayer();
        Rectangle rect = player.getBounds();
        float x1 = player.getPosition().x + rect.x;
        float y1 = player.getPosition().y + rect.y;
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(x1, y1, rect.width, rect.height);
        debugRenderer.end();
    }
}


//tileset que usei https://akumalab.itch.io/16x16-platform-rpg-tileset