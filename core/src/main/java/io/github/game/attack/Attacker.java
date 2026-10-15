package io.github.game.attack;

import io.github.game.Constants;
import io.github.game.Player;

/** initiates, stores, and updates all attacks given parameters. */
public class Attacker {
  public static Attack[] attackPool;
  private static int attackFree;

  static {
    attackPool = new Attack[Constants.ATTACK_POOL_SIZE];
    for (int i = 0; i < Constants.ATTACK_POOL_SIZE; i++) {
      attackPool[i] = new Attack();
    }
  }

  public static void doAttack(AttackParams params) {
    Attack a = getNextActiveAttack();
    if (a != null) a.activate(params);
  }

  public static void reset() {
    for (Attack a : attackPool) {
      a.deactivate();
    }
    attackFree = 0;
  }

  public static void update(Player player, float dt) {
    for (Attack a : attackPool) {
      if (a.isActive()) a.update(player, dt);
    }
  }

  private static Attack getNextActiveAttack() {
    int idx;
    for (int i = 0; i < Constants.ATTACK_POOL_SIZE; i++) {
      idx = (attackFree + i) % Constants.ATTACK_POOL_SIZE;
      if (!attackPool[idx].isActive()) {
        attackFree = idx + 1;
        return attackPool[idx];
      }
    }
    return null; // attacks are all used up.
  }
}
