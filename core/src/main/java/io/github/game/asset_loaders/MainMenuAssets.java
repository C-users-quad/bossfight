package io.github.game.asset_loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainMenuAssets {
  public static Skin skin;
  public static Texture bg;

  public static void load() {
    skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    bg = new Texture("backgrounds/beaut.jpeg");
  }

  public static void dispose() {
    skin.dispose();
    bg.dispose();
  }
}
