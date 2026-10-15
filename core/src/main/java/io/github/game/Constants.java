package io.github.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants {
  // how strongly black hole bullets accelerate the player toward itself
  public static final float BLACK_HOLE_ACC = 75f;
  // how long the black hole animation frames last for
  public static final float BH_ANIM_FRAMETIME = .1f;
  // maximum player speed in x
  public static final float PLAYER_HORIZ_SPEED = 25f;
  // max speed player falls at if holding space and no flight time
  public static final float PLAYER_GLIDE_SPEED = 10f;
  // maximum player speed in y
  public static final float PLAYER_VERT_SPEED = 25f;
  // player accelerations
  public static final float PLAYER_HORIZ_ACC = 20f;
  public static final float PLAYER_VERT_ACC = 50f;
  public static final float GRAVITY = 20f;
  public static final float FRICTION = 25f;
  // players initial health
  public static final float PLAYER_HEALTH = 100f;
  // player respawn point (gets teleported here when they die)
  public static final Vector2 PLAYER_RESPAWN_POS = new Vector2(0f, 0f);
  // player cant go below this
  public static final float FLOOR_Y = 0f;
  // player flight time in seconds
  public static final float PLAYER_FLIGHT_TIME = 10f;
  // world dimensions in arbitraty units ("meters")
  public static final float PPM = 100f; // pixels per meter
  public static final float WORLD_WIDTH = 1600 / PPM;
  public static final float WORLD_HEIGHT = 900 / PPM;
  // higher = camera accelerates quicker toward players position
  public static final float CAM_LERP_SPEED = 5f;
  // percent image width the hitbox is of a charcaters bounding box
  public static final float HITBOX_SCALE_W = .5f;
  // percent image height the hitbox is of a characters bounding box
  public static final float HITBOX_SCALE_H = .5f;
  // number of bridge segments that are drawn
  public static final int NUM_BRIDGE_SEGMENTS = 200;
  // scale for bridge drawing size
  public static final float BRIDGE_DRAW_SCALE = 5f;
  // world boundary in meters in x direction
  public static final float WORLD_BORDER = 1000f;
  // arbitrary number used for maximum bullet pool size
  public static final int BULLET_POOL_SIZE = 4096;
  // bullet image side length in meters
  public static final float BULLET_IMAGE_SIZE = .25f;
  // bullet hitbox side length in meters
  public static final float BULLET_HITBOX_SIZE = .1f;
  // camera zoom
  public static final float CAM_ZOOM = 3f;
  // how much damage a bullet does
  public static final float BULLET_DAMAGE = 5f;
  // time in seconds player is immune for after being damaged
  public static final float PLAYER_IMMUNITY_TIME = .2f;
  // how much to scale up the screen rect when doing screen collisions on bullets
  public static final float SCREEN_RECT_BUFF = 2f;
  // ui padding, space between elements if they are next to eachother.
  // otherwise, space between element and screen boundaries. (in pixels)
  public static final float UI_PADDING = 20f;
  // color of health bar
  public static final Color HEALTH_BAR_COLOR = new Color(1f, 0f, 0f, 1f);
  // color of back of health bar (color below the red health rect)
  public static final Color BAR_BG_COLOR = new Color(.125f, .114f, .114f, 1f);
  // random attack spawn deviation range (in meters).
  // maximum an attacks spawning position can deviate from the players position.
  // duration of a hit flash in seconds
  public static final float HIT_FLASH_DURATION = .5f;
  // duration of a heal flash in seconds
  public static final float HEAL_FLASH_DURATION = .5f;
  // height of a platform in meters
  public static final float PLATFORM_HEIGHT = .5f;
  // cooldown time between heals in seconds
  public static final float PLAYER_HEAL_COOLDOWN_TIME = 10f;
  // amount healed when player heals
  public static final float PLAYER_HEAL_AMOUNT = 30f;
  // radius of idle infinity movement of boss in meters
  // contact damage of the box
  public static final float WARNING_LINE_WIDTH = .3f;
  public static final float BOSS_CONTACT_DAMAGE = 15f;
  /* the higher, the curvier or the more accurate the rose curve warning. */
  public static final int NUM_ROSE_CURVE_WARNING_SAMPLE_POINTS = 100;
  // size of the attack pool array in Attacks
  public static final int ATTACK_POOL_SIZE = 1024;
  // bounds on wandering distance for boss in meters
  public static final int BOSS_MAX_WANDER_RADIUS = 12;
  // time in seconds that player bullets home toward boss
  public static final float PLAYER_BULLET_HOMING_TIME = 1f;
  public static final float PLAYER_ATTACK_COOLDOWN_TIME = .1f;

  /**
   * im done documenting ts bro. im not gonna do it. 😭😭😭 if you dont get what these constants
   * mean, thats on you buddy 🥀
   */
  public static final Color HEAL_BAR_COLOR = new Color(0f, .3f, 1f, 1f);
  public static final float HIT_SOUND_VOLUME = 0.25f;
  public static final float DEATH_SOUND_VOLUME = 0.25f;
  public static final float HEAL_SOUND_VOLUME = 1f;
  public static final float PLAYER_DASH_IMMUNE_TIME = .2f;
  public static final Color FLIGHT_BAR_COLOR = new Color(0f, .3f, 1f, 1f);
  public static final float PLAYER_DASH_SPEED = 10f;
  public static final float PLAYER_DASH_BOOST_TIME = .75f;
  public static final float PLAYER_DASH_COOLDOWN_TIME = 1f;
  public static final int BOSS_MIN_WANDER_RADIUS = 6;
  public static final float BOSS_IMMUNITY_TIME = .2f;
  public static final float BOSS_CHASE_START_DIST = 15f;
  public static final float BOSS_CHASE_END_DIST = 5f;
  public static final float BOSS_CHASE_MAX_SPEED = 75f;
  public static final float BOSS_CHASE_MAX_STEER_FORCE = 65f;
  public static final float BOSS_WANDER_ACCEL = 25f;
  public static final float BOSS_WANDER_LERP_SPEED = 2f;
  public static final float BOSS_WANDER_ARRIVE_DIST = 3f;
  public static final float BOSS_WANDER_PLAYER_DIST = 10f;
  public static final float BOSS_CHASE_DRAG = 2f;
  public static final float BOSS_WANDER_DRAG = 4f;
  public static final float BOSS_MIN_WANDER_TIMER = 4f;
  public static final float BOSS_MAX_WANDER_TIMER = 6f;
  public static final float BOSS_INTER_PHASE_REST_TIME = 2f;
  public static final float BOSS_HEALTH = 1000f;
}
