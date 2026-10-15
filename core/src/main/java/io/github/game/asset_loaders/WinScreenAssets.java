package io.github.game.asset_loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class WinScreenAssets {
  public static Skin skin;
  public static BitmapFont comicSans;
  public static GlyphLayout scratchGlyphLayout;

  public static void load() {
    skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    FreeTypeFontGenerator comicSansGen =
        new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comic Sans MS.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter comicSansParams =
        new FreeTypeFontGenerator.FreeTypeFontParameter();
    comicSansParams.color = Color.RED;
    comicSansParams.borderWidth = 4;
    comicSansParams.borderColor = Color.BLACK;
    comicSansParams.size = 32;
    comicSans = comicSansGen.generateFont(comicSansParams);
    comicSansGen.dispose();
    scratchGlyphLayout = new GlyphLayout();
  }

  public static void dispose() {
    skin.dispose();
    comicSans.dispose();
  }
}
