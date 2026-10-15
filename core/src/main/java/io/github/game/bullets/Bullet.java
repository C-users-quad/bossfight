package io.github.game.bullets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.game.Player;
import io.github.game.Positionable;
import io.github.game.boss.Boss;
import io.github.game.drawing.AnchorPoints;

/**
 * this is a class that can be used to represent any hostile hitbox that inflicts damage to a
 * target.
 *
 * <p>commonly, it is used for bullets, small projectiles that harm either the player or the boss.
 *
 * <p>but it can also be for super ultra death beam type things, sword slashes, and whatever else
 * really.
 */
public class Bullet implements Positionable {
  private Vector2 pos;
  private Vector2 vel;
  private Rectangle hitbox; // for player-bullet collisions.
  private Rectangle boundingBox; // for world-bullet collisions.
  private Accelerator accelerator;
  private Accelerator playerAccelerator;
  private BulletParams params;
  private boolean active;
  private float totalTime;

  // loads a default bullet.
  public Bullet() {
    pos = new Vector2();
    vel = new Vector2();
    hitbox = new Rectangle();
    boundingBox = new Rectangle();
    deactivate();
  }

  public Vector2 getPos() {
    return pos;
  }

  public BulletType getType() {
    return params.type;
  }

  private void integrate(float dt) {
    Vector2 acc = accelerator.getAcceleration(pos, dt);
    vel.add(acc.x * dt, acc.y * dt);
    if (vel.len() > 50f) vel.setLength(50f);
    pos.add(vel.x * dt, vel.y * dt);
  }

  private void updateBoundingBox() {
    boundingBox.setPosition(pos);
  }

  private void updateHitbox() {
    hitbox.setPosition(
        AnchorPoints.getAnchoredPoint(
            AnchorPoints.CENTER, pos.x, pos.y, params.type.hitboxSize.x, params.type.hitboxSize.y));
  }

  private void worldCollision(Rectangle screenRect) {
    if (!screenRect.overlaps(boundingBox) && params.despawnOutOfBounds) {
      deactivate();
    }
  }

  private void collisions(Player player, Boss boss) {
    boolean hit = false;
    switch (params.type.target) {
      case PLAYER:
        if (player.getHitbox().overlaps(hitbox)) {
          if (!player.isInvulnerable()) {
            player.takeDamage(params.damage, params.immuneTime);
            hit = true;
          }
        }
        break;
      case BOSS:
        if (boss.getHitbox().overlaps(hitbox)) {
          if (!boss.isInvulnerable()) {
            boss.takeDamage(params.damage, params.immuneTime);
            hit = true;
          }
        }
        break;
      default:
        break;
    }
    if (params.despawnOnHit && hit) deactivate();
  }

  private void updateTotalTime(float dt) {
    totalTime += dt;
  }

  private void influencePlayer(float dt, Player player) {
    player.applyAcc(playerAccelerator.getAcceleration(player.getPos(), dt));
  }

  private void detectTimedDespawn() {
    if (params.despawnTimer != 0 && totalTime >= params.despawnTimer) deactivate();
  }

  public void update(float dt, Rectangle screenRect, Player player, Boss boss) {
    updateTotalTime(dt);
    integrate(dt);
    updateHitbox();
    updateBoundingBox();
    influencePlayer(dt, player);
    worldCollision(screenRect);
    if (!active) return;
    collisions(player, boss);
    if (!active) return;
    detectTimedDespawn();
    if (!active) return;
  }

  public void draw(SpriteBatch batch) {
    if (params.type.animated) {
      Vector2 drawPos =
          AnchorPoints.getAnchoredPoint(
              AnchorPoints.CENTER,
              pos.x,
              pos.y,
              params.type.drawingSize.x,
              params.type.drawingSize.y);
      batch.draw(
          params.type.getAnimation().getKeyFrame(totalTime),
          drawPos.x,
          drawPos.y,
          params.type.drawingSize.x,
          params.type.drawingSize.y);
      return;
    }
    batch.draw(
        params.type.getTexture(),
        pos.x,
        pos.y,
        params.type.drawingSize.x * .5f,
        params.type.drawingSize.y * .5f,
        params.type.drawingSize.x,
        params.type.drawingSize.y,
        1f,
        1f,
        vel.angleDeg(),
        0,
        0,
        params.type.getTexture().getWidth(),
        params.type.getTexture().getHeight(),
        false,
        false);
  }

  // deactivates the bullet.
  public void deactivate() {
    active = false;
    totalTime = 0f;
    accelerator = null;
    playerAccelerator = null;
    params = null;
    boundingBox.set(0f, 0f, 0f, 0f);
    hitbox.set(0f, 0f, 0f, 0f);
    pos.setZero();
    vel.setZero();
  }

  public boolean isActive() {
    return active;
  }

  /**
   * re-initializes the bullet
   *
   * @param pos spawn position of bullet
   * @param vel initial velocity of bullet
   * @param acc acceleration of bullet
   * @param type determines some bullet-type specific params
   */
  public void activate(Vector2 pos, Vector2 vel, BulletParams params) {
    this.pos.set(pos);
    this.vel.set(vel);
    this.accelerator = params.getAccelerator();
    this.playerAccelerator = params.getPlayerAccelerator(this);
    this.params = params;

    Vector2 center =
        AnchorPoints.getAnchoredPoint(
            AnchorPoints.CENTER,
            pos.x,
            pos.y,
            params.type.drawingSize.x,
            params.type.drawingSize.y);
    hitbox.set(center.x, center.y, params.type.hitboxSize.x, params.type.hitboxSize.y);
    boundingBox.set(pos.x, pos.y, params.type.drawingSize.x, params.type.drawingSize.y);

    active = true;
  }
}
