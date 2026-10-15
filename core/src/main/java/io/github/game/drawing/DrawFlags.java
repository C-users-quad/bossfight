package io.github.game.drawing;

import io.github.game.Constants;

/**
 * Helper class for drawing that stores flags that determine whether certain events have occured
 * that require a visual cue.
 *
 * <p>The pattern is as follows: wherever the event occurs, call the corresponding initiater method
 * that sets the corresponding flag to true and sets the timer (a time deccumulator) to the constant
 * that corresponds to the duration of the event in seconds.
 *
 * <p>in drawutil, there will be a method that reads drawflags, and if the flag is true it will draw
 * the corresponding visual cue, and it will call the corresponding update method in drawflags which
 * decrements the deccumulator, and if its 0, sets the flag to false.
 */
public class DrawFlags {
  public static boolean showHitFlash = false;
  public static float hitFlashTimer = 0f;
  public static boolean showHealFlash = false;
  public static float healFlashTimer = 0f;
  public static boolean showDeathScreen = false;
  public static float deathScreenTimer = 0f;

  public static void doHitFlash() {
    showHitFlash = true;
    hitFlashTimer = Constants.HIT_FLASH_DURATION;
  }

  public static void doHealFlash() {
    showHealFlash = true;
    healFlashTimer = Constants.HEAL_FLASH_DURATION;
  }

  public static void updateHealFlashTimer(float dt) {
    healFlashTimer -= dt;
    healFlashTimer = Math.max(healFlashTimer, 0f);
    if (healFlashTimer <= 0f) showHealFlash = false;
  }

  public static void updateHitFlashTimer(float dt) {
    hitFlashTimer -= dt;
    hitFlashTimer = Math.max(hitFlashTimer, 0f);
    if (hitFlashTimer <= 0f) showHitFlash = false;
  }

  // updates all the timers
  public static void update(float dt) {
    if (showHitFlash) updateHitFlashTimer(dt);
    if (showHealFlash) updateHealFlashTimer(dt);
  }
}
