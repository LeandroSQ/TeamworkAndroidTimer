package leandro.soares.quevedo.scheduller.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author Leandro Soares Quevedo
 * @author leandro.soares@operacao.rcadigital.com.br
 * @since 08/10/2018
 */
public final class KeyboardUtils {

	private static char lastUpdateState;

	/**
	 * Force the softkeyboard to hide
	 *
	 * @param activity The specified Activity
	 */
	public static void hideKeyboard (Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService (Activity.INPUT_METHOD_SERVICE);
			View view = activity.getCurrentFocus ();

			if (view != null) {
				imm.hideSoftInputFromWindow (view.getWindowToken (), 0);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	/**
	 * Force the softkeyboard to show
	 *
	 * @param activity  The specified Activity
	 * @param viewGroup The target view to gain focus
	 */
	public static void showKeyboard (Activity activity, View viewGroup) {
		InputMethodManager inputMethodManager =
				(InputMethodManager) activity.getSystemService (Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInputFromWindow (
				viewGroup.getApplicationWindowToken (),
				InputMethodManager.SHOW_FORCED, 0
		);
	}

	/**
	 * Setups a keyboard visibility listener on the activity's root view
	 *
	 * @param activity The specified Activity
	 * @param listener The target listener
	 */
	public static void addKeyboardListener (Activity activity, KeyboardVisibilityListener listener) {
		// Finds the root view on screen
		View rootView = activity.getWindow ().getDecorView ().getRootView ();

		// Reset the state tracking variable
		lastUpdateState = ' ';

		// Setup keyboard show/hide events
		rootView.getViewTreeObserver ().addOnGlobalLayoutListener (() -> {
			// Navigation bar height
			int navigationBarHeight = 0;
			int resourceId = activity.getResources ().getIdentifier ("navigation_bar_height", "dimen", "android");
			if (resourceId > 0) {
				navigationBarHeight = activity.getResources ().getDimensionPixelSize (resourceId);
			}

			// Status bar height
			int statusBarHeight = 0;
			resourceId = activity.getResources ().getIdentifier ("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				statusBarHeight = activity.getResources ().getDimensionPixelSize (resourceId);
			}

			// Display window size for the app layout
			Rect rect = new Rect ();
			activity.getWindow ().getDecorView ().getWindowVisibleDisplayFrame (rect);

			// Screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
			int keyboardHeight = rootView.getRootView ().getHeight () - (statusBarHeight + navigationBarHeight + rect.height ());


			// The validation of lastUpdateState ensure won't happen duplicated events
			if (keyboardHeight <= 0) {
				// Check if the last state wasn't visible
				if (lastUpdateState != 'v') {
					listener.onKeyboardHidden ();// On Hide Keyboard
				}
				lastUpdateState = 'v';
			} else {
				// Check if the last state wasn't hidden
				if (lastUpdateState != 'h') {
					listener.onKeyboardShown ();// On Show Keyboard
				}
				lastUpdateState = 'h';
			}
		});
	}

	public interface KeyboardVisibilityListener {
		void onKeyboardShown ();

		void onKeyboardHidden ();
	}
}
