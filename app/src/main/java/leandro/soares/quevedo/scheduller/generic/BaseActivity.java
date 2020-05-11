package leandro.soares.quevedo.scheduller.generic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.annotations.ContentView;

public abstract class BaseActivity extends AppCompatActivity {
	@AnimRes
	private int enterAnimationIn = R.anim.slide_right_in, enterAnimationOut = R.anim.slide_right_out;
	@AnimRes
	private int exitAnimationIn = R.anim.slide_left_in, exitAnimationOut = R.anim.slide_left_out;
	@AnimRes
	private int closeAnimationIn = 0, closeAnimationOut = R.anim.slide_down_in;

	private View view;

	private int taskCount;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);

		// Calls the spinnerEvent handlers
		this.onPreload ();

		// Sets the content view based on the class Annotation
		ContentView annotation = getClass ().getAnnotation (ContentView.class);
		setContentView (annotation.value ());

		// Get the root view
		view = ((ViewGroup) this.findViewById (android.R.id.content)).getChildAt (0);
		// Remove auto focus
		new Handler ().post (() -> {
			view.setFocusable (true);
			view.setFocusableInTouchMode (true);
			view.requestFocus ();
		});

		this.onLoadComponents ();

		new Handler ().post (this::onInitValues);
	}

	//<editor-fold defaultstate="Collapsed" desc="Abstract spinnerEvent handlers">

	/**
	 * Loads services and resources
	 */
	protected void onPreload () {
	}

	/**
	 * Initializes variables and components
	 */
	protected abstract void onInitValues ();

	/**
	 * Disposes objects on activity close
	 */
	protected abstract void onLoadComponents ();

	//</editor-fold>

	//<editor-fold defaultstate="Collapsed" desc="Activity methods override">


	@Override
	protected void onDestroy () {
		// If the dialog is showing, dismiss
		if (this.progressDialog != null && this.progressDialog.isShowing ()) {
			this.progressDialog.dismiss ();
		}

		super.onDestroy ();
	}

	public void startActivityFlow (Class activity) {
		Intent intent = new Intent (this, activity).setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity (intent);
		overridePendingTransition (R.anim.fade_in, R.anim.fade_out);

		finish ();
	}

	public void startActivity (Class activity) {
		Intent intent = new Intent (this, activity);
		ArrayList<Pair<View, String>> elements = new ArrayList<> ();

		ViewGroup viewGroup = (ViewGroup) view;
		for (int i = 0; i < viewGroup.getChildCount (); i++) {
			View v = viewGroup.getChildAt (i);
			if (v.getTransitionName () != null && !v.getTransitionName ().isEmpty ()) {
				elements.add (new Pair<> (v, v.getTransitionName ()));
			}
		}

		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation (this, elements.toArray (new Pair[0]));
		startActivity (intent, options.toBundle ());
		overridePendingTransition (enterAnimationIn, enterAnimationOut);
	}

	@Override
	public void startActivityForResult (Intent intent, int requestCode) {
		super.startActivityForResult (intent, requestCode);
		overridePendingTransition (enterAnimationIn, enterAnimationOut);
	}

	@Override
	public void onBackPressed () {
		super.onBackPressed ();

		// Check if is a root of a task, therefore sets the specified closed animation
		if (isTaskRoot ()) {
			overridePendingTransition (closeAnimationIn, closeAnimationOut);
		} else {
			// Otherwise, use the default navigation animation
			overridePendingTransition (exitAnimationIn, exitAnimationOut);
		}
	}

	@Override
	public void finish () {
		super.finish ();

		// Check if is a root of a task, therefore sets the specified closed animation
		if (isTaskRoot ()) {
			overridePendingTransition (closeAnimationIn, closeAnimationOut);
		} else {
			// Otherwise, use the default navigation animation
			overridePendingTransition (exitAnimationIn, exitAnimationOut);
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Loading management">
	public void showLoader () {
		if (this.taskCount <= 0) {
			this.progressDialog = ProgressDialog.show (this, "Por favor, aguarde.", "Carregando dados...");
			this.taskCount = 1;
		} else {
			this.taskCount++;
		}
	}

	public void hideLoader () {
		if (this.taskCount > 1) {
			this.taskCount--;
		} else {
			this.taskCount = 0;

			// If the progress dialog can be dismissed
			if (this.progressDialog != null && this.progressDialog.isShowing ()) {
				this.progressDialog.dismiss ();
			}

			this.progressDialog = null;
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="Collapsed" desc="Utils">
	public void showErrorDialog (String message) {
		new AlertDialog.Builder (this)
				.setIcon (R.drawable.ic_attention)
				.setTitle ("Erro!")
				.setMessage (message)
				.setPositiveButton ("OK", null)
				.show ();
	}


	public void showErrorDialog (String message, Runnable callback) {
		new AlertDialog.Builder (this)
				.setIcon (R.drawable.ic_attention)
				.setTitle ("Erro!")
				.setMessage (message)
				.setPositiveButton ("OK", null)
				.setOnDismissListener ((view) -> callback.run ())
				.show ();
	}

	public void showSuccessDialog (String message) {
		new AlertDialog.Builder (this)
				.setIcon (R.drawable.ic_success)
				.setTitle ("Sucesso!")
				.setMessage (message)
				.setPositiveButton ("OK", null)
				.show ();
	}

	public View getRootView () {
		return view;
	}
	//</editor-fold>
}
