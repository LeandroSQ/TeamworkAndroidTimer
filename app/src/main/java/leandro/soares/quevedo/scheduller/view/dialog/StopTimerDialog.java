package leandro.soares.quevedo.scheduller.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.annotations.DialogConfiguration;
import leandro.soares.quevedo.scheduller.enumerator.Direction;
import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.generic.BaseDialog;
import leandro.soares.quevedo.scheduller.model.ClockInfo;
import leandro.soares.quevedo.scheduller.util.KeyboardUtils;
import leandro.soares.quevedo.scheduller.util.TimeUtil;

@ContentView(R.layout.dialog_stop_timer)
@DialogConfiguration(
		direction = Direction.Down
)
public class StopTimerDialog extends BaseDialog {

	private EditText etDescription;
	private Button btnTimeStart;
	private Button btnTimeEnd;
	private Button btnSend;
	private ClockInfo clockInfo;
	private OnEnterDescriptionListener listener;

	private boolean ignoreDismiss;

	public static StopTimerDialog showDialog (BaseActivity activity, ClockInfo clockInfo) {
		StopTimerDialog dialog = new StopTimerDialog ();
		dialog.listener = (OnEnterDescriptionListener) activity;
		dialog.clockInfo = clockInfo;
		dialog.show (activity);

		return dialog;
	}

	@Override
	protected void onPreload () {

	}

	@Override
	protected void onLoadComponents () {
		etDescription = findViewById (R.id.etDescription);
		btnSend = findViewById (R.id.btnSend);
		btnTimeStart = findViewById (R.id.etTimeStart);
		btnTimeEnd = findViewById (R.id.etTimeEnd);
	}

	@Override
	protected void onInitValues () {
		// Sets the ime input
		/*etDescription.setOnEditorActionListener ((textView, i, keyEvent) -> {
			if (i == EditorInfo.IME_ACTION_DONE && getActivity () != null) {
				//onBtnSendClick (textView);
				KeyboardUtils.hideKeyboard (getActivity ());
				return true;
			} else {
				return false;
			}
		});*/

		// Open keyboard
		etDescription.requestFocus ();
		InputMethodManager imm = (InputMethodManager) getContext ().getSystemService (Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

		// Extract time from clock info
		btnTimeStart.setText (getDateFromTime (clockInfo.getFirstTimeCount ().getStartedMillis ()));
		btnTimeEnd.setText (getDateFromTime (clockInfo.getLastTimeCount ().getEndedMillis ()));

		// Setup events
		setupDatepicker (btnTimeStart);
		setupDatepicker (btnTimeEnd);
		btnSend.setOnClickListener (this::onBtnSendClick);
	}

	@Override
	protected void onDispose () {
		super.onDispose ();

		// If the user has dismissed the dialog
		if (!ignoreDismiss) {
			listener.onCancelRegistry ();
		}

		// Hide keyboard
		etDescription.clearFocus ();
		InputMethodManager imm = (InputMethodManager) getContext ().getSystemService (Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput (InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	// ----------------------------------------------------------------------- UI stuff
	@SuppressLint("DefaultLocale")
	private void setupDatepicker (final Button datePicker) {
		datePicker.setOnClickListener (view -> {
			new TimePickerDialog (
					StopTimerDialog.this.getContext (),
					(timePicker, hours, minutes) -> {
						// Set the properly formatted string to the datePicker
						datePicker.setText (String.format ("%02d:%02d", hours, minutes));
						// After update, we can now save (if all fields are filled)
						updateSendButtonState ();
					},
					TimeUtil.extractHoursFromTime (datePicker.getText ().toString ()),
					TimeUtil.extractMinutesFromTime (datePicker.getText ().toString ()),
					true
			).show ();
		});
	}

	private void updateSendButtonState () {
		btnSend.setEnabled (btnTimeStart.length () > 0 && btnTimeEnd.length () > 0);
	}
	// ----------------------------------------------------------------------- Time handling
	@SuppressLint("SimpleDateFormat")
	private String getDateFromTime (long millis) {
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis (millis);
		return new SimpleDateFormat ("HH:mm").format (calendar.getTime ());
	}

	private long switchTimeFromDate (long millis, String time) {
		// Extract time from string
		int hours = TimeUtil.extractHoursFromTime (time);
		int minutes = TimeUtil.extractMinutesFromTime (time);

		// Create a calendar in that specified date
		Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis (millis);

		// Update time in calendar
		calendar.set (Calendar.HOUR_OF_DAY, hours);
		calendar.set (Calendar.MINUTE, minutes);

		// Return the millis
		return calendar.getTimeInMillis ();
	}

	private void checkTimeParts () {
		// Check the startTime
		long startedMillis = clockInfo.getFirstTimeCount ().getStartedMillis ();
		if (!btnTimeStart.getText ().equals (getDateFromTime (startedMillis))) {
			// If different, change only the hours and minutes
			clockInfo.getFirstTimeCount ().setStartedMillis (switchTimeFromDate (startedMillis, btnTimeStart.getText ().toString ()));
		}

		// Check the endTime
		long endedMillis = clockInfo.getLastTimeCount ().getEndedMillis ();
		if (!btnTimeEnd.getText ().equals (getDateFromTime (endedMillis))) {
			// If different, change only the hours and minutes
			clockInfo.getLastTimeCount ().setEndedMillis (switchTimeFromDate (endedMillis, btnTimeEnd.getText ().toString ()));
		}
	}
	// ----------------------------------------------------------------------- Events
	// EVENT On button send click
	private void onBtnSendClick (View view) {
		this.ignoreDismiss = true;

		this.dismiss ();

		// Check if the user has changed either StartTime or EndTime
		// IMPORTANT, don't need to pass the clock info by argument 'cos we are dealing with reference
		checkTimeParts ();

		// Call the listener with the fields value
		this.listener.onSendRegistry (etDescription.getText ().toString ());
	}

	public interface OnEnterDescriptionListener {
		void onSendRegistry (String description);
		void onCancelRegistry ();
	}

}
