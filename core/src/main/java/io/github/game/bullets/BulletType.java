package io.github.game.bullets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.game.asset_loaders.GameAssets;

/**
 * configs for bullet types. mainly used to define draw/hitbox sizes and texture related concerns.
 */
public enum BulletType {
  FIREBALL(new Vector2(1f, 1f), new Vector2(.25f, .25f), BulletTarget.PLAYER, false),
  PLAYER(new Vector2(1f, 1f), new Vector2(.25f, .25f), BulletTarget.BOSS, false),
  BLACK_HOLE(new Vector2(10f, 10f), new Vector2(5f, 5f), BulletTarget.PLAYER, true);

  public final Vector2 hitboxSize;
  public final Vector2 drawingSize;
  public final BulletTarget target;
  public final boolean animated;

  BulletType(Vector2 drawingSize, Vector2 hitboxSize, BulletTarget target, boolean animated) {
    this.hitboxSize = hitboxSize;
    this.drawingSize = drawingSize;
    this.target = target;
    this.animated = animated;
  }

  /**
   * provides the texture of the bullet. should be used for non-animated sprites.
   *
   * @return
   */
  public Texture getTexture() {
    switch (this) {
      case FIREBALL:
        return GameAssets.fireball;
      case PLAYER:
        return GameAssets.playerBullet;
      default:
        return GameAssets.bullet;
    }
  }

  /** should be used for animated sprites: gets the animation. */
  public Animation<TextureRegion> getAnimation() {
    switch (this) {
      case BLACK_HOLE:
        return GameAssets.blackHoleAnim;
      default:
        return null;
    }
  }
}
