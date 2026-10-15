package io.github.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.game.Constants;
import io.github.game.Player;
import io.github.game.asset_loaders.GameAssets;
import io.github.game.attack.Attack;
import io.github.game.attack.AttackType;
import io.github.game.attack.Attacker;
import io.github.game.boss.Boss;
import io.github.game.bullets.BulletPatterns;

/** not good. works. ughhhhhhhhhh. im not. gonna. refactor. */
public class DrawUtil {
  public static void drawBridge(SpriteBatch batch) {
    float w = Constants.BRIDGE_DRAW_SCALE * GameAssets.bridge.getWidth() / Constants.PPM;
    float h = Constants.BRIDGE_DRAW_SCALE * GameAssets.bridge.getHeight() / Constants.PPM;
    float x = -Constants.NUM_BRIDGE_SEGMENTS * .5f * w;
    Vector2 pos;
    for (int i = 0; i < Constants.NUM_BRIDGE_SEGMENTS; i++) {
      pos = AnchorPoints.getAnchoredPoint(AnchorPoints.MIDTOP, x + w * i, Constants.FLOOR_Y, w, h);
      batch.draw(GameAssets.bridge, pos.x, pos.y, w, h);
    }
  }

  public static void drawUi(
      SpriteBatch batch,
      ShapeRenderer shapeRenderer,
      Player player,
      Boss boss,
      ScreenViewport uiViewport) {
    // common constants used for drawing (aliasing them)
    float padding = Constants.UI_PADDING;
    float winW = uiViewport.getWorldWidth();
    float winH = uiViewport.getWorldHeight();

    // draw health bar
    float healthBarFrameW = GameAssets.healthBar.getWidth();
    float healthBarFrameH = GameAssets.healthBar.getHeight();
    // draw red health rectangle bar and background
    float healthBarW =
        MathUtils.map(0f, Constants.PLAYER_HEALTH, 0f, healthBarFrameW, player.getHealth());
    float healthBarX = padding;
    float healthBarY = winH - padding - healthBarFrameH;
    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(Constants.BAR_BG_COLOR);
    shapeRenderer.rect(healthBarX, healthBarY, healthBarFrameW, healthBarFrameH);
    shapeRenderer.setColor(Constants.HEALTH_BAR_COLOR);
    shapeRenderer.rect(healthBarX, healthBarY, healthBarW, healthBarFrameH);
    shapeRenderer.end();
    // draw bar frame
    batch.begin();
    batch.draw(GameAssets.healthBar, healthBarX, healthBarY);
    batch.end();
    // draw heal bar
    // draw heal bar clock
    float healIndicatorW = GameAssets.healIndicator.getWidth();
    float healIndicatorH = GameAssets.healIndicator.getHeight();
    float healIndicatorX = padding + padding;
    float healIndicatorY = healthBarY - padding - healIndicatorH;
    // draw heal bar background and heal bar progression bar
    float healBarFrameW = GameAssets.healBar.getWidth() * .75f;
    float healBarFrameH = GameAssets.healBar.getHeight();
    float healBarFrameX = healIndicatorX + padding;
    float healBarFrameY = healIndicatorY;
    float healBarW =
        MathUtils.map(
            Constants.PLAYER_HEAL_COOLDOWN_TIME,
            0f,
            0f,
            healBarFrameW,
            player.getHealCooldownTime());
    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(Constants.BAR_BG_COLOR);
    shapeRenderer.rect(healBarFrameX, healBarFrameY, healBarFrameW, healBarFrameH);
    shapeRenderer.setColor(Constants.HEAL_BAR_COLOR);
    shapeRenderer.rect(healBarFrameX, healBarFrameY, healBarW, healBarFrameH);
    shapeRenderer.end();
    // draw flight bar
    float flightBarFrameW = GameAssets.flightBar.getWidth() * .75f;
    float flightBarFrameH = GameAssets.flightBar.getHeight();
    float flightIndicatorW = GameAssets.flightIndicator.getWidth();
    float flightIndicatorH = GameAssets.flightIndicator.getHeight();
    float flightIndicatorX = padding + padding;
    float flightIndicatorY = healIndicatorY - padding - flightIndicatorH;
    float flightBarFrameX = flightIndicatorX + padding;
    float flightBarFrameY = flightIndicatorY;
    float flightBarW =
        MathUtils.map(
            Constants.PLAYER_FLIGHT_TIME, 0f, 0f, flightBarFrameW, player.getFlightTime());
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(Constants.BAR_BG_COLOR);
    shapeRenderer.rect(flightBarFrameX, flightBarFrameY, flightBarFrameW, flightBarFrameH);
    shapeRenderer.setColor(Constants.FLIGHT_BAR_COLOR);
    shapeRenderer.rect(flightBarFrameX, flightBarFrameY, flightBarW, flightBarFrameH);
    shapeRenderer.end();
    batch.begin();
    batch.draw(
        GameAssets.flightBar, flightBarFrameX, flightBarFrameY, flightBarFrameW, flightBarFrameH);
    batch.draw(
        GameAssets.flightIndicator,
        flightIndicatorX,
        flightIndicatorY,
        flightIndicatorW,
        flightIndicatorH);
    batch.end();
    // draw boss bar
    // draw boss bar bg
    float bossBarFrameW = GameAssets.bossBar.getWidth();
    float bossBarFrameH = GameAssets.bossBar.getHeight();
    float bossBarFrameX = winW * .5f - bossBarFrameW * .5f;
    float bossBarFrameY = padding;
    if (!boss.isDead()) {
      shapeRenderer.begin(ShapeType.Filled);
      shapeRenderer.setColor(Constants.BAR_BG_COLOR);
      shapeRenderer.rect(bossBarFrameX, bossBarFrameY, bossBarFrameW, bossBarFrameH);
      // draw boss actual health bar
      float bossBarW =
          MathUtils.map(0f, Constants.BOSS_HEALTH, 0f, bossBarFrameW, boss.getHealth());
      shapeRenderer.setColor(Constants.HEALTH_BAR_COLOR);
      shapeRenderer.rect(bossBarFrameX, bossBarFrameY, bossBarW, bossBarFrameH);
      shapeRenderer.end();
    }
    // draw hitcount
    GameAssets.scratchGlyphLayout.setText(
        GameAssets.comicSans, String.format("Hit Count: %d", player.getHitCount()));
    float hitCountW = GameAssets.scratchGlyphLayout.width;
    float hitCountH = GameAssets.scratchGlyphLayout.height;
    float hitCountX = winW - padding - hitCountW;
    float hitCountY = winH - padding - hitCountH;
    // draw sum other shii
    batch.begin();
    if (!boss.isDead()) batch.draw(GameAssets.bossBar, bossBarFrameX, bossBarFrameY);
    GameAssets.comicSans.draw(batch, GameAssets.scratchGlyphLayout, hitCountX, hitCountY);
    batch.draw(GameAssets.healBar, healBarFrameX, healBarFrameY, healBarFrameW, healBarFrameH);
    batch.draw(GameAssets.healIndicator, healIndicatorX, healIndicatorY, healIndicatorW, healIndicatorH);
    drawFlagged(batch, uiViewport);
    batch.end();
  }

  private static void drawHitFlash(SpriteBatch batch, Viewport viewport) {
    float alpha = MathUtils.map(0f, Constants.HIT_FLASH_DURATION, 0f, 1f, DrawFlags.hitFlashTimer);
    batch.setColor(1f, 1f, 1f, alpha);
    batch.draw(GameAssets.hitFlash, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
    batch.setColor(1f, 1f, 1f, 1f);
  }

  /**
   * draws lines in the form of the rose thats about to spawn
   *
   * @param shapeRenderer
   * @param a
   */
  private static void drawRoseCurveWarning(ShapeRenderer shapeRenderer, Attack a) {
    Vector2 origin = a.getAttackOrigin();
    Vector2 lastPos = origin;
    Vector2 currPos;
    float warningTime = a.getTotalTime();
    float alpha = MathUtils.map(0f, a.params.warningDrawTime, 0f, 1f, warningTime);
    float cuspThreshold = a.params.radius * .5f;

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(1f, 0f, 0f, alpha);

    for (int i = 1; i < Constants.NUM_ROSE_CURVE_WARNING_SAMPLE_POINTS; i++) {
      currPos =
          BulletPatterns.getRoseCurveBulletPos(
              origin,
              a.params.numPetals,
              Constants.NUM_ROSE_CURVE_WARNING_SAMPLE_POINTS,
              a.params.radius,
              i);

      float distPoints = currPos.dst(lastPos);

      // skip segment if either endpoint is near the origin (petal cusp)
      if (distPoints < cuspThreshold) {
        shapeRenderer.rectLine(lastPos, currPos, Constants.WARNING_LINE_WIDTH);
      }

      lastPos = currPos;
    }
    shapeRenderer.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  private static void drawHealFlash(SpriteBatch batch, Viewport viewport) {
    float alpha =
        MathUtils.map(0f, Constants.HEAL_FLASH_DURATION, 0f, 1f, DrawFlags.healFlashTimer);
    batch.setColor(1f, 1f, 1f, alpha);
    batch.draw(GameAssets.healFlash, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
    batch.setColor(1f, 1f, 1f, 1f);
  }

  /**
   * draws lines that show the trajectories of the bullets that are about to spawn from the
   * resulting arc
   *
   * @param shapeRenderer
   * @param a
   */
  private static void drawArcWarning(ShapeRenderer shapeRenderer, Attack a) {
    float arcWarnLineLen = 50f;
    float dtheta = a.params.arcWidth / (a.params.numBullets - 1);
    float warningTime = a.getTotalTime();
    Vector2 farawayPoint = new Vector2();
    float alpha = MathUtils.map(0f, a.params.warningDrawTime, 0f, 1f, warningTime);

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(1f, 0f, 0f, alpha);
    for (int i = 0; i < a.params.numBullets; i++) {
      float theta = a.attackStartAngle + dtheta * i;
      farawayPoint.set(1f, 1f);
      farawayPoint.setLength(arcWarnLineLen);
      farawayPoint.setAngleDeg(theta);
      farawayPoint.add(a.getAttackOrigin());
      shapeRenderer.rectLine(a.getAttackOrigin(), farawayPoint, Constants.WARNING_LINE_WIDTH);
    }
    shapeRenderer.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  private static void drawWallWarning(ShapeRenderer shapeRenderer, Attack a) {
    float warnLineLen = 200f;
    Vector2 origin = a.getAttackOrigin();
    Vector2 target = a.getAttackTarget();
    Vector2 farawayPoint = target.cpy().sub(origin).nor().scl(warnLineLen);
    Vector2 pos = new Vector2();
    float alpha = MathUtils.map(0f, a.params.warningDrawTime, 0f, 1f, a.getTotalTime());

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(1f, 0f, 0f, alpha);
    for (int i = 0; i < a.params.numBullets; i++) {
      pos.set(
          BulletPatterns.getWallBulletPosAt(
              origin, target, a.params.numBullets, i, a.params.length));
      shapeRenderer.rectLine(pos, farawayPoint.cpy().add(pos), Constants.WARNING_LINE_WIDTH);
    }
    shapeRenderer.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  private static void drawSpiralWarning(ShapeRenderer shapeRenderer, Attack a) {
    Vector2 origin = a.getAttackOrigin();
    float alpha = MathUtils.map(0f, a.params.warningDrawTime, 0f, 1f, a.getTotalTime());

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(1f, 0f, 0f, alpha);
    shapeRenderer.circle(origin.x, origin.y, a.params.radius);
    shapeRenderer.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  public static void drawSingleWarning(ShapeRenderer shapeRenderer, Attack a) {
    float lineLen = 50f;
    Vector2 origin = a.getAttackOrigin();
    Vector2 dir = a.params.aType == AttackType.AIMED_SINGLE
        ? a.getAttackTarget().cpy().sub(origin).nor()
        : a.params.getVel().nor();
    Vector2 farawayPoint = origin.cpy().add(dir.setLength(lineLen));
    float alpha = MathUtils.map(0f, a.params.warningDrawTime, 0f, 1f, a.getTotalTime());
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(1f, 0f, 0f, alpha);
    shapeRenderer.rectLine(origin, farawayPoint, Constants.WARNING_LINE_WIDTH);
    shapeRenderer.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  public static void drawWarnings(ShapeRenderer shapeRenderer) {
    // draw attack warnings
    for (Attack a : Attacker.attackPool) {
      if (!a.isActive() || !a.canDrawWarning()) {
        continue;
      }
      switch (a.params.aType) {
        case ROSE:
          drawRoseCurveWarning(shapeRenderer, a);
          break;
        case ARC:
          drawArcWarning(shapeRenderer, a);
          break;
        case SPIRAL:
          drawSpiralWarning(shapeRenderer, a);
          break;
        case WALL:
          drawWallWarning(shapeRenderer, a);
          break;
        case AIMED_SINGLE:
        case SINGLE:
          drawSingleWarning(shapeRenderer, a);
          break;
        default:
          break;
      }
    }
  }

  /**
   * reads DrawFlags and draws additional ui elemenets based on its current state.
   *
   * @param dt
   */
  private static void drawFlagged(SpriteBatch batch, Viewport viewport) {
    if (DrawFlags.showHitFlash) {
      DrawUtil.drawHitFlash(batch, viewport);
    }
    if (DrawFlags.showHealFlash) {
      DrawUtil.drawHealFlash(batch, viewport);
    }
  }
}
