package io.github.game.attack.phase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/** an attack phase. composed of timed attack events. */
public class Phase {
  public AttackEvent[][] attacks;

  public Phase(AttackEvent[][] attacks) {
    this.attacks = attacks;
  }

  /**
   * a simple fisher-yates implementation. to shuffle the order. of the. barrages.
   * durrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr. im gonna go ! crazy! bang bang bang bang
   *
   * <p>!! bangbang
   *
   * <p>pmr zryyrt tohjy pm yjr lrunpstf. jru upi'tr mpy idomh s frvpfrt, str upi?
   *
   * <p>|@%||!%||@!|'|!*|% |!$||!%||@)| |@!||!(|(|!$|& ! $%#|!%|$%|!*|, !|!*|% |@%||!%||@!|?
   */
  public void shuffle(int... indices) {
    // combine all attackevents into a list
    ArrayList<AttackEvent> events = new ArrayList<>();
    for (int i : indices) events.addAll(Arrays.asList(attacks[i]));

    // shuffle the events
    Random rand = new Random();
    for (int i = events.size() - 1; i > 0; i--) {
      int j = rand.nextInt(i + 1);
      AttackEvent temp = events.get(i);
      events.set(i, events.get(j));
      events.set(j, temp);
    }

    // insert them back into attacks
    int eIdx = 0;
    for (int i : indices) {
      for (int j = 0; j < attacks[i].length; j++) {
        attacks[i][j] = events.get(eIdx);
        eIdx++;
      }
    }
  }

  public void reset() {
    for (AttackEvent[] es : attacks) for (AttackEvent e : es) e.reset();
  }
}
