package leandro.soares.quevedo.scheduller.view.activity.login;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;


import java.net.HttpURLConnection;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.interfaces.SimpleCallback;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;
import leandro.soares.quevedo.scheduller.service.TeamworkService;
import leandro.soares.quevedo.scheduller.util.KeyboardUtils;
import leandro.soares.quevedo.scheduller.util.SessionManager;
import leandro.soares.quevedo.scheduller.view.activity.home.HomeActivity;
import leandro.soares.quevedo.scheduller.view.activity.projects.ProjectsActivity;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements TextWatcher, KeyboardUtils.KeyboardVisibilityListener {

	private ScrollView scrollView;
	private EditText etEmail, etPassword;
	private Button btnLogin;
	private View dividerView;
	private ConstraintLayout constraintLayout;

	private boolean didCheck;

	@Override
	protected void onPreload () {
		checkSession ();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onLoadComponents () {
		scrollView = findViewById (R.id.scrollView);
		etEmail = findViewById (R.id.etEmail);
		etPassword = findViewById (R.id.etPassword);
		btnLogin = findViewById (R.id.btnLogin);
		dividerView = findViewById (R.id.dividerView);
		constraintLayout = findViewById (R.id.constraintLayout);
	}

	@Override
	protected void onInitValues () {
		etEmail.addTextChangedListener (this);
		etPassword.addTextChangedListener (this);

		KeyboardUtils.addKeyboardListener (this, this);

		btnLogin.setOnClickListener (this::onBtnLoginClick);
	}

	@Override
	protected void onStart () {
		super.onStart ();
		setupAnimations ();
	}

	private void setupAnimations () {
		LayoutAnimationController controller = new LayoutAnimationController (new AlphaAnimation (0f, 1f), 0.5f);
		constraintLayout.setLayoutAnimation (controller);

		ScaleAnimation scaleAnimation = new ScaleAnimation (0, 1f, 1f, 1f, 0.5f, 0.5f);
		scaleAnimation.setInterpolator (new OvershootInterpolator ());
		scaleAnimation.setDuration (250);
		dividerView.startAnimation (scaleAnimation);
	}

	private void checkSession () {
		// Loads from local storage the Saved user and credentials
		SessionManager localStorage = SessionManager.getInstance ();
		String credentials = localStorage.loadCredentials ();
		TeamworkPerson accountInfo = localStorage.loadAccountInfo ();

		// Check if saved information is valid
		if (credentials != null && !credentials.isEmpty () && accountInfo != null) {
			// If we have valid credentials saved, go to home activity directly
			startActivityFlow (HomeActivity.class);
		}
	}

	private void onBtnLoginClick (View view) {
		// Extract info from fields
		String login = etEmail.getText ().toString ();
		String password = etPassword.getText ().toString ();

		// Show loader
		showLoader ();

		// Tries to retrieve account info
		TeamworkService service = new TeamworkService (this, okhttp3.Credentials.basic (login, password));
		service.getAccountInfo (new SimpleCallback<TeamworkPerson> () {
			@Override
			public void onSuccess (TeamworkPerson response) {
				// Hide loader
				hideLoader ();

				// Save credentials to local storage
				SessionManager localStorage = SessionManager.getInstance ();
				localStorage.saveCredentials (login, password);
				localStorage.saveAccountInfo (response);

				// Go to home activity
				startActivityFlow (HomeActivity.class);
			}

			@Override
			public void onError (String message, int statusCode) {
				// Clears session
				SessionManager.getInstance ().clear ();

				// Hide loader
				hideLoader ();

				// Invalid login
				if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
					// Show error indicator on form fields
					setError (etEmail);
					setError (etPassword);
					// Show error dialog
					showErrorDialog ("Credenciais inválidas!");
				} else {
					// Show error dialog
					showErrorDialog (message);
				}
			}
		});
	}

	// --------------------------------------------- Validation

	private void validateFormFields () {
		boolean valid = true;

		if (etEmail.length () > 0) {
			clearError (etEmail);
		} else {
			valid = false;
			setError (etEmail);
		}

		if (etPassword.length () > 0) {
			clearError (etPassword);
		} else {
			valid = false;
			setError (etPassword);
		}

		btnLogin.setEnabled (valid);
	}

	private void setError (EditText editText) {
		editText.setCompoundDrawablesWithIntrinsicBounds (null, null, getResources ().getDrawable (R.drawable.ic_attention), null);
		editText.setCompoundDrawablePadding (10);
	}

	private void clearError (EditText editText) {
		editText.setCompoundDrawablesWithIntrinsicBounds (null, null, null, null);
	}

	// --------------------------------------------- Touch Callbacks

	@Override
	public void onKeyboardShown () {
		if (etEmail.hasFocus ()) {
			smoothScrollToView (etEmail);
		} else if (etPassword.hasFocus ()) {
			smoothScrollToView (etPassword);
		}
	}

	@Override
	public void onKeyboardHidden () {

	}


	private void smoothScrollToView (View view) {
		scrollView.postDelayed (() -> {
			scrollView.smoothScrollTo (0, view.getTop () - view.getHeight () / 2);
		}, 125);
	}

	// --------------------------------------------- TextWatcher Callbacks

	@Override
	public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void afterTextChanged (Editable editable) {
		validateFormFields ();
	}


}
