package leandro.soares.quevedo.scheduller.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import leandro.soares.quevedo.scheduller.enumerator.DialogType;
import leandro.soares.quevedo.scheduller.enumerator.Direction;

@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface DialogConfiguration {
	boolean isAnimated () default true;

	DialogType type () default DialogType.FillWindow;

	boolean isDimmed () default true;

	boolean isDraggable () default true;

	boolean isCancelable () default true;

	boolean isBackgroundTransparent () default true;

	boolean isOverStatusbar () default false;

	int showTime () default -1;

	Direction direction () default Direction.Down;
}
