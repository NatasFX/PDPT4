package com.ufsm.rockstar;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;             ///PPRECISA DISSO AQUI TBM PRA ANIMAÇÃO
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ufsm.rockstar.World;
import com.ufsm.rockstar.Player;
import com.ufsm.rockstar.Block;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class WorldRenderer {
    private static final float CAMERA_WIDTH = 20f;
    private static final float CAMERA_HEIGHT = 14f;
    private World world;
    private int fase=0, faseAnterior=-1;
    private OrthographicCamera cam;
    ShapeRenderer debugRenderer = new ShapeRenderer();
    ShapeRenderer blockFailRenderer = new ShapeRenderer();

    TextureRegion[] framesDaAnimacao;       /////ESSAS DECLARAÇÃO AQUI ANIMAÇÃO
    Animation animacao;
    float tempoDecorrido;
    float escurecedor=0;

    BitmapFont font;

    private TextureRegion playerParado;
    private Texture playerTexture;
    private Texture gramaTexture;
    private Texture city;
    private Texture gameBuilding;
    private Texture terraTexture;
    private Texture CantoDTexture;
    private Texture CantoETexture;
    private Texture QuinaDTexture;
    private Texture QuinaETexture;
    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX; // pixels per unit on the X axis
    private float ppuY; // pixels per unit on the Y axis

    public void setSize (int w, int h)
    {

        this.width = w;

        this.height = h;

        ppuX = (float)width / CAMERA_WIDTH;

        ppuY = (float)height / CAMERA_HEIGHT;

    }



    public WorldRenderer(World world, boolean debug)
    {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();

        font = new BitmapFont(Gdx.files.internal("fonts/bebas_100.fnt"), false);

        loadTextures();
    }


    private void loadTextures()
    {

        playerTexture = new Texture(Gdx.files.internal("sprites/Player.png"));
        criaAnimacao();                                                             // ESSES 2 ANIMAÇÃO
        city = new Texture(Gdx.files.internal("imagens/16617.jpg"));
        gameBuilding = new Texture(Gdx.files.internal("imagens/3480.png"));
        gramaTexture = new Texture(Gdx.files.internal("imagens/Grama.png"));
        terraTexture = new Texture(Gdx.files.internal("imagens/Terra.png"));
        CantoDTexture = new Texture(Gdx.files.internal("imagens/CantoDireito.png"));
        CantoETexture = new Texture(Gdx.files.internal("imagens/CantoEsquerdo.png"));
        QuinaDTexture = new Texture(Gdx.files.internal("imagens/QuinaDireita.png"));
        QuinaETexture = new Texture(Gdx.files.internal("imagens/QuinaEsquerda.png"));

    }

    ////////////////////////ANIMAÇÃO//////////////////////
    private void criaAnimacao()
    {
        TextureRegion[][] frames = TextureRegion.split(playerTexture,128,163);
        framesDaAnimacao = new TextureRegion[8];
        int index = 0, j=0;

        for (int i=0; i<8;i++)
        {
            framesDaAnimacao[index++] = frames[0][i];
        }
        playerParado = new TextureRegion(framesDaAnimacao[0]);
        animacao = new Animation(1f/8f,framesDaAnimacao);
    }
    ///////////////ANIMAÇÃO////////////////////

    public void render()
    {
        tempoDecorrido+= Gdx.graphics.getDeltaTime();           ///animação

        spriteBatch.begin();

        spriteBatch.draw(city,0,4*ppuY-10,800,500);
        drawBlocks();
        if(debug) {
            font.draw(spriteBatch, fase+"" ,400,500);
        }
        if (fase==0)
        {
            spriteBatch.draw(gameBuilding,11*ppuX,4*ppuY-10,320, 240);
        }
        drawPlayer();

        spriteBatch.end();
        drawBackground();
        if (debug)
            drawDebug();
    }

    private void drawBackground()
    {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        for (Block block : world.getBlocks()) {
            Rectangle rect = block.getBounds();
            float x1 = block.getPosition().x + rect.x;
            float y1 = block.getPosition().y + rect.y;
            debugRenderer.setColor(new Color(0.78f+(escurecedor), 0.69f+(escurecedor), 0.545f+(escurecedor*1.5f), 0.4f));
            debugRenderer.rect(x1, y1, rect.width, rect.height);

        }
        debugRenderer.setColor(new Color(0.78f+(escurecedor), 0.69f+(escurecedor), 0.545f+(escurecedor*1.5f), 0.4f));

        debugRenderer.rect(0, 4, 800, 500);

        debugRenderer.end();


    }

    private void drawBlocks()
    {
        for (Block block : world.getBlocks())
        {
            if (block.getName().equals("Grama"))
                spriteBatch.draw(gramaTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX+10, Block.SIZE * ppuY);

            if (block.getName().equals("Terra"))
                spriteBatch.draw(terraTexture, block.getPosition().x * ppuX, (block.getPosition().y * ppuY)+5, Block.SIZE * ppuX+10, Block.SIZE * ppuY+10);
        }
    }



    private void drawPlayer()
    {
        Player player = world.getPlayer();

        if (player.getState() == Player.State.WALKING)
            spriteBatch.draw((TextureRegion) animacao.getKeyFrame(tempoDecorrido,true), player.getPosition().x * ppuX, player.getPosition().y * ppuY, player.SIZE * ppuX, player.SIZE * ppuY);
        else
            spriteBatch.draw(playerParado, player.getPosition().x * ppuX, player.getPosition().y * ppuY, player.SIZE * ppuX, player.SIZE * ppuY);

        if (player.getPosition().x*ppuX <= 0 && fase == 0)
            player.setPosition(0);
        else if (player.getPosition().x*ppuX <= -40 && fase > 0)             // verifica se o personagem sai da tela pela esquerda
        {
            player.setPosition(19);                     //magica da programação não mudar
            faseAnterior=fase;
            fase--;
            escurecedor+=0.15f;
        }

        if (player.getPosition().x*ppuX >= 800)
        {
            player.setPosition(-1);
            faseAnterior=fase;
            fase++;
            escurecedor-=0.15f;
        }

    }


    private void drawDebug()
    {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        for (Block block : world.getBlocks())
        {
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
// city background <a href='https://www.freepik.com/vectors/background'>Background vector created by katemangostar - www.freepik.com</a>
// rockista dançarino https://www.spriters-resource.com/fullview/18936/
//tileset que usei https://akumalab.itch.io/16x16-platform-rpg-tileset
//https://www.pngwing.com/pt/free-png-xkfra player
// link do predio https://www.freepik.com/free-vector/building-icons_1528904.htm#page=9&query=2d%20building&position=27
