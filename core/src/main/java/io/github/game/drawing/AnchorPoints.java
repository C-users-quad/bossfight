package io.github.game.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/** something that i barely use. */
public enum AnchorPoints {
  TOPLEFT,
  BOTTOMLEFT,
  MIDLEFT,
  TOPRIGHT,
  MIDRIGHT,
  CENTER,
  MIDBOTTOM,
  BOTTOMRIGHT,
  MIDTOP;

  public static void drawAnchored(
      SpriteBatch batch, Texture tex, float x, float y, AnchorPoints anchor, float w, float h) {
    Vector2 drawPos = AnchorPoints.getAnchoredPoint(anchor, x, y, w, h);
    batch.draw(tex, drawPos.x, drawPos.y, w, h);
  }

  /**
   * returns the anchored point of a point on a rect / AABB
   *
   * @param anchor
   * @param x
   * @param y
   * @param w
   * @param h
   * @return
   */
  public static Vector2 getAnchoredPoint(AnchorPoints anchor, float x, float y, float w, float h) {
    float anchored_x, anchored_y;
    switch (anchor) {
      case TOPLEFT:
        anchored_x = x;
        anchored_y = y - h;
        break;
      case BOTTOMLEFT:
        anchored_x = x;
        anchored_y = y;
        break;
      case MIDLEFT:
        anchored_x = x;
        anchored_y = y - h * 0.5f;
        break;
      case TOPRIGHT:
        anchored_x = x - w;
        anchored_y = y - h;
        break;
      case MIDRIGHT:
        anchored_x = x - w;
        anchored_y = y - h * 0.5f;
        break;
      case CENTER:
        anchored_x = x - w * 0.5f;
        anchored_y = y - h * 0.5f;
        break;
      case MIDBOTTOM:
        anchored_x = x - w * 0.5f;
        anchored_y = y;
        break;
      case BOTTOMRIGHT:
        anchored_x = x - w;
        anchored_y = y;
        break;
      case MIDTOP:
        anchored_x = x - w * 0.5f;
        anchored_y = y - h;
        break;
      default:
        anchored_x = x;
        anchored_y = y;
    }
    return new Vector2(anchored_x, anchored_y);
  }
}
