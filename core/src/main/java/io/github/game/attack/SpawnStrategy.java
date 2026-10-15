package io.github.game.attack;

/** determines how bullets spawn. */
public enum SpawnStrategy {
  /** the bullet spawns relative to the spawn anchors precomputed exact position */
  EXACT,
  /** the bullet spawns relative to a random position nearby the spawn anchors exact position */
  RANDOM,
  /** the bullet spawns relative to the spawn anchors exact position freshly computed every frame */
  EMANATING,
}
