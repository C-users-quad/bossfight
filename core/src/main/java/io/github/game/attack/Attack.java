package io.github.game.attack;

import com.badlogic.gdx.math.Vector2;
import io.github.game.Player;
import io.github.game.bullets.Bullet;
import io.github.game.bullets.BulletPatterns;
import io.github.game.bullets.Bullets;
import java.util.function.Supplier;

/** the dude. */
public class Attack {
  public AttackParams params;
  private boolean active;
  private boolean justStarted;
  private int bulletCounter;
  private float firingTime;
  private float warningTime;
  private float totalTime;
  private float intervalTime;
  // used for child attacks
  private Bullet anchorBullet;
  private Supplier<AttackParams[]> resolvedChildFactory;
  private float childIntervalTime;
  // used for staggered attacks
  private Vector2 attackTarget;
  private Vector2 attackOrigin;
  public float attackStartAngle;
  private float attackEndAngle;

  /** new the dude. */
  public Attack() {
    attackOrigin = new Vector2();
    attackTarget = new Vector2();
    deactivate();
  }

  /** kill the dude. */
  public void deactivate() {
    params = null;
    active = false;
    bulletCounter = 0;
    firingTime = 0f;
    warningTime = 0f;
    attackStartAngle = 0f;
    attackEndAngle = 0f;
    totalTime = 0f;
    intervalTime = 0f;
    childIntervalTime = 0f;
    anchorBullet = null;
    resolvedChildFactory = null;
    attackOrigin.setZero();
    attackTarget.setZero();
  }

  /** revive the dude. */
  public void activate(AttackParams p) {
    params = p;
    active = true;
  }

  /** check on the dude. */
  public boolean isActive() {
    return active;
  }

  /** check on the dudes origins. */
  public Vector2 getAttackOrigin() {
    return attackOrigin;
  }

  /** check on the dudes goals. */
  public Vector2 getAttackTarget() {
    return attackTarget;
  }

  /** how old the dude is. */
  public float getTotalTime() {
    return totalTime;
  }

  /** check how photogenic the dude is. */
  public boolean canDrawWarning() {
    return totalTime < params.warningDrawTime;
  }

  /** do the dude. but not like that. */
  public void update(Player player, float dt) {
    if (params.warningTime == 0 && totalTime == 0) justStarted = true;
    if (totalTime == 0f) computeParams(player);

    totalTime += dt;
    if (warningTime < params.warningTime) {
      warningTime += dt;
      if (warningTime > params.warningTime) justStarted = true;
      return;
    }

    if (firingTime > params.getEffectiveDuration()) {
      if (totalTime < params.warningDrawTime) return;
      deactivate();
      return;
    }
    firingTime += dt;

    boolean staggered = params.timeBetweenBullets > 0f;
    if (!staggered && justStarted) {
      spawnInstant();
      justStarted = false;
    } else if (intervalTime >= params.timeBetweenBullets || justStarted) {
      intervalTime -= params.timeBetweenBullets;
      onInterval();
      justStarted = false;
    }
    intervalTime += dt;

    updateChildAttack(dt);
  }

  /** birth. */
  private void updateChildAttack(float dt) {
    if (resolvedChildFactory == null) {
      return;
    }

    if (anchorBullet != null && !anchorBullet.isActive()) return;
    boolean justStarted = childIntervalTime == 0f;
    childIntervalTime += dt;
    if (childIntervalTime >= params.timeBetweenChildAttacks || justStarted) {
      if (!justStarted) childIntervalTime -= params.timeBetweenChildAttacks;
      for (AttackParams a : resolvedChildFactory.get()) Attacker.doAttack(a);
    }
  }

  /** mini birth. */
  private void onInterval() {
    if (bulletCounter > params.numBullets) return;
    spawnStaggered();
    bulletCounter++;
  }

  /** planned parenthood */
  private void computeParams(Player player) {
    switch (params.aType) {
      case ARC:
        attackOrigin = params.getOrigin();
        float centerAngle = player.getPos().cpy().sub(attackOrigin).angleDeg();
        attackStartAngle = centerAngle - params.arcWidth * .5f;
        attackEndAngle = centerAngle + params.arcWidth * .5f;
        break;
      case RING:
      case ROSE:
      case SINGLE:
      case SPIRAL:
        attackOrigin = params.getOrigin();
        break;
      case AIMED_SINGLE:
      case WALL:
        attackOrigin = params.getOrigin();
        attackTarget = params.getTargetPos();
        break;
      default:
        break;
    }
  }

  /** litter */
  private void spawnInstant() {
    Bullet spawned = null;
    switch (params.aType) {
      case ARC:
        BulletPatterns.spawnArcAt(
            attackOrigin, attackStartAngle, attackEndAngle, params.numBullets, params.bParams);
        break;
      case ROSE:
        BulletPatterns.spawnRoseCurveAt(
            attackOrigin, params.numPetals, params.numBullets, params.radius, params.bParams);
        break;
      case SINGLE:
        spawned = Bullets.create(attackOrigin, params.getVel(), params.bParams);
        break;
      case SPIRAL:
        BulletPatterns.spawnSpiralAt(
            attackOrigin, params.totalAngle, params.radius, params.numBullets, params.bParams);
        break;
      case WALL:
        BulletPatterns.spawnWallAt(
            attackOrigin, attackTarget, params.numBullets, params.length, params.bParams);
        break;
      case RING:
        BulletPatterns.spawnRingAt(attackOrigin, params.numBullets, params.radius, params.bParams);
        break;
      case AIMED_SINGLE:
        Vector2 aimDir = attackTarget.cpy().sub(attackOrigin).nor();
        spawned = Bullets.create(attackOrigin, aimDir.scl(params.bParams.speed), params.bParams);
        break;
      default:
        break;
    }
    if (spawned != null) anchorBullet = spawned;
    resolvedChildFactory = params.getChildAttackFactory(anchorBullet, attackOrigin);
  }

  /** kitty */
  private void spawnStaggered() {
    Vector2 origin;
    switch (params.spawnStrategy) {
      case EMANATING:
        origin = params.getOrigin();
        break;
      default:
        origin = attackOrigin;
        break;
    }
    switch (params.aType) {
      case ARC:
        BulletPatterns.spawnArcBulletAt(
            origin,
            attackStartAngle,
            attackEndAngle,
            params.numBullets,
            bulletCounter,
            params.bParams);
        break;
      case ROSE:
        BulletPatterns.spawnRoseCurveBulletAt(
            origin,
            params.numPetals,
            params.numBullets,
            params.radius,
            bulletCounter,
            params.bParams);
        break;
      case SPIRAL:
        BulletPatterns.spawnSpiralBulletAt(
            origin,
            params.totalAngle,
            params.radius,
            params.numBullets,
            bulletCounter,
            params.bParams);
        break;
      case WALL:
        BulletPatterns.spawnWallBulletAt(
            origin, attackTarget, params.numBullets, bulletCounter, params.length, params.bParams);
        break;
      case RING:
        BulletPatterns.spawnRingBulletAt(
            origin, params.numBullets, bulletCounter, params.radius, params.bParams);
        break;
      default:
        break;
    }
  }
}
