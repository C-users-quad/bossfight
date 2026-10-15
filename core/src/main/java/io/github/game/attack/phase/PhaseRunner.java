package io.github.game.attack.phase;

import io.github.game.attack.Attacker;
import io.github.game.bullets.Bullets;

/** updates, sequences, and executes a phase of attacks. im going crazy!!! */
public class PhaseRunner {
  public Phase phase;
  private int currAttackIdx;
  private int currEventIdx;
  private float totalTime;
  private float duration;
  private boolean clearBulletsOnReset;
  private boolean clearAttacksOnReset;

  public PhaseRunner(Phase phase, float duration, boolean clearBulletsOnReset, boolean clearAttacksOnReset) {
    this.phase = phase;
    this.currAttackIdx = 0;
    this.currEventIdx = 0;
    this.totalTime = 0;
    this.duration = duration;
    this.clearAttacksOnReset = clearAttacksOnReset;
    this.clearBulletsOnReset = clearBulletsOnReset;
  }

  /**
   * determines whether all of the attack events in the provided phase have been executed.
   *
   * @return
   */
  public boolean isFinishedExecuting() {
    return currAttackIdx == phase.attacks.length
        && currEventIdx == 0
        && phase.attacks[currAttackIdx - 1][phase.attacks[currAttackIdx - 1].length - 1]
            .isFinished();
  }

  /**
   * determines whether the total time accumulated during updates has exceeded this phaserunners
   * specified duration limit.
   *
   * <p>you choose what to do with this information, duration does not affect any functionality in
   * this muhfuh thang.
   *
   * @return bouul-ian value that is cool
   */
  public boolean hasExceededDuration() {
    return totalTime > duration;
  }

  public void reset() {
    currAttackIdx = 0;
    currEventIdx = 0;
    totalTime = 0;
    phase.reset();
    if (clearAttacksOnReset) Attacker.reset();
    if (clearBulletsOnReset) Bullets.reset();
  }

  public void update(float dt) {
    totalTime += dt;
    if (currAttackIdx >= phase.attacks.length) return;

    AttackEvent[] currAttack = phase.attacks[currAttackIdx];
    AttackEvent currEvent = phase.attacks[currAttackIdx][currEventIdx];

    if (currEvent.canExecute()) {
      currEvent.execute();
    }
    currEvent.updateDelay(dt);
    if (currEvent.isFinished()) {
      if (currEventIdx == currAttack.length - 1) {
        currEventIdx = 0;
        currAttackIdx++;
      } else {
        currEventIdx++;
      }
    }
  }
}
