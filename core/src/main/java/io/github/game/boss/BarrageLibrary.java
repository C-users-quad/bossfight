package io.github.game.boss;

import io.github.game.attack.AttackLibrary;
import io.github.game.attack.phase.AttackEvent;

/** boss attacks. */
public class BarrageLibrary {
  public static AttackEvent[] slashes() {
    AttackEvent delayedSlash = new AttackEvent(AttackLibrary.AIMED_SINGLE_FROM_BEHIND, 2f, 1);
    AttackEvent fastSlash = new AttackEvent(AttackLibrary.AIMED_SINGLE_FROM_BEHIND, .1f, 1);
    return AttackEvent.concat(
        new AttackEvent[] {delayedSlash},
        AttackEvent.repeat(fastSlash, 40),
        new AttackEvent[] {delayedSlash},
        AttackEvent.repeat(fastSlash, 40),
        new AttackEvent[] {delayedSlash},
        AttackEvent.repeat(fastSlash, 40),
        new AttackEvent[] {delayedSlash},
        AttackEvent.repeat(fastSlash, 40));
  }

  public static AttackEvent[] walls() {
    return AttackEvent.repeat(
        new AttackEvent(AttackLibrary.WALL, AttackLibrary.WALL.lifetime, 2), 10);
  }

  public static AttackEvent[] bombs() {
    return AttackEvent.repeat(new AttackEvent(AttackLibrary.EXPLOSIONS, 1f, 1), 10);
  }

  public static AttackEvent[] thorns() {
    return AttackEvent.repeat(new AttackEvent(AttackLibrary.ROSE_CURVE_AT_PLAYER, 1f, 1), 10);
  }

  public static AttackEvent[] spirals() {
    return AttackEvent.repeat(new AttackEvent(AttackLibrary.SPIRAL, 1f, 1), 10);
  }

  public static AttackEvent[] blackHole() {
    return new AttackEvent[] {new AttackEvent(AttackLibrary.BLACK_HOLE, 0f, 1)};
  }

  public static AttackEvent[] finalAttack() {
    return AttackEvent.repeat(new AttackEvent(AttackLibrary.SUPER_BOMB, .4f, 1), 7);
  }
}
