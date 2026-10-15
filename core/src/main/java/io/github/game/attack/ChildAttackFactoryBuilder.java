package io.github.game.attack;

import com.badlogic.gdx.math.Vector2;
import java.util.function.Supplier;

/**
 * EXISTS TO PREVENT WRITING <code>Function<\Supplier<\Vector2>, Supplier<\AttackParams>></code>.
 * AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
 *
 * <p>JAVA F*****G SUCKS TO DO FUNCTIONAL PROGRAMMING
 *
 * <p>ChildAttackFactoryBuilder AbstractSingletonProxyFactoryBean
 * InternalFrameInternalFrameTitlePaneInternalFrameTitlePaneMaximizeButtonWindowNotFocusedState
 * CNLabelContactRelationElderCousinMothersSiblingsDaughterOrFathersSistersDaughter
 *
 * <p>this. is. java. naming. conventions.
 */
@FunctionalInterface
public interface ChildAttackFactoryBuilder {
  Supplier<AttackParams[]> bindAnchor(Supplier<Vector2> anchor);
}
