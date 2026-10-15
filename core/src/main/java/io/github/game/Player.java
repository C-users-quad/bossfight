package io.github.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.game.asset_loaders.GameAssets;
import io.github.game.attack.AttackLibrary;
import io.github.game.attack.Attacker;
import io.github.game.drawing.AnchorPoints;
import io.github.game.drawing.DrawFlags;

public class Player implements Positionable {
  private Vector2 pos; // bottom-left
  private Vector2 vel;
  private Vector2 dir; // direction of the acceleration vector
  private Vector2 externalAcc; // modified by anything that externally accelerates the player.
  private Vector2 size;
  private Rectangle hitbox;
  private Rectangle boundingBox;
  private boolean grounded;
  private float attackCooldownTime;
  private float dashCooldownTime;
  private float flightTime;
  private float health;
  private float immuneTime; // time remaining immune in seconds
  private float dashSpeedBoost;
  private float healCooldownTime;
  private int hitCount;

  // default constructor
  public Player() {
    pos = new Vector2(0f, 0f);
    dir = new Vector2(0f, 0f);
    vel = new Vector2(0f, 0f);
    size = new Vector2(1f, 2f);
    health = Constants.PLAYER_HEALTH;
    grounded = true;
    immuneTime = 0f;
    attackCooldownTime = 0f;
    dashCooldownTime = 0f;
    dashSpeedBoost = 0f;
    externalAcc = new Vector2();
    Vector2 center =
        AnchorPoints.getAnchoredPoint(AnchorPoints.CENTER, pos.x, pos.y, size.x, size.y);
    boundingBox = new Rectangle(pos.x, pos.y, size.x, size.y);
    healCooldownTime = 0f;
    hitCount = 0;
    hitbox =
        new Rectangle(
            center.x,
            center.y,
            size.x * Constants.HITBOX_SCALE_W,
            size.y * Constants.HITBOX_SCALE_H);
  }

  public float getFlightTime() {
    return flightTime;
  }

  /** gets the rectangle used for rigid body collisions */
  public Rectangle getBoundingBox() {
    return boundingBox;
  }

  public float getHealCooldownTime() {
    return healCooldownTime;
  }

  public Vector2 getVel() {
    return vel;
  }

  public Vector2 getPos() {
    return pos;
  }

  public void applyAcc(Vector2 acc) {
    externalAcc.add(acc);
  }

  public void setPos(float x, float y) {
    pos.set(x, y);
  }

  /** gets the rectangle used for damage collisions */
  public Rectangle getHitbox() {
    return hitbox;
  }

  // makes player invulnerable for some number of seconds
  public void makeInvulnerable(float immuneTime) {
    this.immuneTime = immuneTime;
  }

  public boolean isInvulnerable() {
    return immuneTime > 0;
  }

  public boolean canDash() {
    return dashCooldownTime <= 0f;
  }

  public void takeDamage(float damage, float immuneTime) {
    health -= damage;
    health = Math.max(health, 0f);
    makeInvulnerable(immuneTime);
    GameAssets.hitSound.play(Constants.HIT_SOUND_VOLUME);
    DrawFlags.doHitFlash();
    hitCount++;
  }

  public int getHitCount() {
    return hitCount;
  }

  public float getHealth() {
    return health;
  }

  public boolean isDead() {
    return health == 0f;
  }

  private void heal() {
    health += Constants.PLAYER_HEAL_AMOUNT;
    health = Math.min(health, Constants.PLAYER_HEALTH);
    healCooldownTime += Constants.PLAYER_HEAL_COOLDOWN_TIME;
    DrawFlags.doHealFlash();
    GameAssets.healSound.play(Constants.HEAL_SOUND_VOLUME);
  }

  private void dash() {
    makeInvulnerable(Constants.PLAYER_DASH_IMMUNE_TIME);
    dashCooldownTime += Constants.PLAYER_DASH_COOLDOWN_TIME;
    Vector2 dashDir = new Vector2(dir);
    if (dashDir.isZero()) {
      dashDir.set(1f, 0f);
    }
    dashDir.nor();
    dashSpeedBoost = Constants.PLAYER_DASH_SPEED * dashDir.x;
  }

  private void updateDir() {
    dir.setZero();
    if (Gdx.input.isKeyPressed(Keys.A)) dir.x -= 1.0f;
    if (Gdx.input.isKeyPressed(Keys.D)) dir.x += 1.0f;

    // Only allow upward thrust if flight time hasn't been exhausted
    boolean spaceHeld = Gdx.input.isKeyPressed(Keys.SPACE);
    boolean canFly = spaceHeld && flightTime < Constants.PLAYER_FLIGHT_TIME;
    if (canFly) {
      dir.y += 1.0f;
      grounded = false;
    }

    dir.nor();
  }

  // prevents player from leaving the confines of this world
  public void worldCollisions() {
    if (pos.x < -Constants.WORLD_BORDER) {
      vel.x = 0;
      pos.x = -Constants.WORLD_BORDER;
    } else if (pos.x > Constants.WORLD_BORDER) {
      vel.x = 0;
      pos.x = Constants.WORLD_BORDER;
    }
  }

  public void updateHitbox() {
    hitbox.setPosition(
        pos.x + (size.x - hitbox.width) * .5f, pos.y + (size.y - hitbox.height) * .5f);
  }

  private boolean canHeal() {
    return healCooldownTime <= 0f;
  }

  private void handleHealInput() {
    if (canHeal() && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
      heal();
    }
  }

  private void handleAttackInput() {
    if (canAttack() && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      attack();
    }
  }

  private void handleDashInput() {
    if (canDash() && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      dash();
    }
  }

  private void attack() {
    Attacker.doAttack(AttackLibrary.PLAYER_ATTACK);
    attackCooldownTime += Constants.PLAYER_ATTACK_COOLDOWN_TIME;
  }

  private boolean canAttack() {
    return attackCooldownTime <= 0f;
  }

  // detects input and handles accordingly.
  public void handleInput() {
    updateDir();
    handleHealInput();
    handleAttackInput();
    handleDashInput();
  }

  public void draw(SpriteBatch batch) {
    batch.draw(GameAssets.player, pos.x, pos.y, size.x, size.y);
  }

  private void integrateVelocity(float dt) {
    // Vertical acceleration only while flight time remains and space drives it
    vel.add(dir.x * Constants.PLAYER_HORIZ_ACC * dt, dir.y * Constants.PLAYER_VERT_ACC * dt);

    // Horizontal clamp
    vel.x = MathUtils.clamp(vel.x, -Constants.PLAYER_HORIZ_SPEED, Constants.PLAYER_HORIZ_SPEED);

    // Vertical clamp: gliding (space held, flight exhausted) slows the fall
    boolean exhaustedAndGliding =
        Gdx.input.isKeyPressed(Keys.SPACE) && flightTime >= Constants.PLAYER_FLIGHT_TIME;
    float maxFallSpeed =
        exhaustedAndGliding
            ? Constants.PLAYER_GLIDE_SPEED // slow fall
            : Constants.PLAYER_VERT_SPEED; // normal fall
    vel.y = MathUtils.clamp(vel.y, -maxFallSpeed, Constants.PLAYER_VERT_SPEED);

    // Friction when not moving horizontally
    if (dir.x == 0f) {
      vel.x *= (float) Math.pow(Constants.FRICTION, -dt);
    }

    // apply external forces
    applyExternalAcc(dt);

    // limit velocity
    if (vel.len() > Constants.PLAYER_VERT_SPEED) vel.setLength(Constants.PLAYER_VERT_SPEED);
    if (vel.len() < .1f) vel.setLength(0f);

    // dash (after velocity limit cus dash overrides this)
    if (Constants.PLAYER_DASH_COOLDOWN_TIME - dashCooldownTime
        <= Constants.PLAYER_DASH_BOOST_TIME) {
      vel.add(dashSpeedBoost, 0f);
    }
  }

  private void applyExternalAcc(float dt) {
    vel.add(externalAcc.scl(dt));
    externalAcc.setZero();
  }

  private void integratePosition(float dt) {
    pos.add(vel.x * dt, vel.y * dt);
  }

  public void makeGrounded(float yFloor) {
    pos.y = yFloor;
    vel.y = 0f;
    grounded = true;
  }

  public void makeUngrounded() {
    grounded = false;
  }

  private void applyGravity(float dt) {
    if (!grounded) vel.sub(0.0f, Constants.GRAVITY * dt);
    if (pos.y <= Constants.FLOOR_Y) {
      makeGrounded(Constants.FLOOR_Y);
    }
  }

  private void updateFlightTime(float dt) {
    if (grounded) {
      // Reset on landing
      flightTime = 0f;
    } else if (flightTime < Constants.PLAYER_FLIGHT_TIME && Gdx.input.isKeyPressed(Keys.SPACE)) {
      // Only consume flight time while actively thrusting
      flightTime += dt;
    }
  }

  private void updateImmuneTime(float dt) {
    immuneTime -= dt;
    immuneTime = Math.max(immuneTime, 0);
  }

  private void integrate(float dt) {
    integrateVelocity(dt);
    integratePosition(dt);
    applyGravity(dt);
  }

  public void handleDeath() {
    health = Constants.PLAYER_HEALTH;
    hitCount = 0;
    pos.set(Constants.PLAYER_RESPAWN_POS);
  }

  private void updateBoundingBox() {
    boundingBox.setPosition(pos);
  }

  private void updateHealCooldownTime(float dt) {
    healCooldownTime -= dt;
    healCooldownTime = Math.max(healCooldownTime, 0f);
  }

  private void updateAttackCooldownTime(float dt) {
    attackCooldownTime -= dt;
    attackCooldownTime = Math.max(attackCooldownTime, 0f);
  }

  private void updateDashCooldownTime(float dt) {
    dashCooldownTime -= dt;
    dashCooldownTime = Math.max(dashCooldownTime, 0f);
  }

  public void update(float dt) {
    handleInput();

    integrate(dt);
    worldCollisions();
    updateHitbox();
    updateBoundingBox();

    updateFlightTime(dt);
    updateImmuneTime(dt);
    updateHealCooldownTime(dt);
    updateAttackCooldownTime(dt);
    updateDashCooldownTime(dt);
  }
}
