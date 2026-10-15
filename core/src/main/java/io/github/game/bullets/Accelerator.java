package io.github.game.bullets;

import com.badlogic.gdx.math.Vector2;
import java.util.function.Supplier;

/** Accelerates a target. */
@FunctionalInterface
public interface Accelerator {
  Vector2 getAcceleration(Vector2 targetPos, float dt);

  /**
   * does not accelerate.
   *
   * @return
   */
  static Accelerator none() {
    return (targetPos, dt) -> Vector2.Zero;
  }

  /** limits the time an accelerator is active for. */
  static Accelerator timed(Accelerator inner, float duration, float activationDelay) {
    float[] elapsed = {0f};
    return (targetPos, dt) -> {
      elapsed[0] += dt;
      if (elapsed[0] >= duration) return Vector2.Zero;
      ;
      if (elapsed[0] < activationDelay) return Vector2.Zero;
      return inner.getAcceleration(targetPos, dt);
    };
  }

  /**
   * accelerates toward a point.
   *
   * @param p
   * @param magAcc
   * @return
   */
  static Accelerator towardPoint(Supplier<Vector2> p, float magAcc) {
    return (targetPos, dt) -> p.get().cpy().sub(targetPos).nor().scl(magAcc);
  }
}
