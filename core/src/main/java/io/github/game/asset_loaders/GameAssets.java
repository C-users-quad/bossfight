package io.github.game.asset_loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import io.github.game.Constants;

/** literally all of the assets. */
public class GameAssets {
  public static Texture player;
  public static Texture boss;
  public static Texture bg;
  public static Texture bridge;
  public static Texture bullet;
  public static Texture healthBar;
  public static Texture hitFlash;
  public static Texture platform;
  public static Texture healFlash;
  public static Texture healIndicator;
  public static Texture healBar;
  public static Texture bossBar;
  public static Texture fireball;
  public static Texture playerBullet;
  public static Texture flightBar;
  public static Texture flightIndicator;
  public static TextureAtlas blackHoleAtlas;
  public static BitmapFont comicSans;
  public static GlyphLayout scratchGlyphLayout;
  public static Animation<TextureRegion> blackHoleAnim;
  public static Sound hitSound;
  public static Sound deathSound;
  public static Sound healSound;
  public static Music bgMusic;

  public static void load() {
    player = new Texture("characters/player.png");
    boss = new Texture("characters/boss.png");
    bg = new Texture("backgrounds/bg.png");
    bridge = new Texture("objects/bridge.png");
    bullet = new Texture("objects/bullets/bullet.png");
    healthBar = new Texture("objects/ui/healthBar.png");
    hitFlash = new Texture("objects/ui/hitFlash.png");
    platform = new Texture("objects/platform.png");
    healFlash = new Texture("objects/ui/healFlash.png");
    healIndicator = new Texture("objects/ui/healIndicator.png");
    healBar = new Texture("objects/ui/bar.png");
    bossBar = new Texture("objects/ui/bossBar.png");
    fireball = new Texture("objects/bullets/fireball.png");
    playerBullet = new Texture("objects/bullets/playerBullet.png");
    flightBar = new Texture("objects/ui/bar.png");
    flightIndicator = new Texture("objects/ui/flightIndicator.png");
    blackHoleAtlas = new TextureAtlas("objects/bullets/black-hole.atlas");
    FreeTypeFontGenerator comicSansGen =
        new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comic Sans MS.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter comicSansParams =
        new FreeTypeFontGenerator.FreeTypeFontParameter();
    comicSansParams.color = Color.RED;
    comicSansParams.borderWidth = 4;
    comicSansParams.borderColor = Color.BLACK;
    comicSansParams.size = 32;
    comicSans = comicSansGen.generateFont(comicSansParams);
    comicSansGen.dispose();
    scratchGlyphLayout = new GlyphLayout();
    blackHoleAnim =
        new Animation<>(Constants.BH_ANIM_FRAMETIME, blackHoleAtlas.findRegions("frame"));
    blackHoleAnim.setPlayMode(PlayMode.LOOP);
    hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.mp3"));
    deathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/death.mp3"));
    healSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gulp.mp3"));
    bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/mus.mp3"));
    bgMusic.setLooping(true);
  }

  public static void dispose() {
    bg.dispose();
    boss.dispose();
    player.dispose();
    bridge.dispose();
    bullet.dispose();
    healthBar.dispose();
    hitFlash.dispose();
    platform.dispose();
    healFlash.dispose();
    healIndicator.dispose();
    healBar.dispose();
    bossBar.dispose();
    blackHoleAtlas.dispose();
    playerBullet.dispose();
    fireball.dispose();
    flightBar.dispose();
    flightIndicator.dispose();
    comicSans.dispose();
    hitSound.dispose();
    deathSound.dispose();
    healSound.dispose();
    bgMusic.dispose();
  }
}
