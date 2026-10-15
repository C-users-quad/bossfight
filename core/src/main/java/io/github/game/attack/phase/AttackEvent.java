package io.github.game.attack.phase;

import io.github.game.attack.AttackParams;
import io.github.game.attack.Attacker;
import java.util.Arrays;

/**
 * used for timed attacks in sequence.
 *
 * <p>basically, used to compose attack phases.
 *
 * <p>this stores a delay in seconds before the next attack event is activated and an attackparams.
 */
public class AttackEvent {
  /** the parameters of the attack this attackevent executes */
  private AttackParams params;

  /** determines if this attackevent has attacked. */
  private boolean executed;

  /** increments every frame */
  private float intervalTimer;

  /** amount of time before the attacks are executed */
  private float delay;

  /** number of concurrently executed attacks */
  private int numAttacks;

  public AttackEvent(AttackParams params, float delay, int numAttacks) {
    this.params = params;
    this.delay = delay;
    this.intervalTimer = delay;
    this.executed = false;
    this.numAttacks = numAttacks;
  }

  public AttackEvent cpy() {
    return new AttackEvent(params, delay, numAttacks);
  }

  public static AttackEvent[] repeat(AttackEvent e, int n) {
    AttackEvent[] events = new AttackEvent[n];
    for (int i = 0; i < n; i++) {
      events[i] = e.cpy();
    }
    return events;
  }

  /**
   * i didnt know there was rust in java. anyway, chains the thingymabobs.
   *
   * @param arrays all the thingymaboos.
   * @return thingymaboos, but concatted. cat. con. cat. with cat. meow.
   */
  public static AttackEvent[] concat(AttackEvent[]... arrays) {
    return Arrays.stream(arrays).flatMap(Arrays::stream).toArray(AttackEvent[]::new);
  }

  public float getDuration() {
    return params.getTotalDuration();
  }

  public boolean canExecute() {
    return !executed;
  }

  public void execute() {
    for (int i = 0; i < numAttacks; i++) Attacker.doAttack(params);
    executed = true;
  }

  public void updateDelay(float dt) {
    intervalTimer -= dt;
    intervalTimer = Math.max(0f, intervalTimer);
  }

  public boolean isFinished() {
    return intervalTimer <= 0f;
  }

  public void reset() {
    this.intervalTimer = delay;
    this.executed = false;
  }
}
