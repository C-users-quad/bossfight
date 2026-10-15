package io.github.game.bullets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.game.Constants;
import io.github.game.Player;
import io.github.game.boss.Boss;

/** updates, draws, and stores every bullet spawned by bullet patterns or via methods here */
public class Bullets {
  // array that store all bullets
  private static Bullet[] bulletPool = new Bullet[Constants.BULLET_POOL_SIZE];
  // index suceeding the index of the last bullet activated.
  // used for searching for next available bullet during creation with better constants.
  private static int free = 0;

  // initialize bullet arrays with default, inactive bullets.
  static {
    for (int i = 0; i < Constants.BULLET_POOL_SIZE; i++) {
      bulletPool[i] = new Bullet();
    }
  }

  // locates the index of the next available bullet slot. returns -1 if pool overflows.
  private static int getBullet(Bullet[] arr) {
    for (int i = 0; i < Constants.BULLET_POOL_SIZE; i++) {
      int idx = (free + i) % Constants.BULLET_POOL_SIZE;
      if (!arr[idx].isActive()) return idx;
    }
    return -1;
  }

  public static Bullet create(Vector2 pos, Vector2 vel, BulletParams params) {
    int idx = getBullet(bulletPool);
    if (idx == -1) return null; // pool overflow, creation fails.
    bulletPool[idx].activate(pos, vel, params);
    free = (idx + 1) % Constants.BULLET_POOL_SIZE;
    return bulletPool[idx];
  }

  // gets a rectangle slightly bigger than the screen rect for bullet world collisions
  private static Rectangle getBuffedScreenRect(Rectangle screenRect) {
    Rectangle buffedRect = new Rectangle(screenRect);
    buffedRect.setSize(
        buffedRect.width * Constants.SCREEN_RECT_BUFF,
        buffedRect.height * Constants.SCREEN_RECT_BUFF);
    Vector2 center = new Vector2();
    screenRect.getCenter(center);
    buffedRect.setCenter(center);
    return buffedRect;
  }

  /**
   * determines whether there are any actively updating bullets
   * that arent the players
   * @return
   */
  public static boolean isActive() {
    for (Bullet b : bulletPool) {
      if (b.isActive() && b.getType() != BulletType.PLAYER) return true;
    }
    return false;
  }

  public static void reset() {
    for (int i = 0; i < Constants.BULLET_POOL_SIZE; i++) {
      bulletPool[i].deactivate();
    }
    free = 0;
  }

  // integrates position, handles collisions.
  public static void update(float dt, Rectangle screenRect, Player player, Boss boss) {
    Rectangle buffedRect = getBuffedScreenRect(screenRect);
    for (int i = 0; i < bulletPool.length; i++) {
      if (bulletPool[i].isActive()) bulletPool[i].update(dt, buffedRect, player, boss);
    }
  }

  // draws all the bullets
  public static void draw(SpriteBatch batch) {
    for (Bullet b : bulletPool) {
      if (b.isActive()) b.draw(batch);
    }
  }
}
