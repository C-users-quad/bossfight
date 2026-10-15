package io.github.game.bullets;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BulletPatterns {
  /**
   * Spawns bullets in a polar flower formation.
   *
   * @param origin center of the flower
   * @param numPetals number of petals
   * @param numBullets number of bullets the flower is composed of
   * @param radius radius of the flower
   */
  public static void spawnRoseCurveAt(
      Vector2 origin, int numPetals, int numBullets, float radius, BulletParams params) {
    for (int i = 0; i < numBullets; i++) {
      spawnRoseCurveBulletAt(origin, numPetals, numBullets, radius, i, params);
    }
  }

  public static void spawnRoseCurveBulletAt(
      Vector2 origin,
      int numPetals,
      int numBullets,
      float radius,
      int currBullet,
      BulletParams params) {
    float theta = 360f / numBullets * currBullet;
    Vector2 pos = getRoseCurveBulletPos(origin, numPetals, numBullets, radius, currBullet);
    Vector2 vel =
        new Vector2(MathUtils.cosDeg(theta) * params.speed, MathUtils.sinDeg(theta) * params.speed);
    Bullets.create(pos, vel, params);
  }

  /**
   * gets the position of the corresponding bullet in the rose curve.
   *
   * @param origin
   * @param numPetals
   * @param numBullets
   * @param radius
   * @param currBullet
   * @return
   */
  public static Vector2 getRoseCurveBulletPos(
      Vector2 origin, int numPetals, int numBullets, float radius, int currBullet) {
    float theta = 360f / numBullets * currBullet;
    float r = radius * MathUtils.cosDeg(numPetals * theta);
    Vector2 pos =
        new Vector2(origin.x + r * MathUtils.cosDeg(theta), origin.y + r * MathUtils.sinDeg(theta));
    return pos;
  }

  /**
   * returns a random position near pos that is at least as far as innerRadius away from pos and at
   * most as far as outerRadius from pos.
   *
   * @param pos center of the field of possible positions
   * @param minDeviation minimum distance from pos
   * @param maxDeviation maximum distance from pos
   * @return
   */
  public static Vector2 getRandomPosNear(Vector2 pos, float minDeviation, float maxDeviation) {
    float angle = MathUtils.random(0f, 360f);
    float dist = (float)Math.sqrt(MathUtils.random(minDeviation * minDeviation, maxDeviation * maxDeviation));
    return new Vector2(
        pos.x + dist * MathUtils.cosDeg(angle),
        pos.y + dist * MathUtils.sinDeg(angle));
  }

  /**
   * spawns an arc of bullets at a position specified by origin
   *
   * @param origin where the arc is spawned
   * @param startAngle the first angle
   * @param endAngle the second angle
   * @param numBullets the number of bullets in the arc
   * @param type the bullet type
   */
  public static void spawnArcAt(
      Vector2 origin, float startAngle, float endAngle, int numBullets, BulletParams params) {
    for (int i = 0; i < numBullets; i++) {
      spawnArcBulletAt(origin, startAngle, endAngle, numBullets, i, params);
    }
  }

  /**
   * spawns an individual bullet in an arc.
   *
   * @param origin
   * @param startAngle
   * @param endAngle
   * @param numBullets
   * @param currBullet
   * @param type
   */
  public static void spawnArcBulletAt(
      Vector2 origin,
      float startAngle,
      float endAngle,
      int numBullets,
      int currBullet,
      BulletParams params) {
    float step = (endAngle - startAngle) / (numBullets - 1);
    float angle = startAngle + step * currBullet;
    float vx = MathUtils.cosDeg(angle) * params.speed;
    float vy = MathUtils.sinDeg(angle) * params.speed;
    Vector2 vel = new Vector2(vx, vy);
    Bullets.create(origin, vel, params);
  }

  /**
   * spawns a spiral (r = theta) centered at origin with the given radius and total angle
   *
   * @param origin
   * @param totalAngle
   * @param radius
   * @param numBullets
   * @param currBullet
   * @param type
   * @param accelerator
   */
  public static void spawnSpiralAt(
      Vector2 origin, float totalAngle, float radius, int numBullets, BulletParams params) {
    for (int i = 0; i < numBullets; i++) {
      spawnSpiralBulletAt(origin, totalAngle, radius, numBullets, i, params);
    }
  }

  public static void spawnSpiralBulletAt(
      Vector2 origin,
      float totalAngle,
      float radius,
      int numBullets,
      int currBullet,
      BulletParams params) {
    float dr = radius / numBullets;
    float dtheta = totalAngle / numBullets;
    float r = currBullet * dr;
    float theta = currBullet * dtheta;
    float cosTheta = MathUtils.cosDeg(theta);
    float sinTheta = MathUtils.sinDeg(theta);
    float x = r * cosTheta + origin.x;
    float y = r * sinTheta + origin.y;
    float vx = params.speed * cosTheta;
    float vy = params.speed * sinTheta;
    Vector2 pos = new Vector2(x, y);
    Vector2 vel = new Vector2(vx, vy).scl((r + radius * .5f) / radius);
    Bullets.create(pos, vel, params);
  }

  public static void spawnWallAt(
      Vector2 origin, Vector2 target, int numBullets, float length, BulletParams params) {
    for (int i = 0; i < numBullets; i++) {
      spawnWallBulletAt(origin, target, numBullets, i, length, params);
    }
  }

  public static Vector2 getWallBulletPosAt(
      Vector2 origin, Vector2 target, int numBullets, int currBullet, float length) {
    Vector2 toward = target.cpy().sub(origin).nor();
    Vector2 perp = new Vector2(-toward.y, toward.x).nor();
    float dl = length / (numBullets - 1);
    return perp.scl(dl * (currBullet - (numBullets - 1) / 2f)).add(origin);
  }

  public static void spawnWallBulletAt(
      Vector2 origin,
      Vector2 target,
      int numBullets,
      int currBullet,
      float length,
      BulletParams params) {
    Vector2 toward = target.cpy().sub(origin).nor();
    Vector2 perp = new Vector2(-toward.y, toward.x).nor();
    float dl = length / (numBullets - 1);
    Vector2 pos = perp.scl(dl * (currBullet - (numBullets - 1) / 2f)).add(origin);
    Vector2 vel = toward.scl(params.speed);
    Bullets.create(pos, vel, params);
  }

  public static void spawnRingBulletAt(
      Vector2 origin, int numBullets, int currBullet, float radius, BulletParams params) {
    float dtheta = 360f / numBullets;
    float theta = dtheta * currBullet;
    float x = radius * MathUtils.cosDeg(theta);
    float y = radius * MathUtils.sinDeg(theta);
    Vector2 pos = new Vector2(origin.x + x, origin.y + y);
    Vector2 vel = new Vector2(origin.x - pos.x, origin.y - pos.y).nor().scl(params.speed);
    Bullets.create(pos, vel, params);
  }

  public static void spawnRingAt(
      Vector2 origin, int numBullets, float radius, BulletParams params) {
    for (int i = 0; i < numBullets; i++) {
      spawnRingBulletAt(origin, numBullets, i, radius, params);
    }
  }
}
