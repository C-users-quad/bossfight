package io.github.game.attack;

import com.badlogic.gdx.math.Vector2;
import io.github.game.Positionable;
import io.github.game.bullets.Bullet;
import io.github.game.bullets.BulletParams;
import io.github.game.bullets.BulletPatterns;
import java.util.function.Supplier;

/**
 * fat "struct" that contains anything that an attack would need. in reality, most of these params
 * can just be left as defaults.
 *
 * <p>this isnt a public libaray, so i expect myself to be able to know what params to set to
 * defaults and what not to.
 *
 * <p>yep. thats right. no documentation that provides what params should be defaults for what
 * AttackType(s). just figure it out kid.
 */
public class AttackParams {
  // necessary for all attacks
  public AttackType aType;
  public BulletParams bParams;
  public SpawnStrategy spawnStrategy;
  public Supplier<Vector2> spawnAnchor;
  public int numBullets;
  public float minSpawnDeviation;
  public float maxSpawnDeviation;
  // necessary for some attacks
  public Positionable target;
  public Supplier<Vector2> dir;
  public float arcWidth;
  public float totalAngle;
  public float length;
  public int numPetals;
  public float radius;

  // optional
  /**
   * where do i begin?
   *
   * <p>for some attacks, there are child attacks that are spawned every timeBetweenChildAttacks
   * seconds that are positioned relative to the parent attack.
   *
   * <p>this provides a function that takes in a supplier of vector2's which are used to construct
   * the attackparams relative to the parent attack, and returns back a supplier of attackparams
   * that can be used to spawn multiple child attacks.
   */
  public ChildAttackFactoryBuilder childAttackFactoryBuilder;

  public float timeBetweenChildAttacks;
  public float timeBetweenBullets;
  public float warningTime;
  public float warningDrawTime;
  public float lifetime;

  /**
   * constructs with defaults. populate using setters. setters return this object back to enable
   * method chaining. cus we love method chaining. mm.
   */
  public AttackParams() {
    aType = null;
    bParams = null;
    dir = () -> Vector2.Zero;
    childAttackFactoryBuilder = null;
    numBullets = 0;
    numPetals = 0;
    timeBetweenBullets = 0f;
    warningTime = 0f;
    arcWidth = 0f;
    radius = 0f;
    totalAngle = 0f;
    length = 0f;
    target = null;
    minSpawnDeviation = 0f;
    maxSpawnDeviation = 0f;
    lifetime = 0f;
  }

  /**
   * its a. uh. (() -> Vector2) -> (() -> AttackParams).
   *
   * @param f
   * @return
   */
  public AttackParams setChildAttackFactoryBuilder(ChildAttackFactoryBuilder f) {
    childAttackFactoryBuilder = f;
    return this;
  }

  /**
   * direction vector used for single bullets.
   *
   * @param v
   * @return
   */
  public AttackParams setDir(Supplier<Vector2> d) {
    dir = d;
    return this;
  }

  /**
   * lifetime, the total time in seconds this attack is updated for.
   *
   * @param t
   * @return
   */
  public AttackParams setLifetime(float t) {
    lifetime = t;
    return this;
  }

  public AttackParams setTimeBetweenChildAttacks(float t) {
    timeBetweenChildAttacks = t;
    return this;
  }

  public AttackParams setTarget(Positionable p) {
    target = p;
    return this;
  }

  public AttackParams setLength(float l) {
    length = l;
    return this;
  }

  public AttackParams setTotalAngle(float a) {
    totalAngle = a;
    return this;
  }

  public AttackParams setAttackType(AttackType a) {
    aType = a;
    return this;
  }

  public AttackParams setBulletParams(BulletParams b) {
    bParams = b;
    return this;
  }

  public AttackParams setNumBullets(int n) {
    numBullets = n;
    return this;
  }

  public AttackParams setNumPetals(int n) {
    numPetals = n;
    return this;
  }

  public AttackParams setTimeBetweenBullets(float t) {
    timeBetweenBullets = t;
    return this;
  }

  public AttackParams setWarningDrawTime(float t) {
    warningDrawTime = t;
    return this;
  }

  public AttackParams setWarningTime(float t) {
    warningTime = t;
    return this;
  }

  public AttackParams setArcWidth(float a) {
    arcWidth = a;
    return this;
  }

  public AttackParams setRadius(float r) {
    radius = r;
    return this;
  }

  /** sets the parameters used for determining the origin of the attack */
  public AttackParams setSpawnAnchor(
      Supplier<Vector2> a, SpawnStrategy s, float minDeviation, float maxDeviation) {
    spawnAnchor = a;
    spawnStrategy = s;
    minSpawnDeviation = minDeviation;
    maxSpawnDeviation = maxDeviation;
    return this;
  }

  /** gets the origin of the attack */
  public Vector2 getOrigin() {
    Vector2 anchor = spawnAnchor.get().cpy();
    switch (spawnStrategy) {
      case EXACT:
        return anchor;
      case RANDOM:
        return BulletPatterns.getRandomPosNear(anchor, minSpawnDeviation, maxSpawnDeviation);
      default:
        return anchor;
    }
  }

  /**
   * total duration of attack sequence - after warning time
   *
   * @return
   */
  public float getTotalAttackDuration() {
    return numBullets * timeBetweenBullets;
  }

  /**
   * returns how long the attack update loop should
   *
   * @return
   */
  public float getEffectiveDuration() {
    return lifetime > 0f ? lifetime : getTotalAttackDuration();
  }

  /**
   * total duration including warning time
   *
   * @return
   */
  public float getTotalDuration() {
    return warningTime + numBullets * timeBetweenBullets;
  }

  /**
   * gets the current position of the target
   *
   * @return
   */
  public Vector2 getTargetPos() {
    return target.getPos().cpy();
  }

  /**
   * provides a factory for child attack params.
   *
   * @param anchorBullet optional anchor.
   * @param attackOrigin required anchor.
   * @return
   */
  public Supplier<AttackParams[]> getChildAttackFactory(Bullet anchorBullet, Vector2 attackOrigin) {
    if (childAttackFactoryBuilder == null) return null;

    Supplier<Vector2> anchor;
    switch (spawnStrategy) {
      case EMANATING:
        anchor =
            anchorBullet != null ? () -> anchorBullet.getPos().cpy() : () -> attackOrigin.cpy();
        break;
      default:
        Vector2 snap = anchorBullet != null ? anchorBullet.getPos().cpy() : attackOrigin.cpy();
        anchor = () -> snap;
        break;
    }

    return childAttackFactoryBuilder.bindAnchor(anchor);
  }

  public Vector2 getVel() {
    return dir.get().scl(bParams.speed);
  }
}
