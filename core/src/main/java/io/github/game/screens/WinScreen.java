package io.github.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.game.Main;
import io.github.game.asset_loaders.WinScreenAssets;

/**
 * EXISTS TO DIE.
 * LOVES IT.
 */
public class WinScreen implements Screen {
  private Main game;
  private int numHits;
  private Stage stage;

  public WinScreen(Main game, int numHits) {
    this.game = game;
    this.numHits = numHits;
    this.stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(this.stage);

    TextButton playAgainButton = new TextButton("play again", WinScreenAssets.skin);
    playAgainButton.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          game.setScreen(game.gameScreen);
        }
      });

    Table table = new Table();
    table.setFillParent(true);
    table.center();
    table.add(playAgainButton);
    stage.addActor(table);
  }

  @Override
  public void show() {}

  @Override
  public void render(float dt) {
    Gdx.gl.glClearColor(0, 1, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.getViewport().apply();
    game.batch.setProjectionMatrix(stage.getCamera().combined);
    game.batch.begin();
    // draw text
    float winW = stage.getViewport().getWorldWidth();
    float winH = stage.getViewport().getWorldHeight();
    WinScreenAssets.scratchGlyphLayout.setText(WinScreenAssets.comicSans, "you win!!");
    float winTxtX = winW * .5f - WinScreenAssets.scratchGlyphLayout.width * .5f;
    float winTxtY = winH - 10f - WinScreenAssets.scratchGlyphLayout.height;
    WinScreenAssets.comicSans.draw(game.batch, WinScreenAssets.scratchGlyphLayout, winTxtX, winTxtY);
    WinScreenAssets.scratchGlyphLayout.setText(
      WinScreenAssets.comicSans,
      String.format("You got hit %d times!", numHits)
    );
    float hitTxtX = winW * .5f - WinScreenAssets.scratchGlyphLayout.width * .5f;
    float hitTxtY = winTxtY - 10f - WinScreenAssets.scratchGlyphLayout.height;
    WinScreenAssets.comicSans.draw(game.batch, WinScreenAssets.scratchGlyphLayout, hitTxtX, hitTxtY);
    game.batch.end();
    stage.act(dt);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {}
}
