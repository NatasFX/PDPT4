package com.ufsm.rockstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class utils {

    static public BitmapFont bigFont = new BitmapFont(Gdx.files.internal("fonts/bebas_100.fnt"), false);
    static public BitmapFont mediumFont = new BitmapFont(Gdx.files.internal("fonts/bebas_60.fnt"), false);
    static public BitmapFont smallFont = new BitmapFont(Gdx.files.internal("fonts/bebas.fnt"), false);

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
}
