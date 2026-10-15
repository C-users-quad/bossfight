package io.github.game.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.game.Constants;
import io.github.game.Player;
import io.github.game.Positionable;
import io.github.game.asset_loaders.GameAssets;
import io.github.game.attack.phase.AttackEvent;
import io.github.game.attack.phase.Phase;
import io.github.game.attack.phase.PhaseRunner;

/** class that manages drawing and updating the actual boss character */
public class Boss implements Positionable {
  private Vector2 pos;
  private Vector2 vel;
  private Vector2 size;
  private Rectangle hitbox;
  private Rectangle boundingBox;
  private Vector2 wanderTarget;
  private PhaseRunner[] phases;
  private boolean chasing;
  private float wanderTimer;
  private int wanderQuadrant;
  private float health;
  private float restTime;
  private float immuneTime; // time remaining immune in seconds
  private int phaseCounter;

  public Boss() {
    setDefaults();
  }

  public void initPhases() {
    phases =
        new PhaseRunner[] {
          new PhaseRunner(
              new Phase(
                  new AttackEvent[][] {
                    BarrageLibrary.thorns(), BarrageLibrary.bombs(), BarrageLibrary.spirals()
                  }),
              30f,
              false,
              true),
          new PhaseRunner(
              new Phase(new AttackEvent[][] {BarrageLibrary.walls()}), 10f, false, true),
          new PhaseRunner(
              new Phase(new AttackEvent[][] {BarrageLibrary.blackHole()}), 30f, true, true),
          new PhaseRunner(
            new Phase(new AttackEvent[][] {BarrageLibrary.slashes()}), 15f, false, true),
          new PhaseRunner(
              new Phase(
                  new AttackEvent[][] {
                    BarrageLibrary.finalAttack(),
                  }),
              20f,
              false,
              true)
        };
    // shuffle phase 1
    phases[0].phase.shuffle(0, 1, 2);
  }

  // makes player invulnerable for some number of frames
  public void makeInvulnerable(float immuneTime) {
    this.immuneTime = immuneTime;
  }

  public void takeDamage(float damage, float immuneTime) {
    health -= damage;
    health = Math.max(health, 0f);
    makeInvulnerable(immuneTime);
  }

  public boolean isInvulnerable() {
    return immuneTime > 0;
  }

  private boolean isResting() {
    return restTime > 0f;
  }

  /** waits a little between phases */
  private void rest() {
    restTime = Constants.BOSS_INTER_PHASE_REST_TIME;
  }

  public boolean isDead() {
    return health == 0f;
  }

  /** re-initializes the boss with default state, state at phase zero. */
  public void setDefaults() {
    size = new Vector2(1f, 2f);
    hitbox = new Rectangle(0f, 0f, size.x, size.y);
    pos = new Vector2(0f, 5f);
    vel = new Vector2();
    wanderQuadrant = 0;
    wanderTarget = new Vector2();
    wanderTimer = 0f;
    boundingBox = new Rectangle(0f, 0f, size.x, size.y);
    chasing = false;
    phaseCounter = 0;
    health = Constants.BOSS_HEALTH;
    if (phases != null) for (PhaseRunner p : phases) p.reset();
  }

  public void draw(SpriteBatch batch) {
    batch.draw(GameAssets.boss, boundingBox.x, boundingBox.y, size.x, size.y);
  }

  public Rectangle getHitbox() {
    return hitbox;
  }

  private void updateHitbox() {
    hitbox.setPosition(
        pos.x + (size.x - hitbox.width) * 0.5f, pos.y + (size.y - hitbox.height) * 0.5f);
  }

  public Vector2 getPos() {
    return pos;
  }

  private void updateBoundingBox() {
    boundingBox.setPosition(pos.x, pos.y);
  }

  private void updateWanderTarget(Player player) {
    float startx = 0;
    float endx = 0;
    float starty = 0;
    float endy = 0;
    switch (wanderQuadrant) {
      case 3:
        startx = -Constants.BOSS_MAX_WANDER_RADIUS;
        endx = -Constants.BOSS_MIN_WANDER_RADIUS;
        starty = Constants.BOSS_MIN_WANDER_RADIUS;
        endy = Constants.BOSS_MAX_WANDER_RADIUS;
        break;
      case 2:
        startx = Constants.BOSS_MIN_WANDER_RADIUS;
        endx = Constants.BOSS_MAX_WANDER_RADIUS;
        starty = Constants.BOSS_MIN_WANDER_RADIUS;
        endy = Constants.BOSS_MAX_WANDER_RADIUS;
        break;
      case 0:
        startx = -Constants.BOSS_MAX_WANDER_RADIUS;
        endx = -Constants.BOSS_MIN_WANDER_RADIUS;
        starty = -Constants.BOSS_MAX_WANDER_RADIUS;
        endy = -Constants.BOSS_MIN_WANDER_RADIUS;
        break;
      case 1:
        startx = Constants.BOSS_MIN_WANDER_RADIUS;
        endx = Constants.BOSS_MAX_WANDER_RADIUS;
        starty = -Constants.BOSS_MAX_WANDER_RADIUS;
        endy = -Constants.BOSS_MIN_WANDER_RADIUS;
        break;
      default:
        break;
    }
    wanderQuadrant = (wanderQuadrant + 1) % 4;
    wanderTarget.set(
        player.getPos().x + MathUtils.random(startx, endx),
        player.getPos().y + MathUtils.random(starty, endy));
    wanderTimer =
        MathUtils.random(Constants.BOSS_MIN_WANDER_TIMER, Constants.BOSS_MAX_WANDER_TIMER);
  }

  public void integrateVelocity(Player player, float dt) {
    Vector2 toPlayer = player.getPos().cpy().sub(pos);
    float distToPlayer = toPlayer.len();

    // hysterics for transitions between chasing and wandering
    if (!chasing && distToPlayer > Constants.BOSS_CHASE_START_DIST) chasing = true;
    if (chasing && distToPlayer < Constants.BOSS_CHASE_END_DIST) {
      chasing = false;
      updateWanderTarget(player); // fresh target and fresh timer
    }

    if (chasing) {
      Vector2 desired = toPlayer.setLength(Constants.BOSS_CHASE_MAX_SPEED);
      Vector2 steering = desired.sub(vel); // steer = desired - current vel
      // clamp steering force magnitude
      if (steering.len() > Constants.BOSS_CHASE_MAX_STEER_FORCE) {
        steering.setLength(Constants.BOSS_CHASE_MAX_STEER_FORCE);
      }
      vel.add(steering.scl(dt));
      // clamp max speed
      if (vel.len() > Constants.BOSS_CHASE_MAX_SPEED) {
        vel.setLength(Constants.BOSS_CHASE_MAX_SPEED);
      }
    } else {
      // wandering case: decrement wander timer, update wandering target if necessary.
      wanderTimer -= dt;
      if (wanderTimer <= 0f || pos.dst(wanderTarget) < Constants.BOSS_WANDER_ARRIVE_DIST) {
        updateWanderTarget(player);
      }
      Vector2 acc = wanderTarget.cpy().sub(pos).setLength(Constants.BOSS_WANDER_ACCEL);
      vel.add(acc.x * dt, acc.y * dt);
    }
    // apply drag.
    float drag = chasing ? Constants.BOSS_CHASE_DRAG : Constants.BOSS_WANDER_DRAG;
    vel.scl(1f - drag * dt);
  }

  public void integratePosition(Player player, float dt) {
    pos.add(vel.x * dt, vel.y * dt);
  }

  private void updateImmuneTime(float dt) {
    immuneTime -= dt;
    immuneTime = Math.max(immuneTime, 0);
  }

  private void updatePhases(float dt) {
    if (phaseCounter == phases.length) {
      // end boss logic (not implemented)
      return;
    }
    if (isResting()) return;
    // update phase
    PhaseRunner currPhase = phases[phaseCounter];
    currPhase.update(dt);

    // determine if the boss goes to the next phase
    boolean canGotoNextPhase = false;
    switch (phaseCounter) {
      case 0:
        canGotoNextPhase = health <= Constants.BOSS_HEALTH * .75;
        break;
      case 1:
        canGotoNextPhase = health <= Constants.BOSS_HEALTH * .65;
        break;
      case 2:
        canGotoNextPhase = health <= Constants.BOSS_HEALTH * .40;
        break;
      case 3:
        canGotoNextPhase = health <= Constants.BOSS_HEALTH * .10f;
        break;
      case 4:
        canGotoNextPhase = health <= 0f;
        break;
    }
    if (canGotoNextPhase) {
      phaseCounter++;
      currPhase.reset();
      rest();
    } else if (currPhase.isFinishedExecuting() && currPhase.hasExceededDuration()) {
      currPhase.reset();
    }
    if (phaseCounter >= phases.length) {
      currPhase.reset();
    }
  }

  /** get the boss's current health */
  public float getHealth() {
    return health;
  }

  private void updateRestTime(float dt) {
    restTime -= dt;
    restTime = Math.max(0, restTime);
  }

  /**
   * does position and collision updates.
   *
   * @param player
   * @param dt
   */
  public void update(Player player, float dt) {
    integrateVelocity(player, dt);
    integratePosition(player, dt);
    updateBoundingBox();
    updateHitbox();
    updateImmuneTime(dt);
    updateRestTime(dt);
    updatePhases(dt);
  }
}
