package io.github.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.game.Main;
import io.github.game.asset_loaders.MainMenuAssets;

public class MainMenuScreen implements Screen {
  private Stage stage;
  private Main game;

  public MainMenuScreen(Main game) {
    this.game = game;
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    TextButton playButton = new TextButton("play game", MainMenuAssets.skin);
    TextButton infoButton = new TextButton("info", MainMenuAssets.skin);
    TextButton quitButton = new TextButton("quit game", MainMenuAssets.skin);
    playButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            game.setScreen(game.gameScreen);
          }
        });
    infoButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            Dialog infoDialog = new Dialog("", MainMenuAssets.skin);

            Table content = infoDialog.getContentTable();
            content.defaults().pad(4);
            content.add(new Label("Move left with A, right with D.", MainMenuAssets.skin)).row();
            content.add(new Label("Hold SPACE to fly.", MainMenuAssets.skin)).row();
            content
                .add(new Label("Hold LEFT MOUSE BUTTON to attack the boss.", MainMenuAssets.skin))
                .row();
            content
                .add(
                    new Label(
                        "Press RIGHT MOUSE BUTTON while moving to dash.", MainMenuAssets.skin))
                .row();
            content.add(new Label("Press LEFT SHIFT to heal.", MainMenuAssets.skin)).row();
            content.add(new Label("press P to reset game.", MainMenuAssets.skin)).row();
            content.add(new Label("press ESC in-game to go back to this screen.", MainMenuAssets.skin)).row();
            infoDialog.button("got it");
            infoDialog.pad(10);
            infoDialog.show(stage);
          }
        });
    quitButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            Gdx.app.exit();
          }
        });

    Table table = new Table();
    table.setFillParent(true);
    table.center();
    table.add(playButton);
    table.row();
    table.add(infoButton).padTop(10);
    table.row();
    table.add(quitButton).padTop(10);
    stage.addActor(table);
  }

  @Override
  public void render(float dt) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    game.batch.setProjectionMatrix(stage.getCamera().combined);
    game.batch.begin();
    game.batch.draw(
        MainMenuAssets.bg, 0, 0, stage.getCamera().viewportWidth, stage.getCamera().viewportHeight);
    game.batch.end();
    stage.act(dt);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void show() {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void hide() {}

  @Override
  public void dispose() {
    stage.dispose();
  }
}
