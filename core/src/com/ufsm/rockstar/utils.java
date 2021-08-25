package com.ufsm.rockstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.Animation;

public class utils {

    static public BitmapFont bigFont = new BitmapFont(Gdx.files.internal("fonts/bebas_100.fnt"), false);
    static public BitmapFont mediumFont = new BitmapFont(Gdx.files.internal("fonts/bebas_60.fnt"), false);
    static public BitmapFont smallFont = new BitmapFont(Gdx.files.internal("fonts/bebas_36.fnt"), false);

    public static float textWidth(BitmapFontCache cache) {
        float sum = 0;
        for (GlyphLayout r: cache.getLayouts()) sum += r.width;
        return sum;
    }

    public static float textWidth(BitmapFont font, String text) {
        float sum = 0;
        BitmapFontCache c = font.newFontCache();
        c.setText(text, 0, 0);

        for (GlyphLayout r: c.getLayouts()) sum += r.width;
        return sum;
    }
    public static float textWidth(BitmapFont font, String text, int offsetX) {
        float sum = 0;
        BitmapFontCache c = font.getCache();
        c.setText(text, 0, 0);

        for (GlyphLayout r: c.getLayouts()) sum += r.width;
        return sum-offsetX*2;
    }

    public static Animation loadDancarino() {
        TextureRegion[][] frames = TextureRegion.split(new Texture(Gdx.files.internal("sprites/dancando.png")),95,82);
        Array framesDaAnimacao = new Array();

        //matrix to 1d array
        for (int i=0; i<5;i++)
            for (int j=0;j<4;j++)
                framesDaAnimacao.add(frames[j][i]);

        return new Animation(1f/6f, framesDaAnimacao);
    }
}
