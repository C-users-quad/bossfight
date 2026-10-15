package io.github.game.bullets;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Used to define a bullet with some behavior and some sprite defined by BulletType.
 *
 * <p>can be used to compose a BulletLibrary of configured instances of BulletParams.
 *
 * <p>the end goal is to be able to just do Bullets.create(pos, vel, BulletLibrary.X);
 */
public class BulletParams {
  /** determines the bullets image, image size, and hitbox size */
  public BulletType type;

  /** determines who the bullet can damage */
  public BulletTarget target;

  /** provides accelerators that can determine what a bullet homes toward */
  private Supplier<Accelerator> acceleratorFactory;

  /** provides accelerators that can determine how a bullet accelerates the player */
  private Supplier<Function<Bullet, Accelerator>> playerAcceleratorFactory;

  /** determines how much immune time is granted to the bullets target when hit */
  public float immuneTime;

  /** determines how much health is removed from the bullet target on hit */
  public float damage;

  /** the bullets inital speed */
  public float speed;

  /** how long after creation time until the bullet automatically despawns */
  public float despawnTimer;

  /** determines whether the bullet despawns when it hits its target */
  public boolean despawnOnHit;

  /** determines whether the bullet despawns if it is not visible on-screen */
  public boolean despawnOutOfBounds;

  public BulletParams() {
    type = null;
    target = null;
    acceleratorFactory = () -> Accelerator.none();
    playerAcceleratorFactory = () -> b -> Accelerator.none();
    immuneTime = 0f;
    damage = 0f;
    speed = 0f;
    despawnTimer = 0f;
    despawnOnHit = false;
    despawnOutOfBounds = false;
  }

  public BulletParams setType(BulletType t) {
    type = t;
    return this;
  }

  public BulletParams setAcceleratorFactory(Supplier<Accelerator> aF) {
    acceleratorFactory = aF;
    return this;
  }

  public BulletParams setTarget(BulletTarget t) {
    target = t;
    return this;
  }

  public BulletParams setImmuneTime(float t) {
    immuneTime = t;
    return this;
  }

  public BulletParams setDamage(float d) {
    damage = d;
    return this;
  }

  public BulletParams setSpeed(float s) {
    speed = s;
    return this;
  }

  public BulletParams setDespawnTimer(float t) {
    despawnTimer = t;
    return this;
  }

  public BulletParams setDespawnOnHit(boolean d) {
    despawnOnHit = d;
    return this;
  }

  public BulletParams setDespawnOutOfBounds(boolean d) {
    despawnOutOfBounds = d;
    return this;
  }

  public BulletParams setPlayerAcceleratorFactory(Supplier<Function<Bullet, Accelerator>> a) {
    playerAcceleratorFactory = a;
    return this;
  }

  public Accelerator getAccelerator() {
    return acceleratorFactory.get();
  }

  /**
   * gets the player accelerator.
   *
   * <p>bullets that accelerate the player generally require an accelerator that knows of the
   * bullets state, so thats why i am having the factory supply a function that accepts bullet.
   *
   * @param b
   * @return
   */
  public Accelerator getPlayerAccelerator(Bullet b) {
    return playerAcceleratorFactory.get().apply(b);
  }
}
