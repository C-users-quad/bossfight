package io.github.game.bullets;

import io.github.game.Player;
import io.github.game.boss.Boss;
import io.github.game.Constants;

/**
 * <b>stores a bunch of configs for bullet behavior on here.</b>
 *
 * <p>in the library, i use this <b>naming convention</b>:
 * damageStrength_whoFiresTheBullet_bulletType
 *
 * <p>so for example, STRONG_BOSS_FIREBALL refers to a bullet that:
 *
 * <ul>
 *   <li>does a lot of damage (strong) or does little damage but provides no immunity time, look its
 *       goddamn strong ok
 *   <li>is fired by the boss
 *   <li>looks like fireball
 * </ul>
 *
 * <p>ALSO NO, "looks like fireball" wasnt a grammatical error, the fireballs are LITERALLY images
 * of the alcoholic beverage FIREBALL.
 *
 * <p>ill also append other keywords depending on behavior such as:
 *
 * <ul>
 *   <li><b>HOMING</b> for bullets with accelerators toward some target
 *   <li><b>COWARDLY</b> for bullets that despawn on timer or on hit
 * </ul>
 *
 * <p><b>just kidding i probably will be too lazy to follow literally any of this.</b>
 */
public class BulletLibrary {
  public static BulletParams STRONG_BOSS_FIREBALL;
  public static BulletParams WEAK_BOSS_FIREBALL;
  public static BulletParams BLACK_HOLE;
  public static BulletParams PLAYER_BULLET;
  public static BulletParams SUPER_DEADLY_FIREBALL;
  public static BulletParams SPEEDY_STRONG_BOSS_FIREBALL;

  public static void init(Player player, Boss boss) {
    STRONG_BOSS_FIREBALL =
        new BulletParams()
            .setType(BulletType.FIREBALL)
            .setDamage(15f)
            .setImmuneTime(.3f)
            .setSpeed(30f)
            .setTarget(BulletTarget.PLAYER)
            .setDespawnOutOfBounds(true);

    SPEEDY_STRONG_BOSS_FIREBALL =
        new BulletParams()
            .setType(BulletType.FIREBALL)
            .setDamage(15f)
            .setImmuneTime(.3f)
            .setSpeed(180f)
            .setTarget(BulletTarget.PLAYER)
            .setDespawnOutOfBounds(true);

    SUPER_DEADLY_FIREBALL =
        new BulletParams()
            .setType(BulletType.FIREBALL)
            .setDamage(50f)
            .setSpeed(200f)
            .setTarget(BulletTarget.PLAYER)
            .setDespawnTimer(5f);

    BLACK_HOLE =
        new BulletParams()
            .setType(BulletType.BLACK_HOLE)
            .setDamage(1f)
            .setSpeed(15f)
            .setTarget(BulletTarget.PLAYER)
            .setAcceleratorFactory(() -> Accelerator.towardPoint(player::getPos, Constants.BLACK_HOLE_ACC))
            .setPlayerAcceleratorFactory(() -> b -> Accelerator.towardPoint(b::getPos, 15f))
            .setDespawnTimer(30f);

    WEAK_BOSS_FIREBALL =
        new BulletParams()
            .setType(BulletType.FIREBALL)
            .setDamage(3f)
            .setImmuneTime(.5f)
            .setSpeed(30f)
            .setTarget(BulletTarget.PLAYER)
            .setDespawnOutOfBounds(true);

    PLAYER_BULLET =
        new BulletParams()
            .setType(BulletType.PLAYER)
            .setAcceleratorFactory(
                () -> Accelerator.timed(Accelerator.towardPoint(boss::getPos, 30f), 3f, 0f))
            .setDamage(1f)
            .setDespawnOnHit(true)
            .setDespawnOutOfBounds(true)
            .setDespawnTimer(3f)
            .setSpeed(15f)
            .setTarget(BulletTarget.BOSS);
  }
}
