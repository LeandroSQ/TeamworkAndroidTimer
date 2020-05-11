package leandro.soares.quevedo.scheduller.generic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import leandro.soares.quevedo.scheduller.annotations.ContentView;
import quevedo.soares.leandro.PartialView;

public abstract class BasePartial extends PartialView {

	//<editor-fold defaultstate="Collapsed" desc="Constructors">
	public BasePartial (Context context) {
		super (context);
	}

	public BasePartial (Context context, @Nullable AttributeSet attrs) {
		super (context, attrs);
	}

	public BasePartial (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super (context, attrs, defStyleAttr);
	}

	public BasePartial (Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super (context, attrs, defStyleAttr, defStyleRes);
	}
	//</editor-fold>

	//<editor-fold defaultstate="Collapsed" desc="Overriden methods">
	@Override
	public final View onCreateView (LayoutInflater layoutInflater) {
		// Sets the content view based on the class Annotation
		ContentView annotation = getClass ().getAnnotation (ContentView.class);
		View view = layoutInflater.inflate (annotation.value (), null);

		// Calls the event handlers
		onPreload ();

		post (this::onLoadComponents);

		return view;
	}

	@Override
	public final void onStart () {
		super.onStart ();

		onInitValues ();
	}

	@Override
	public final void onDestroy () {
		super.onDestroy ();
		onDispose ();
	}
	//</editor-fold>

	//<editor-fold defaultstate="Collapsed" desc="Abstract event handlers">
	protected void onPreload () {
	}

	protected abstract void onLoadComponents ();

	protected abstract void onInitValues ();

	public void onDispose () {
	}
	//</editor-fold>
}
