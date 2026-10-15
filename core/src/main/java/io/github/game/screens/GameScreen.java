package io.github.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.game.Constants;
import io.github.game.Main;
import io.github.game.Player;
import io.github.game.asset_loaders.GameAssets;
import io.github.game.attack.AttackLibrary;
import io.github.game.attack.Attacker;
import io.github.game.boss.Boss;
import io.github.game.bullets.BulletLibrary;
import io.github.game.bullets.Bullets;
import io.github.game.drawing.DrawFlags;
import io.github.game.drawing.DrawUtil;

public class GameScreen implements Screen {
  private final Main game;
  private final Player player;
  private final ExtendViewport gameViewport;
  private final FillViewport bgViewport;
  private final ScreenViewport uiViewport;
  private final OrthographicCamera cam;
  private final OrthographicCamera bgCam;
  private final OrthographicCamera uiCam;
  private final Rectangle screenRect; // rect that represents visible area on screen
  private final Boss boss;

  public GameScreen(Main game) {
    this.game = game;
    player = new Player();

    // initialize cameras and viewports
    cam = new OrthographicCamera();
    cam.zoom = Constants.CAM_ZOOM;
    bgCam = new OrthographicCamera();
    bgCam.position.set(Constants.WORLD_WIDTH * .5f, Constants.WORLD_HEIGHT * .5f, 0);
    uiCam = new OrthographicCamera();
    uiCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    gameViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, cam);
    bgViewport = new FillViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, bgCam);
    uiViewport = new ScreenViewport(uiCam);

    // initialize screen rect (used for screen boundary collisions)
    screenRect = new Rectangle();

    // cool guy
    boss = new Boss();

    // initialize libraris
    BulletLibrary.init(player, boss);
    AttackLibrary.init(player, boss);

    // initialize the boss's phases now that attack lib is initialized
    boss.initPhases();
  }

  @Override
  public void render(float dt) {
    update(dt);
    draw(dt);
  }

  private void updateScreenRect() {
    float width = gameViewport.getWorldWidth() * cam.zoom;
    float height = gameViewport.getWorldHeight() * cam.zoom;
    float x = cam.position.x - width * .5f;
    float y = cam.position.y - height * .5f;
    screenRect.set(x, y, width, height);
  }

  private void input() {
    if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
      game.setScreen(new MainMenuScreen(game));
    }
    if (Gdx.input.isKeyJustPressed(Keys.P)) {
      respawn();
    }
  }

  private void update(float dt) {
    cam.update();
    bgCam.update();
    updateCamPos(dt);
    updateScreenRect();

    input();
    Attacker.update(player, dt);
    boss.update(player, dt);
    Bullets.update(dt, screenRect, player, boss);
    player.update(dt);
    handleVictory();
    handlePlayerDeath();
    DrawFlags.update(dt);
  }

  private void handlePlayerDeath(){
    if (player.isDead()) respawn();
  }

  private void handleVictory(){
    if (playerWon()) {
      int hitCount = player.getHitCount();
      respawn();
      GameAssets.bgMusic.stop();
      game.setScreen(new WinScreen(game, hitCount));
    }
  }

  private boolean playerWon() {
    return !player.isDead() && boss.isDead() && !Bullets.isActive();
  }

  private void updateCamPos(float dt) {
    float progress = Constants.CAM_LERP_SPEED * dt;
    cam.position.x = MathUtils.lerp(cam.position.x, player.getPos().x, progress);
    cam.position.y = MathUtils.lerp(cam.position.y, player.getPos().y, progress);
  }

  /** handles player death. funeral services NOT included. */
  private void respawn() {
    GameAssets.bgMusic.stop();
    GameAssets.bgMusic.play();
    GameAssets.deathSound.play(Constants.DEATH_SOUND_VOLUME);
    player.handleDeath();
    Attacker.reset();
    Bullets.reset();
    boss.setDefaults();
  }

  private void draw(float dt) {
    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    // draw fixed background
    bgViewport.apply();
    game.batch.setProjectionMatrix(bgCam.combined);
    game.batch.begin();
    game.batch.draw(GameAssets.bg, 0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    game.batch.end();

    // draw game
    gameViewport.apply();
    game.batch.setProjectionMatrix(cam.combined);
    game.batch.begin();
    DrawUtil.drawBridge(game.batch);
    game.batch.end();
    game.shapeRenderer.setProjectionMatrix(cam.combined);
    DrawUtil.drawWarnings(game.shapeRenderer);
    game.batch.begin();
    boss.draw(game.batch);
    player.draw(game.batch);
    Bullets.draw(game.batch);
    game.batch.end();

    // draw ui
    uiViewport.apply();
    game.batch.setProjectionMatrix(uiCam.combined);
    DrawUtil.drawUi(game.batch, game.shapeRenderer, player, boss, uiViewport);
  }

  @Override
  public void show() {
    GameAssets.bgMusic.play();
  }

  @Override
  public void resize(int width, int height) {
    gameViewport.update(width, height);
    bgViewport.update(width, height);
    uiViewport.update(width, height, true);
  }

  @Override
  public void pause() {
    GameAssets.bgMusic.pause();
  }

  @Override
  public void resume() {
    GameAssets.bgMusic.play();
  }

  @Override
  public void hide() {
    GameAssets.bgMusic.pause();
  }

  @Override
  public void dispose() {}
}
