package leandro.soares.quevedo.scheduller.view.activity.alarms;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.model.AlarmInfo;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;
import leandro.soares.quevedo.scheduller.util.AlarmUtil;
import leandro.soares.quevedo.scheduller.util.SessionManager;
import leandro.soares.quevedo.scheduller.util.TimeUtil;

@ContentView(R.layout.activity_alarms)
public class AlarmsActivity extends BaseActivity {

	private Button btnTimeStart;
	private Button btnTimeEnd;
	private TextView tvDayLength;
	private Button btnDeleteAlarms;
	private Button btnSaveAlarms;

	private AlarmInfo alarmInfo;

	@Override
	protected void onLoadComponents () {
		btnTimeStart = findViewById (R.id.etTimeStart);
		btnTimeEnd = findViewById (R.id.etTimeEnd);
		tvDayLength = findViewById (R.id.tvDayLength);
		btnDeleteAlarms = findViewById (R.id.btnDeleteAlarms);
		btnSaveAlarms = findViewById (R.id.btnSaveAlarms);
	}

	@Override
	protected void onInitValues () {
		// Setup the day length label
		tvDayLength.setText (String.format ("Carga horária diária: %s", getFormattedDayLength ()));

		// Load alarms from local storage
		alarmInfo = SessionManager.getInstance ().loadAlarmInfo ();

		if (alarmInfo != null) {
			// If any alarm saved, fill fields with its values
			btnTimeStart.setText (alarmInfo.getStartHours ());
			btnTimeEnd.setText (alarmInfo.getEndHours ());

			btnDeleteAlarms.setEnabled (true);// We can delete the actual alarms
			btnSaveAlarms.setEnabled (false);// Only allow to save after some update
		} else {
			// No alarms saved
			btnDeleteAlarms.setEnabled (false);// We can't delete
			btnSaveAlarms.setEnabled (false);// We can't save
		}

		setupDatepicker (btnTimeStart);
		setupDatepicker (btnTimeEnd);
	}

	// ----------------------------------------------------------------------- UI stuff
	@SuppressLint("DefaultLocale")
	private void setupDatepicker (final Button datePicker) {
		datePicker.setOnClickListener (view -> {
			new TimePickerDialog (
					this,
					(timePicker, hours, minutes) -> {
						// Set the properly formatted string to the datePicker
						datePicker.setText (String.format ("%02d:%02d", hours, minutes));
						// After update, we can now save (if all fields are filled)
						updateSaveButtonState ();
					},
					TimeUtil.extractHoursFromTime (datePicker.getText ().toString ()),
					TimeUtil.extractMinutesFromTime (datePicker.getText ().toString ()),
					true
			).show ();
		});
	}


	private String getDayLength () {
		TeamworkPerson account = SessionManager.getInstance ().loadAccountInfo ();
		return account.getLengthOfDay ();
	}

	@SuppressLint("DefaultLocale")
	private String getFormattedDayLength () {
		TeamworkPerson account = SessionManager.getInstance ().loadAccountInfo ();

		float time = Float.parseFloat (account.getLengthOfDay ());
		int minutes = (int) (time * 60f);
		int hours = minutes / 60;
		minutes -= hours * 60;

		if (minutes > 0) {
			return String.format ("%d horas e %d minutos", hours, minutes);
		} else {
			return String.format ("%d horas", hours);
		}
	}

	private void updateSaveButtonState () {
		btnSaveAlarms.setEnabled (btnTimeStart.length () > 0 && btnTimeEnd.length () > 0);
	}

	private void hoverInAnimation () {
		tvDayLength.animate ()
				   .scaleY (1.4f)
				   .scaleX (1.4f)
				   .setDuration (2000)
				   .setInterpolator (new AccelerateDecelerateInterpolator ())
				   .withEndAction (this::hoverOutAnimation)
				   .start ();
	}

	private void hoverOutAnimation () {
		tvDayLength.animate ()
				   .scaleY (1.5f)
				   .scaleX (1.5f)
				   .setDuration (2000)
				   .setInterpolator (new AccelerateDecelerateInterpolator ())
				   .withEndAction (this::hoverInAnimation)
				   .start ();
	}

	// ----------------------------------------------------------------------- Events
	// EVENT On button delete alarms click
	public void onBtnDeleteAlarmsClick (View view) {
		SessionManager.getInstance ().saveAlarmInfo (null);

		// We now can't delete an alarm
		btnDeleteAlarms.setEnabled (false);

		// After update, validate if all fields are filled
		updateSaveButtonState ();

		AlarmUtil.cancelAlarmSchedule (this);
	}

	// EVENT On button save alarms click
	public void onBtnSaveAlarmsClick (View view) {
		// Extract the valid day length
		float dayLength = Float.parseFloat (alarmInfo == null ? getDayLength () : alarmInfo.getDayLength ());

		if (!checkLengthOfDay (dayLength)) return;

		alarmInfo = new AlarmInfo (
				btnTimeStart.getText ().toString (),
				btnTimeEnd.getText ().toString (),
				String.valueOf (dayLength)
		);

		SessionManager.getInstance ().saveAlarmInfo (alarmInfo);

		// We now can delete an alarm
		btnDeleteAlarms.setEnabled (true);

		// Only enable it after another update
		btnSaveAlarms.setEnabled (false);

		AlarmUtil.scheduleAlarm (
				this,
				"Você esqueceu de iniciar o cronômetro?",
				TimeUtil.extractHoursFromTime (btnTimeStart.getText ().toString ()),
				TimeUtil.extractMinutesFromTime (btnTimeStart.getText ().toString ())
		);
	}

	// ----------------------------------------------------------------------- Alarm handling
	private boolean checkLengthOfDay (float dayLength) {
		// Check the length of day
		float calculatedDayLength = calculateLengthOfDay () / 60f;
		if (calculatedDayLength != dayLength) {
			// Show alert message
			showErrorDialog ("A carga horária díaria inserida não condiz com a registrada no TeamWork.\n\nPara alterar no Teamwork vá em: \n Perfil\n Edite meus dados\n Localização\n Duração do dia", () -> {
				// Animate the label
				/*ScaleAnimation scaleAnimation = new ScaleAnimation (1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setStartOffset (1000);
				scaleAnimation.setDuration (250);
				scaleAnimation.setInterpolator (new OvershootInterpolator ());*/

				// Pop up and fade in the view
				tvDayLength.animate ()
						   .alpha (1f)
						   .scaleX (1.5f)
						   .scaleY (1.5f)
						   .setInterpolator (new OvershootInterpolator ())
						   .setStartDelay (750)
						   .setDuration (250)
						   .start ();

				// Change the labels color
				ValueAnimator valueAnimator = ValueAnimator.ofArgb (tvDayLength.getCurrentTextColor (), getResources ().getColor (R.color.red));
				valueAnimator.setInterpolator (new AccelerateInterpolator ());
				valueAnimator.setStartDelay (750);
				valueAnimator.setDuration (250);
				valueAnimator.setEvaluator (new ArgbEvaluator ());
				valueAnimator.addUpdateListener (valueAnimator1 -> tvDayLength.setTextColor ((Integer) valueAnimator1.getAnimatedValue ()));
				valueAnimator.addListener (new AnimatorListenerAdapter () {
					@Override
					public void onAnimationEnd (Animator animation) {
						hoverInAnimation ();
					}
				});
				valueAnimator.start ();
			});
			return false;
		} else {
			return true;
		}
	}

	private int calculateLengthOfDay () {
		// Extract hours and minutes
		int startHours = TimeUtil.extractHoursFromTime (btnTimeStart.getText ().toString ());
		int startMinutes = TimeUtil.extractMinutesFromTime (btnTimeStart.getText ().toString ());
		// Calculate time in minutes
		int startTime = startHours * 60 + startMinutes;
		// Extract hours and minutes
		int endHours = TimeUtil.extractHoursFromTime (btnTimeEnd.getText ().toString ());
		int endMinutes = TimeUtil.extractMinutesFromTime (btnTimeEnd.getText ().toString ());
		// Calculate time in minutes
		int endTime = endHours * 60 + endMinutes;

		// Return time difference in minutes
		return endTime - startTime;
	}

}
