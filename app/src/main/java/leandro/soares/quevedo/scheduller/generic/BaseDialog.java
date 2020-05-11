package leandro.soares.quevedo.scheduller.generic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.annotations.DialogConfiguration;
import leandro.soares.quevedo.scheduller.enumerator.DialogType;
import leandro.soares.quevedo.scheduller.enumerator.Direction;

public abstract class BaseDialog extends DialogFragment {

	private static final int STATE_IDLE = 0;
	private static final int STATE_DRAGGING = 1;
	private static final int STATE_ENDING = 2;

	protected boolean isAnimated = true;
	protected DialogType dialogType = DialogType.FillWindow;
	protected boolean isDimmed = true;
	protected boolean isDraggable = true;
	protected boolean isCancelable = true;
	protected boolean isOverStatusbar = false;
	protected boolean isBackgroundTransparent = true;
	protected Direction dragDirection = Direction.Down;
	protected int showTime = -1;

	protected boolean isAnimationRunning = false;
	protected boolean isDismissed = false;
	protected boolean onlyDismiss = false;

	protected int swipeVerticalOffset, swipeHorizontalOffset;

	protected View rootView;
	private DialogInterface.OnDismissListener onDismissListener;

	//<editor-fold defaultstate="Collapsed" desc="Abstract event handlers">

	/**
	 * Initializes variables and components
	 */
	protected abstract void onInitValues ();

	/**
	 * Find views and load components
	 */
	protected abstract void onLoadComponents ();

	/**
	 * Destroys the fragment
	 */
	protected void onDispose () {
	}

	/**
	 * Loads services and resources
	 */
	protected void onPreload () {
	}
	//</editor-fold>

	//<editor-fold defaultstate="Collapsed" desc="FragmentDialog methods override">
	@Override
	public void onCreate (@Nullable Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);

		// Extracts the configuration of the annotation
		DialogConfiguration configuration = getClass ().getAnnotation (DialogConfiguration.class);
		if (configuration != null) {
			isAnimated = configuration.isAnimated ();
			dialogType = configuration.type ();
			isOverStatusbar = configuration.isOverStatusbar ();
			isDraggable = configuration.isDraggable ();
			isCancelable = configuration.isCancelable ();
			dragDirection = configuration.direction ();
			isDimmed = configuration.isDimmed ();
			showTime = configuration.showTime ();
			isBackgroundTransparent = configuration.isBackgroundTransparent ();
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		//Dialog dialog = super.onCreateDialog (savedInstanceState);
		Dialog dialog = new Dialog (getContext (), getTheme ()) {
			private VelocityTracker velocityTracker;
			private float lastX, lastY;
			private int touchSlope = 0;
			private int minimumDismissTrigger;
			private int minimumDismissVelocity;

			private boolean shoulInterceptNextEvent;

			private int touchPointerID;

			private int draggingState;

			@Override
			public void onBackPressed () {
				BaseDialog.this.dismiss ();
			}

			@Override
			protected void onStart () {
				super.onStart ();

				velocityTracker = VelocityTracker.obtain ();
				// Retrieve the device specific configuration
				ViewConfiguration configuration = ViewConfiguration.get (getContext ());
				touchSlope = configuration.getScaledTouchSlop ();

				getContentView ().measure (0, 0);
				// Calculate the minimum dismiss trigger
				minimumDismissTrigger = getContentView ().getMeasuredHeight () / 3;
				// Calculate te minimum dismiss velocity
				minimumDismissVelocity = (int) (getContentView ().getMeasuredHeight () / 1.5f);
			}

			@Override
			public boolean dispatchTouchEvent (@NonNull MotionEvent ev) {
				// Ignore any touch when dismissed!
				if (isDismissed || isAnimationRunning) {
					return false;
				} else {
					return super.dispatchTouchEvent (ev);
				}
			}

			@Override
			public boolean onTouchEvent (@NonNull MotionEvent event) {
				if (isAnimationRunning) return false;

				if (isDraggable) {
					velocityTracker.addMovement (event);

					switch (event.getActionMasked ()) {
						case MotionEvent.ACTION_DOWN:
							draggingState = STATE_DRAGGING;

							touchPointerID = ((event.getAction () & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT);

							shoulInterceptNextEvent = false;
							break;
						case MotionEvent.ACTION_MOVE:
							// Check if is starting a Drag
							checkStartingDrag (event);

							updateDrag (event);

							shoulInterceptNextEvent = true;
						case MotionEvent.ACTION_UP:
							// Verifies if we actually lifted finger
							if (!shoulInterceptNextEvent) {
								endDrag ();
							} else {
								shoulInterceptNextEvent = false;
							}
							break;
						case MotionEvent.ACTION_CANCEL:
							endDrag ();
							break;
					}

					// Update the last motion variables
					lastX = (int) event.getX (touchPointerID);
					lastY = (int) event.getY (touchPointerID);

					return true;
				} else {
					return super.onTouchEvent (event);
				}
			}

			private void checkStartingDrag (MotionEvent event) {
				if (draggingState != STATE_IDLE) return;

				float eventX = event.getX (touchPointerID), eventY = event.getY (touchPointerID);

				switch (dragDirection) {
					case Up:
						// If has moved a minimum distance
						if (eventY - lastY >= touchSlope) {
							// Start dragging
							draggingState = STATE_DRAGGING;
						}
						break;
					case Down:
					case Center:
						// If has moved a minimum distance
						if (eventY - lastY <= touchSlope) {
							// Start dragging
							draggingState = STATE_DRAGGING;
						}
						break;
					case Left:
						// If has moved a minimum distance
						if (eventX - lastX >= touchSlope) {
							// Start dragging
							draggingState = STATE_DRAGGING;
						}
						break;
					case Right:
						// If has moved a minimum distance
						if (eventX - lastX <= touchSlope) {
							// Start dragging
							draggingState = STATE_DRAGGING;
						}
						break;
				}
			}

			private void updateDrag (MotionEvent event) {
				if (draggingState != STATE_DRAGGING) {
					return;
				}

				float delta;

				switch (dragDirection) {
					case Up:
						delta = lastY - event.getY (touchPointerID);
						swipeVerticalOffset -= delta;

						swipeVerticalOffset = clamp (swipeVerticalOffset, -getContentView ().getMeasuredHeight (), 0);

						getContentView ().setTranslationY (swipeVerticalOffset);
						break;
					case Down:
					case Center:
						delta = lastY - event.getY (touchPointerID);
						swipeVerticalOffset -= delta;

						swipeVerticalOffset = clamp (swipeVerticalOffset, 0, getContentView ().getMeasuredHeight ());

						getContentView ().setTranslationY (swipeVerticalOffset);
						break;
					case Left:
						delta = lastX - event.getX (touchPointerID);
						swipeHorizontalOffset -= delta;

						getContentView ().setTranslationX (swipeHorizontalOffset);
						break;
					case Right:
						delta = lastX - event.getX (touchPointerID);
						swipeHorizontalOffset += delta;

						getContentView ().setTranslationX (swipeHorizontalOffset);
						break;
				}

				//ViewCompat.postInvalidateOnAnimation (getContentView ());
			}

			private int clamp (int x, int min, int max) {
				return x < min ? min : x > max ? max : x;
			}

			private void endDrag () {
				if (draggingState != STATE_DRAGGING) {
					return;
				}

				draggingState = STATE_ENDING;

				velocityTracker.computeCurrentVelocity (50);

				float velocityY = velocityTracker.getYVelocity (touchPointerID);
				float velocityX = velocityTracker.getXVelocity (touchPointerID);

				switch (dragDirection) {
					case Up:
						// Check the drag direction
						if (velocityY < 0) {
							// Check if we passed the minimum dismiss velocity
							// Or if we did dragged up the minimum distance
							if (Math.abs (velocityY) >= minimumDismissVelocity) {
								// Dismiss the dialog
								setupExitAnimations (() -> {
									isDismissed = true;
									onDestroyDialog ();
									this.dismiss ();
								});
							} else {
								// Reset dragging
								translateBack (() -> draggingState = STATE_IDLE);
							}
						} else {
							// Reset dragging
							translateBack (() -> draggingState = STATE_IDLE);
						}
						break;
					case Down:
					case Center:
						// Check the drag direction
						if (velocityY > 0) {
							// Check if we passed the minimum dismiss velocity
							// Or if we did dragged up the minimum distance
							if (Math.abs (velocityY) >= minimumDismissVelocity) {
								// Dismiss the dialog
								setupExitAnimations (() -> {
									isDismissed = true;
									onDestroyDialog ();
									this.dismiss ();
								});
							} else {
								// Reset dragging
								translateBack (() -> draggingState = STATE_IDLE);
							}
						} else {
							// Reset dragging
							translateBack (() -> draggingState = STATE_IDLE);
						}
						break;
					case Left:
						// Check the drag direction
						if (velocityX < 0) {
							// Check if we passed the minimum dismiss velocity
							// Or if we did dragged up the minimum distance
							if (Math.abs (velocityX) >= minimumDismissVelocity) {
								// Dismiss the dialog
								setupExitAnimations (() -> {
									isDismissed = true;
									onDestroyDialog ();
									this.dismiss ();
								});
							} else {
								// Reset dragging
								translateBack (() -> draggingState = STATE_IDLE);
							}
						} else {
							// Reset dragging
							translateBack (() -> draggingState = STATE_IDLE);
						}
						break;
					case Right:
						// Check the drag direction
						if (velocityX > 0) {
							// Check if we passed the minimum dismiss velocity
							// Or if we did dragged up the minimum distance
							if (Math.abs (velocityX) >= minimumDismissVelocity) {
								// Dismiss the dialog
								setupExitAnimations (() -> {
									isDismissed = true;
									onDestroyDialog ();
									this.dismiss ();
								});
							} else {
								// Reset dragging
								translateBack (() -> draggingState = STATE_IDLE);
							}
						} else {
							// Reset dragging
							translateBack (() -> draggingState = STATE_IDLE);
						}
						break;
				}

				velocityTracker.clear ();

				shoulInterceptNextEvent = false;
			}

			private void translateBack (Runnable callback) {
				isAnimationRunning = true;

				switch (dragDirection) {
					case Up:
					case Down:
					case Center: {
						ValueAnimator animator = ValueAnimator.ofInt (swipeVerticalOffset, 0);

						animator.addUpdateListener (valueAnimator -> {
							swipeVerticalOffset = (int) valueAnimator.getAnimatedValue ();
							getContentView ().setTranslationY (swipeVerticalOffset);
						});
						animator.addListener (new AnimatorListenerAdapter () {
							@Override
							public void onAnimationEnd (Animator animation) {
								super.onAnimationEnd (animation);
								isAnimationRunning = false;

								callback.run ();
							}
						});
						animator.start ();
						break;
					}
					case Left:
					case Right: {
						ValueAnimator animator = ValueAnimator.ofInt (swipeHorizontalOffset, 0);

						animator.addUpdateListener (valueAnimator -> {
							swipeHorizontalOffset = (int) valueAnimator.getAnimatedValue ();
							getContentView ().setTranslationX (swipeHorizontalOffset);
						});
						animator.addListener (new AnimatorListenerAdapter () {
							@Override
							public void onAnimationEnd (Animator animation) {
								super.onAnimationEnd (animation);
								isAnimationRunning = false;

								callback.run ();
							}
						});
						animator.start ();
						break;
					}
				}


			}
		};

		init (dialog, false);
		return dialog;
	}

	private void init (Dialog dialog, boolean initialized) {
		if (dialog != null) {

			// Checks if the dialog should not be cancelable
			if (!isCancelable) {
				dialog.setCancelable (false);
				dialog.setCanceledOnTouchOutside (false);
			}

			Window window = dialog.getWindow ();

			// Check if the background should transparent
			if (isBackgroundTransparent) {
				window.setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
			}

			WindowManager.LayoutParams params = window.getAttributes ();
			// Defines the layout by dialog type
			switch (dialogType) {
				case Fullscreen:// Make it in front of StatusBar like an AlertWindow
					params.width = ViewGroup.LayoutParams.MATCH_PARENT;
					params.height = ViewGroup.LayoutParams.MATCH_PARENT;

					if (!initialized) {
						dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
						window.setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
					}
					break;
				case FillWindow:
					params.width = ViewGroup.LayoutParams.MATCH_PARENT;
					params.height = ViewGroup.LayoutParams.MATCH_PARENT;
					break;
				case WrapContent:
					params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
					params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
					break;
				case FillWidth:
					params.width = ViewGroup.LayoutParams.MATCH_PARENT;
					params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
					break;
			}

			// Setup the dialog direction, for dissmiss and placement
			switch (dragDirection) {
				case Up:
					params.gravity = Gravity.TOP;
					break;
				case Down:
					params.gravity = Gravity.BOTTOM;
					break;
				case Left:
					params.gravity = Gravity.START;
					break;
				case Right:
					params.gravity = Gravity.END;
					break;
				case Center:
					params.gravity = Gravity.CENTER;
					break;
			}

			// Checks if the background should not be dimmed
			if (!this.isDimmed) {
				params.dimAmount = 0;
			}

			window.setAttributes (params);

			if (!initialized) {
				dialog.setOnDismissListener ((dialogInterface) -> dismiss ());

				if (isOverStatusbar) {
					window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				}
			}
		}
	}

	@Nullable
	@Override
	public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		// Sets the content view based on the class Annotation
		ContentView annotation = getClass ().getAnnotation (ContentView.class);
		this.rootView = inflater.inflate (annotation.value (), container);

		// Calls the dialog setup methods
		this.onPreload ();

		this.onLoadComponents ();

		// Call the method 'onInitValues' after the dialog initialization
		new Handler ().post (this::onInitValues);

		// Handles the default slide animation, and the animation direction
		if (this.isAnimated) {
			setupEnterAnimations ();
		}

		return rootView;
	}

	@Override
	public void onStart () {
		super.onStart ();
		init (getDialog (), true);

		rootView.measure (0, 0);

		if (this.showTime != -1) {
			new Handler ().postDelayed (this::dismiss, showTime);
		}
	}


	//</editor-fold>

	//<editor-fold defaultstate="Collapsed" desc="Animations">
	private void setupEnterAnimations () {
		isAnimationRunning = true;
		AnimationSet animationSet = new AnimationSet (false);

		switch (this.dragDirection) {
			case Up: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_SELF,
						-1,
						Animation.RELATIVE_TO_SELF,
						0
				);
				translateAnimation.setDuration (500);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Down: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_SELF,
						1,
						Animation.RELATIVE_TO_SELF,
						0
				);
				translateAnimation.setDuration (500);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Left: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_SELF,
						-1,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_SELF,
						0,
						Animation.RELATIVE_TO_SELF,
						0
				);
				translateAnimation.setDuration (500);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Right: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_SELF,
						1,
						Animation.RELATIVE_TO_SELF,
						0,
						Animation.RELATIVE_TO_SELF,
						0,
						Animation.RELATIVE_TO_SELF,
						0
				);
				translateAnimation.setDuration (500);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Center: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_SELF,
						0.3f,
						Animation.RELATIVE_TO_SELF,
						0
				);
				translateAnimation.setDuration (150);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());
				animationSet.addAnimation (translateAnimation);

				AlphaAnimation alphaAnimation = new AlphaAnimation (0f, 1f);
				alphaAnimation.setDuration (250);
				animationSet.addAnimation (alphaAnimation);
				break;
			}
		}

		animationSet.setAnimationListener (new Animation.AnimationListener () {
			@Override
			public void onAnimationStart (Animation animation) {

			}

			@Override
			public void onAnimationEnd (Animation animation) {
				isAnimationRunning = false;
			}

			@Override
			public void onAnimationRepeat (Animation animation) {

			}
		});

		getContentView ().startAnimation (animationSet);
	}

	private void setupExitAnimations (Runnable runnable) {
		AnimationSet animationSet = new AnimationSet (false);

		switch (this.dragDirection) {
			case Up: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_SELF,
						swipeVerticalOffset / rootView.getMeasuredHeight (),
						Animation.RELATIVE_TO_SELF,
						-1
				);
				translateAnimation.setDuration (300);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Down: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_SELF,
						swipeVerticalOffset / rootView.getMeasuredHeight (),
						Animation.RELATIVE_TO_SELF,
						1
				);
				translateAnimation.setDuration (300);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Left: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_SELF,
						swipeHorizontalOffset / rootView.getMeasuredWidth (),
						Animation.RELATIVE_TO_SELF,
						-1,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0
				);
				translateAnimation.setDuration (300);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Right: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_SELF,
						swipeHorizontalOffset / rootView.getMeasuredWidth (),
						Animation.RELATIVE_TO_SELF,
						11,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0
				);
				translateAnimation.setDuration (300);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());

				animationSet.addAnimation (translateAnimation);
				break;
			}
			case Center: {
				TranslateAnimation translateAnimation = new TranslateAnimation (
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_PARENT,
						0,
						Animation.RELATIVE_TO_SELF,
						0,
						Animation.RELATIVE_TO_SELF,
						0.3f
				);
				translateAnimation.setDuration (500);
				translateAnimation.setInterpolator (new AccelerateDecelerateInterpolator ());
				animationSet.addAnimation (translateAnimation);

				AlphaAnimation alphaAnimation = new AlphaAnimation (1f, 0f);
				alphaAnimation.setDuration (250);
				animationSet.addAnimation (alphaAnimation);
				break;
			}
		}

		animationSet.setAnimationListener (new Animation.AnimationListener () {
			@Override
			public void onAnimationStart (Animation animation) {
				isAnimationRunning = true;
			}

			@Override
			public void onAnimationEnd (Animation animation) {
				runnable.run ();
				isAnimationRunning = false;
			}

			@Override
			public void onAnimationRepeat (Animation animation) {

			}
		});

		if (Looper.getMainLooper ().equals (Looper.getMainLooper ())) {
			getContentView ().startAnimation (animationSet);
		} else {
			new Handler (Looper.getMainLooper ()).post (() -> getContentView ().startAnimation (animationSet));
		}

	}
	//</editor-fold>

	//<editor-fold defaultstate="Collapsed" desc="Utils">
	protected <T extends View> T findViewById (@IdRes int id) {
		if (this.rootView != null) {
			return this.rootView.findViewById (id);
		} else {
			return null;
		}
	}

	public void show (BaseActivity activity) {
		super.show (activity.getSupportFragmentManager (), getClass ().getSimpleName ());
	}

	public void setOnDismissListener (DialogInterface.OnDismissListener onDismissListener) {
		this.onDismissListener = onDismissListener;
	}

	public View getRootView () {
		return rootView;
	}

	public boolean isShowing () {
		if (this.isDismissed) {
			return false;
		} else if (this.getDialog () != null) {
			return this.getDialog ().isShowing ();
		} else {
			return false;
		}
	}

	public void dismiss () {
		isDismissed = true;

		if (onlyDismiss) {
			super.dismiss ();
			return;
		}

		setupExitAnimations (() -> {
			onDestroyDialog ();
			getDialog ().dismiss ();
		});
	}

	private void onDestroyDialog () {
		if (this.onDismissListener != null) {
			this.onDismissListener.onDismiss (null);
		}

		onDispose ();
	}

	public View getContentView () {
		View view = this.rootView.findViewWithTag ("content");

		if (view != null) {
			return view;
		} else {
			return this.rootView;
		}
	}
	//</editor-fold>


}
