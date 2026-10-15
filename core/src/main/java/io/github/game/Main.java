package io.github.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.game.asset_loaders.GameAssets;
import io.github.game.asset_loaders.MainMenuAssets;
import io.github.game.asset_loaders.WinScreenAssets;
import io.github.game.screens.GameScreen;
import io.github.game.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
  public SpriteBatch batch;
  public ShapeRenderer shapeRenderer;
  public GameScreen gameScreen;
  private boolean skipFrame;

  @Override
  public void create() {
    batch = new SpriteBatch();
    shapeRenderer = new ShapeRenderer();
    gameScreen = new GameScreen(this);
    skipFrame = false;
    GameAssets.load();
    MainMenuAssets.load();
    WinScreenAssets.load();
    setScreen(new MainMenuScreen(this));
  }

  @Override
  public void render() {
    float dt = skipFrame ? 0f : Gdx.graphics.getDeltaTime();
    getScreen().render(dt);
  }

  @Override
  public void dispose() {
    batch.dispose();
    GameAssets.dispose();
    MainMenuAssets.dispose();
    WinScreenAssets.dispose();
    getScreen().dispose();
  }

  @Override
  public void pause() {
    skipFrame = true;
  }
}
