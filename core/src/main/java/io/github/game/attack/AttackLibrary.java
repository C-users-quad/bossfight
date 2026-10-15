package io.github.game.attack;

import com.badlogic.gdx.math.Vector2;
import io.github.game.Player;
import io.github.game.boss.Boss;
import io.github.game.bullets.BulletLibrary;
import io.github.game.bullets.BulletParams;
import io.github.game.bullets.BulletType;

/** contains a bunch of attack param configs that u can reuse for atttacks. */
public class AttackLibrary {
  public static AttackParams ARC_AT_PLAYER;
  public static AttackParams ARC_AT_PLAYER_STAGGERED;
  public static AttackParams ROSE_CURVE_AT_PLAYER;
  public static AttackParams EXPLOSIONS;
  public static AttackParams PLAYER_ATTACK;
  public static AttackParams SUPER_BOMB;
  public static AttackParams BLACK_HOLE;
  public static AttackParams SPIRAL;
  public static AttackParams WALL;
  public static AttackParams FLOOR_LASER_LEFT;
  public static AttackParams FLOOR_LASER_RIGHT;
  public static AttackParams LASER_AT_PLAYER;
  public static AttackParams AIMED_SINGLE_FROM_BEHIND;

  /**
   * initializes the library.
   *
   * @param player pass this sucker in so that the accelerators can be initialized
   * @param boss another sucker!
   */
  public static void init(Player player, Boss boss) {
    FLOOR_LASER_LEFT =
        new AttackParams()
            .setAttackType(AttackType.SINGLE)
            .setBulletParams(BulletLibrary.SUPER_DEADLY_FIREBALL)
            .setNumBullets(1)
            .setWarningTime(1.5f)
            .setWarningDrawTime(1f)
            .setSpawnAnchor(
                () -> new Vector2(player.getPos().x - 10f, 1f), SpawnStrategy.EXACT, 0f, 0f)
            .setDir(() -> new Vector2(1f, 0f));

    FLOOR_LASER_RIGHT =
        new AttackParams()
            .setAttackType(AttackType.SINGLE)
            .setBulletParams(BulletLibrary.SUPER_DEADLY_FIREBALL)
            .setNumBullets(1)
            .setWarningTime(1.5f)
            .setWarningDrawTime(1f)
            .setSpawnAnchor(
                () -> new Vector2(player.getPos().x + 10f, 1f), SpawnStrategy.EXACT, 0f, 0f)
            .setDir(() -> new Vector2(-1, 0f));

    WALL =
        new AttackParams()
            .setAttackType(AttackType.WALL)
            .setBulletParams(BulletLibrary.SUPER_DEADLY_FIREBALL)
            .setTarget(player)
            .setLength(200f)
            .setNumBullets(50)
            .setSpawnAnchor(player::getPos, SpawnStrategy.RANDOM, 50f, 75f)
            .setWarningTime(1f)
            .setWarningDrawTime(3f)
            .setTimeBetweenChildAttacks(1.5f)
            .setLifetime(3f)
            .setChildAttackFactoryBuilder(
                anchor ->
                    () ->
                        new AttackParams[] {
                          AttackLibrary.FLOOR_LASER_LEFT, AttackLibrary.FLOOR_LASER_RIGHT
                        });

    SPIRAL =
        new AttackParams()
            .setAttackType(AttackType.SPIRAL)
            .setBulletParams(BulletLibrary.STRONG_BOSS_FIREBALL)
            .setRadius(3f)
            .setNumBullets(300)
            .setSpawnAnchor(boss::getPos, SpawnStrategy.EXACT, 7f, 13f)
            .setWarningTime(1f)
            .setWarningDrawTime(1f)
            .setTotalAngle(360f * 9);

    BLACK_HOLE =
        new AttackParams()
            .setAttackType(AttackType.SINGLE)
            .setBulletParams(BulletLibrary.BLACK_HOLE)
            .setNumBullets(1)
            .setSpawnAnchor(boss::getPos, SpawnStrategy.EMANATING, 7f, 13f)
            .setTimeBetweenChildAttacks(1.5f)
            .setLifetime(BulletLibrary.BLACK_HOLE.despawnTimer)
            .setChildAttackFactoryBuilder(
                anchor ->
                    () ->
                        new AttackParams[] {
                          new AttackParams()
                              .setAttackType(AttackType.RING)
                              .setNumBullets(25)
                              .setSpawnAnchor(anchor, SpawnStrategy.EMANATING, 0f, 0f)
                              .setRadius(30f)
                              .setBulletParams(
                                  new BulletParams()
                                      .setType(BulletType.FIREBALL)
                                      .setDamage(30f)
                                      .setImmuneTime(1f)
                                      .setSpeed(30f)
                                      .setDespawnTimer(5f))
                        });

    ARC_AT_PLAYER =
        new AttackParams()
            .setAttackType(AttackType.ARC)
            .setBulletParams(BulletLibrary.STRONG_BOSS_FIREBALL)
            .setSpawnAnchor(boss::getPos, SpawnStrategy.RANDOM, 7f, 13f)
            .setNumBullets(15)
            .setWarningDrawTime(1f)
            .setWarningTime(.5f)
            .setArcWidth(180f);

    ARC_AT_PLAYER_STAGGERED =
        new AttackParams()
            .setAttackType(AttackType.ARC)
            .setBulletParams(BulletLibrary.STRONG_BOSS_FIREBALL)
            .setSpawnAnchor(boss::getPos, SpawnStrategy.RANDOM, 7f, 13f)
            .setNumBullets(15)
            .setTimeBetweenBullets(.03f)
            .setArcWidth(180f);

    EXPLOSIONS =
        new AttackParams()
            .setAttackType(AttackType.ARC)
            .setBulletParams(BulletLibrary.STRONG_BOSS_FIREBALL)
            .setSpawnAnchor(boss::getPos, SpawnStrategy.RANDOM, 7f, 13f)
            .setNumBullets(75)
            .setWarningTime(1f)
            .setWarningDrawTime(2f)
            .setTimeBetweenBullets(.01f)
            .setArcWidth(1440f);

    ROSE_CURVE_AT_PLAYER =
        new AttackParams()
            .setAttackType(AttackType.ROSE)
            .setBulletParams(BulletLibrary.STRONG_BOSS_FIREBALL)
            .setSpawnAnchor(boss::getPos, SpawnStrategy.RANDOM, 7f, 13f)
            .setNumBullets(50)
            .setWarningTime(.5f)
            .setWarningDrawTime(1f)
            .setNumPetals(5)
            .setRadius(4f);

    PLAYER_ATTACK =
        new AttackParams()
            .setAttackType(AttackType.SINGLE)
            .setBulletParams(BulletLibrary.PLAYER_BULLET)
            .setSpawnAnchor(player::getPos, SpawnStrategy.EXACT, 7f, 13f)
            .setNumBullets(1);

    AIMED_SINGLE_FROM_BEHIND =
        new AttackParams()
            .setAttackType(AttackType.AIMED_SINGLE)
            .setBulletParams(BulletLibrary.STRONG_BOSS_FIREBALL)
            .setTarget(player)
            .setWarningTime(.2f)
            .setWarningDrawTime(1f)
            .setLifetime(1f)
            .setSpawnAnchor(
                player::getPos,
                SpawnStrategy.RANDOM,
                16f,
                17f
                )
            .setNumBullets(1);

    SUPER_BOMB =
        new AttackParams()
            .setAttackType(AttackType.ARC)
            .setBulletParams(BulletLibrary.WEAK_BOSS_FIREBALL)
            .setSpawnAnchor(boss::getPos, SpawnStrategy.EMANATING, 7f, 13f)
            .setNumBullets(2000)
            .setWarningTime(1f)
            .setArcWidth(10000f)
            .setTimeBetweenBullets(.01f);
  }
}
