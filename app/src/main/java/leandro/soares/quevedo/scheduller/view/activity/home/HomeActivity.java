package leandro.soares.quevedo.scheduller.view.activity.home;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.annotations.ContentView;
import leandro.soares.quevedo.scheduller.background.ClockNotificationService;
import leandro.soares.quevedo.scheduller.generic.BaseActivity;
import leandro.soares.quevedo.scheduller.helper.HolidaysHelper;
import leandro.soares.quevedo.scheduller.interfaces.SimpleCallback;
import leandro.soares.quevedo.scheduller.model.AlarmInfo;
import leandro.soares.quevedo.scheduller.model.ClockInfo;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkPerson;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTask;
import leandro.soares.quevedo.scheduller.model.teamwork.TeamworkTimeEntry;
import leandro.soares.quevedo.scheduller.service.TeamworkService;
import leandro.soares.quevedo.scheduller.util.ClockUtil;
import leandro.soares.quevedo.scheduller.util.SessionManager;
import leandro.soares.quevedo.scheduller.util.TimeUtil;
import leandro.soares.quevedo.scheduller.view.activity.alarms.AlarmsActivity;
import leandro.soares.quevedo.scheduller.view.activity.projects.ProjectsActivity;
import leandro.soares.quevedo.scheduller.view.activity.weekly.WeeklyTimeActivity;
import leandro.soares.quevedo.scheduller.view.dialog.StopTimerDialog;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements StopTimerDialog.OnEnterDescriptionListener {

	private static final int SELECT_PROJECT_AND_TASK_REQUEST_CODE = 1337;
	private static final int NOTIFICATIONS_PERMISSION_REQUEST_CODE = 1337;

	private LinearLayout lnProgress;
	private TextView tvHoursReport;
	private ProgressBar pbHoursPercent;
	private FrameLayout flClockContainer;
	private LinearLayout lnButtonContainer;
	private ImageButton btnPlayPause;
	private ImageButton btnStop;
	private TextView tvPassedTime;
	private TextView tvAdjustAlarms;
	private BottomSheetBehavior<LinearLayout> lnProgressBottomSheet;

	private TextView tvCurrentTaskLabel;
	private TextView tvCurrentTask;

	private ClockInfo clockInfo;
	private boolean servicesLoaded;
	private boolean timeIndicatorVisible;
	private boolean pendingSwitchTask;
	private boolean ignoreResume;
	private boolean pendingStartClock;

	private BroadcastReceiver receiver;

	private HolidaysHelper holidaysHelper;

	@Override
	protected void onPreload () {
		manageShortcuts ();

		manageNotificationPermissions ();

		// Setup the communication with push notifications
		receiver = new BroadcastReceiver () {
			@Override
			public void onReceive (Context context, Intent intent) {
				switch (intent.getStringExtra ("action")) {
					case "playPause":
						resetClockUi ();
						break;
					case "stop":
						onBtnStopClick (btnStop);
						break;
					case "changeTask":
						onBtnChangeTaskClick (tvCurrentTask);
						break;
				}
			}
		};

		// Update the holiday date list
		holidaysHelper = new HolidaysHelper (this);

		manageShortcutStartAction ();
	}

	@Override
	protected void onLoadComponents () {
		lnProgress = findViewById (R.id.lnProgress);
		tvHoursReport = findViewById (R.id.tvHoursReport);
		pbHoursPercent = findViewById (R.id.pbHoursPercent);
		flClockContainer = findViewById (R.id.flClockContainer);
		lnButtonContainer = findViewById (R.id.lnButtonContainer);
		btnPlayPause = findViewById (R.id.btnPlayPause);
		btnStop = findViewById (R.id.btnStop);
		tvPassedTime = findViewById (R.id.tvPassedTime);
		tvAdjustAlarms = findViewById (R.id.tvAdjustAlarms);
		tvCurrentTaskLabel = findViewById (R.id.tvCurrentTaskLabel);
		tvCurrentTask = findViewById (R.id.tvCurrentTask);

		lnProgressBottomSheet = BottomSheetBehavior.from (lnProgress);

		btnStop.setOnLongClickListener (view -> {
			this.onBtnStopLongClick (view);
			return true;
		});
	}

	@Override
	protected void onInitValues () {
		setupButtonContainerLayoutTransition ();
		loadTimeEntries ();
		startBackgroundNotificationService ();
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == SELECT_PROJECT_AND_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			TeamworkTask teamworkTask = (TeamworkTask) data.getSerializableExtra ("task");

			// Set the label's text
			tvCurrentTaskLabel.setVisibility (View.VISIBLE);
			tvCurrentTask.setText (teamworkTask.getProjectName () + " : " + teamworkTask.getContent ());

			if (clockInfo == null) {
				// No clock running
				createClockForTask (teamworkTask);
				startClock ();
			} else {
				// There is already a clock running, just update its task
				clockInfo.setTargetTask (teamworkTask);

				// Save it
				SessionManager.getInstance ().saveClockInfo (clockInfo);
			}
		}
	}

	@Override
	protected void onStart () {
		super.onStart ();

		LocalBroadcastManager.getInstance (this).registerReceiver (this.receiver, new IntentFilter (HomeActivity.class.getName ()));
	}

	@Override
	protected void onResume () {
		super.onResume ();

		if (ignoreResume) {
			ignoreResume = false;
		} else {
			resetClockUi ();
		}
	}

	@Override
	protected void onStop () {
		super.onStop ();
		LocalBroadcastManager.getInstance (this).unregisterReceiver (this.receiver);
	}

	@Override
	protected void onDestroy () {
		super.onDestroy ();

		dismissBackgroundNotificationService ();
	}

	// ----------------------------------------------------------------------- Shortcut handling
	private void manageShortcuts () {
		ShortcutManager shortcutManager = null;
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
			shortcutManager = getSystemService (ShortcutManager.class);

			if (shortcutManager.getDynamicShortcuts ().size () <= 0) {
				ShortcutInfo shortcut = new ShortcutInfo.Builder (this, "startClock")
						.setShortLabel ("Iniciar")
						.setLongLabel ("Iniciar o cronômetro")
						.setIcon (Icon.createWithResource (this, R.drawable.ic_play))
						.setIntent (new Intent (this, HomeActivity.class).setAction ("startClock"))
						.build ();

				shortcutManager.setDynamicShortcuts (Arrays.asList (shortcut));
			}
		}

	}

	private void manageShortcutStartAction () {
		String action = this.getIntent ().getAction ();
		if (Objects.equals (action, "startClock")) {
			pendingStartClock = true;
		}
	}

	// ----------------------------------------------------------------------- Notification handling
	private void manageNotificationPermissions () {
		// Only Marshmallow and above needs this
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Check permission status
			boolean permissionGranted = ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED;
			if (permissionGranted) { return; }

			// If not granted and can request permissions directly
			if (ActivityCompat.shouldShowRequestPermissionRationale (this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
				// Request permissions
				ActivityCompat.requestPermissions (this, new String[] {Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATIONS_PERMISSION_REQUEST_CODE);
			} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// Only Oreo has a special treatment for notifications
				// Show a message to user
				new AlertDialog.Builder (this)
						.setTitle ("Atenção")
						.setMessage ("Este aplicativo necessita da permissão para mostrar notificações.")
						.setPositiveButton ("Conceder permissão", (dialogInterface, i) -> {
							Intent settingsIntent = new Intent (Settings.ACTION_APP_NOTIFICATION_SETTINGS)
									.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK)
									.putExtra (Settings.EXTRA_APP_PACKAGE, getPackageName ());
							startActivity (settingsIntent);
						})
						.setNegativeButton ("Continuar sem notificações", null)
						.show ();
			}
		}
	}

	// ----------------------------------------------------------------------- Alarm handling
	public void onBtnAdjustAlarmsClick (View view) {
		startActivity (new Intent (this, AlarmsActivity.class));
	}

	// ----------------------------------------------------------------------- Events
	// EVENT On button play pause click
	public void onBtnPlayPauseClick (View view) {
		// Check if the last time part has ended
		if (ClockUtil.isClockRunning ()) {
			pauseClock ();
			hideStopButton ();
		} else {
			// Check if there is an instance of clock started
			if (clockInfo != null) {
				// Then start counting
				resumeClock ();
				showStopButton ();
			} else {
				// Else, select the task and start a new clock
				selectTask ();
			}
		}
	}

	// EVENT On button stop click
	public void onBtnStopClick (View view) {
		stopClock (true);
	}

	// EVENT On button stop long click
	public void onBtnStopLongClick (View view) {
		new AlertDialog.Builder (this)
				.setTitle ("Atenção")
				.setMessage ("Gostaria de cancelar o cronômetro?")
				.setPositiveButton ("sim", (dialogInterface, i) -> cancelClock ())
				.setNegativeButton ("Não", null)
				.show ();
	}

	// EVENT On button change task click
	public void onBtnChangeTaskClick (View view) {
		new AlertDialog.Builder (this)
				.setTitle ("Atenção")
				.setMessage ("Gostaria de encerrar o cronômetro e depois alternar de tarefa?")
				.setPositiveButton ("Enviar com descrição", (dialogInterface, i) -> {
					pendingSwitchTask = true;
					stopClock (true);
				})
				.setNeutralButton ("Enviar sem descrição", (dialogInterface, i) -> {
					pendingSwitchTask = true;
					stopClock (false);
				})
				.setNegativeButton ("Apenas mudar de tarefa", (dialogInterface, i) -> {
					selectTask ();
				})
				.show ();
	}

	// EVENT On dialog stop timer return
	public void onSendRegistry (String description) {
		showLoader ();

		sendTimeCountPart (0, description);
	}

	// EVENT On dialog stop timer return canceled
	public void onCancelRegistry () {
		// Resume the timer
		ClockUtil.setClockRunning (true);

		clockInfo.getLastTimeCount ().setEndedMillis (System.currentTimeMillis ());
		clockInfo.getLastTimeCount ().setHasEnded (false);

		// Save to local storage
		SessionManager.getInstance ().saveClockInfo (clockInfo);

		// Resets the ui timer
		tvPassedTime.removeCallbacks (this::updateTimer);
		tvPassedTime.postDelayed (this::updateTimer, 500);
	}

	// EVENT On linear layout progress click
	public void onLnProgressClick (View view) {
		if (lnProgressBottomSheet.getState () == BottomSheetBehavior.STATE_EXPANDED)
			lnProgressBottomSheet.setState (BottomSheetBehavior.STATE_COLLAPSED);
		else
			lnProgressBottomSheet.setState (BottomSheetBehavior.STATE_EXPANDED);
	}
	// ----------------------------------------------------------------------- Clock handling
	private void handleStartupAction () {
		if (getIntent ().hasExtra ("action")) {
			switch (getIntent ().getStringExtra ("action")) {
				case "stop":
					onBtnStopClick (btnStop);
					break;
				case "changeTask":
					onBtnChangeTaskClick (tvCurrentTask);
					break;
			}

			getIntent ().removeExtra ("action");
		}
	}

	@SuppressLint("DefaultLocale")
	private void setupClock () {
		clockInfo = SessionManager.getInstance ().loadClockInfo ();

		if (clockInfo != null) {
			// We got a clock started
			updateTimeUi ((int) clockInfo.getPassedSeconds ());

			// Set the label's text
			tvCurrentTaskLabel.setVisibility (View.VISIBLE);
			tvCurrentTask.setText (clockInfo.getTargetTask ().getProjectName () + " : " + clockInfo.getTargetTask ().getContent ());

			// Check if the last time part has ended
			if (clockInfo.getLastTimeCount ().hasEnded ()) {
				// If so, show the play image
				changeButtonImageToPlay ();
				hideStopButton ();
				ClockUtil.setClockRunning (false);
			} else {
				// Otherwise, show the pause image
				changeButtonImageToPause ();
				showStopButton ();
				ClockUtil.setClockRunning (true);

				// Stops the internal timer
				tvPassedTime.removeCallbacks (this::updateTimer);
				// Create a timer indicator for visual proposes
				updateTimer ();

				notifyBackgroundNotificationService ();
			}
		} else {
			// If so, show the play image
			changeButtonImageToPlay ();
			hideStopButton ();
			ClockUtil.setClockRunning (false);
		}

	}

	private void startBackgroundNotificationService () {
		Intent serviceIntent = new Intent (this, ClockNotificationService.class);
		startService (serviceIntent);
	}

	private void notifyBackgroundNotificationService () {
		Intent intent = new Intent (ClockNotificationService.SERVICE_IDENTIFIER);
		intent.putExtra ("action", ClockUtil.isClockRunning () ? "start" : "pause");
		sendBroadcast (intent);
	}

	private void dismissBackgroundNotificationService () {
		Intent intent = new Intent (ClockNotificationService.SERVICE_IDENTIFIER);
		intent.putExtra ("action", "dismiss");
		sendBroadcast (intent);
	}

	private void selectTask () {
		// Else, select the task and start a new clock
		startActivityForResult (new Intent (this, ProjectsActivity.class), SELECT_PROJECT_AND_TASK_REQUEST_CODE);
	}

	private void createClockForTask (TeamworkTask task) {
		this.clockInfo = new ClockInfo ();
		this.clockInfo.setTargetTask (task);
		this.clockInfo.setTimeParts (new ArrayList<> ());
	}

	private void startClock () {
		AlarmInfo alarmInfo = SessionManager.getInstance ().loadAlarmInfo ();

		// If we don't have alarms setup
		if (alarmInfo == null) {
			// Just start normaly
			resumeClock ();
		} else {
			// Loads the time and converts it to a Calendar object
			String time = alarmInfo.getStartHours ();
			Calendar workingStartTime = Calendar.getInstance ();
			workingStartTime.set (Calendar.HOUR_OF_DAY, TimeUtil.extractHoursFromTime (time));
			workingStartTime.set (Calendar.MINUTE, TimeUtil.extractMinutesFromTime (time));

			// Get the current time
			Calendar calendar = Calendar.getInstance ();

			// Calculate the difference between time
			long timeDiff = calendar.getTimeInMillis () - workingStartTime.getTimeInMillis ();
			long maximumTime = 1000 * 60 * 15;// 15 minutes converted to Milliseconds

			// If the user is late **IGNORE IT IF WE DID SWITCH THE TASKS**
			if (!pendingSwitchTask && timeDiff >= maximumTime) {
				this.ignoreResume = true;
				// Show a dialog asking if the user is late
				new AlertDialog.Builder (this)
						.setTitle ("Atenção")
						.setMessage ("Você está atrasado?")
						.setPositiveButton ("Sim, me atrasei", (dialogInterface, i) -> {
							this.ignoreResume = false;

							// Just start regularly
							resumeClock ();
						})
						.setNegativeButton ("Não, esqueci de iniciar o timer", (dialogInterface, i) -> {
							this.ignoreResume = false;

							// User has forgotten to start the timer
							startDelayedClock (workingStartTime);
						})
						.show ();
			} else {
				// Otherwise, Start the clock now
				resumeClock ();
			}
		}
	}

	private void startDelayedClock (Calendar startTime) {
		// Starts the clock as if it had started on the specified time
		ClockUtil.startDelayedClock (clockInfo, startTime.getTimeInMillis ());

		notifyBackgroundNotificationService ();

		// Stops the internal timer
		tvPassedTime.removeCallbacks (this::updateTimer);

		// Create a timer indicator for visual proposes
		updateTimer ();

		showStopButton ();
		changeButtonImageToPause ();
	}

	private void resumeClock () {
		ClockUtil.resumeClock (clockInfo);

		notifyBackgroundNotificationService ();

		// Stops the internal timer
		tvPassedTime.removeCallbacks (this::updateTimer);
		// Create a timer indicator for visual proposes
		updateTimer ();

		showStopButton ();
		changeButtonImageToPause ();
	}

	private void pauseClock () {
		ClockUtil.pauseClock (clockInfo);

		notifyBackgroundNotificationService ();

		changeButtonImageToPlay ();
	}

	private void stopClock (boolean descriptionNeeded) {
		// Stop timer
		ClockUtil.setClockRunning (false);

		clockInfo.getLastTimeCount ().setEndedMillis (System.currentTimeMillis ());
		clockInfo.getLastTimeCount ().setHasEnded (true);

		// Save to local storage
		SessionManager.getInstance ().saveClockInfo (clockInfo);

		// If description needed, request to user with a dialog
		if (descriptionNeeded) {
			StopTimerDialog.showDialog (this, clockInfo);
		} else {
			// Otherwise just send it as empty string
			showLoader ();
			sendTimeCountPart (0, "");
		}
	}

	private void cancelClock () {
		this.clockInfo = null;
		ClockUtil.cancelClock ();
		dismissBackgroundNotificationService ();

		changeButtonImageToPlay ();
		hideStopButton ();
		resetTimeUi ();
	}

	@SuppressLint("SimpleDateFormat")
	private void sendTimeCountPart (int position, final String description) {
		ClockInfo.TimeCount timePart = clockInfo.getTimeParts ().get (position);

		// Convert started millis to "HH:mm" string format
		long startedMillis = timePart.getStartedMillis ();
		String startedTime = new SimpleDateFormat ("HH:mm").format (new Date (startedMillis));

		// Convert passed seconds into Hours:Minutes format
		int seconds = (int) timePart.getTime () / 1000;
		int minutes = seconds / 60;
		int hours = minutes / 60;
		minutes -= hours * 60;

		// Check if is zero time billable
		if (hours == 0 && minutes == 0) {
			callNextSendTimeCountPart (position, description);
			return;
		}

		// Call API
		TeamworkService service = new TeamworkService (this);
		service.postInsertTimeEntry (clockInfo.getTargetTask ().getId (), description, startedTime, hours, minutes, new SimpleCallback<String> () {
			@Override
			public void onSuccess (String response) {
				callNextSendTimeCountPart (position, description);
			}

			@Override
			public void onError (String message, int statusCode) {
				hideLoader ();

				showErrorDialog (message);

				resetClockUi ();
			}
		});
	}

	private void callNextSendTimeCountPart (final int position, final String description) {
		// Check if is the last time count part
		if (position >= clockInfo.getTimeParts ().size () - 1) {
			hideLoader ();

			SessionManager.getInstance ().saveClockInfo (null);
			clockInfo = null;
			resetTimeUi ();

			dismissBackgroundNotificationService ();

			// Check if we need to change task
			if (pendingSwitchTask) {
				pendingSwitchTask = false;
				selectTask ();
			} else {
				// Otherwise just show success a message
				showSuccessDialog ("Horas inseridas com sucesso!");
			}
		} else {
			// Otherwise just send the next time count part
			sendTimeCountPart (position + 1, description);
		}
	}

	@SuppressLint("DefaultLocale")
	private void updateTimer () {
		if (clockInfo == null || !ClockUtil.isClockRunning ()) { return; }

		updateTimeUi ((int) clockInfo.getPassedSeconds ());

		tvPassedTime.postDelayed (this::updateTimer, 500);
	}

	// -------------------- UI related stuff
	private void resetClockUi () {
		// If we did loaded all services
		if (servicesLoaded) {
			// Stops the internal timer
			tvPassedTime.removeCallbacks (this::updateTimer);

			// Update UI
			setupClock ();
		}
	}

	private void setupButtonContainerLayoutTransition () {
		LayoutTransition transition = new LayoutTransition ();
		transition.enableTransitionType (LayoutTransition.CHANGING);
		transition.enableTransitionType (LayoutTransition.APPEARING);
		transition.enableTransitionType (LayoutTransition.DISAPPEARING);
		transition.enableTransitionType (LayoutTransition.CHANGE_DISAPPEARING);
		transition.enableTransitionType (LayoutTransition.CHANGE_APPEARING);
		transition.setDuration ((long) (375f / getAnimationScale ()));
		lnButtonContainer.setLayoutTransition (transition);
	}

	private float getAnimationScale () {
		float durationScale = Settings.Global.getFloat (getContentResolver (), Settings.Global.ANIMATOR_DURATION_SCALE, 0);
		if (durationScale < 0) { return 1f; }
		return durationScale;
	}

	private void animateClockView () {
		// Sets the view perspective
		int distance = 3000;
		float scale = getResources ().getDisplayMetrics ().density;
		flClockContainer.setCameraDistance (distance * scale);

		// Animate view flipping
		flClockContainer.post (() -> {
			ObjectAnimator animator = ObjectAnimator.ofFloat (flClockContainer, "rotationY", 90f, 0f);
			animator.setDuration (1000);
			animator.setInterpolator (new DecelerateInterpolator ());
			animator.addListener (new AnimatorListenerAdapter () {
				@Override
				public void onAnimationEnd (Animator animation) {
					super.onAnimationEnd (animation);

					handleStartupAction ();
				}
			});
			animator.start ();
		});
	}

	private void changeButtonImageToPlay () {
		this.btnPlayPause.setImageResource (R.drawable.ic_play);
	}

	private void changeButtonImageToPause () {
		this.btnPlayPause.setImageResource (R.drawable.ic_pause);
	}

	private void showStopButton () {
		btnStop.setVisibility (View.VISIBLE);
	}

	private void hideStopButton () {
		btnStop.setVisibility (View.GONE);
	}

	private void hideTaskLabels () {
		tvCurrentTaskLabel.setVisibility (View.GONE);
		tvCurrentTask.setText ("");
	}

	private void resetTimeUi () {
		hideStopButton ();
		changeButtonImageToPlay ();
		tvPassedTime.setText ("Aperte para iniciar");
		hideTaskLabels ();
	}

	private void updateTimeUi (int seconds) {
		int minutes = seconds / 60;
		seconds -= minutes * 60;
		int hours = minutes / 60;
		minutes -= hours * 60;

		if (Calendar.getInstance ().get (Calendar.MILLISECOND) / 500 == 0) {
			tvPassedTime.setText (String.format ("%02d:%02d:%02d", hours, minutes, seconds));
		} else {
			tvPassedTime.setText (String.format ("%02d %02d %02d", hours, minutes, seconds));
		}

		timeIndicatorVisible = !timeIndicatorVisible;
	}

	// ----------------------------------------------------------------------- Time service handling
	private void loadTimeEntries () {
		showLoader ();

		TeamworkService service = new TeamworkService (this);
		service.getMonthRegisteredTimeEntryList (new SimpleCallback<List<TeamworkTimeEntry>> () {
			@Override
			public void onSuccess (List<TeamworkTimeEntry> response) {
				onTimeEntriesLoaded (response);

				hideLoader ();
			}

			@Override
			public void onError (String message, int statusCode) {
				hideLoader ();

				showErrorDialog (message);
			}
		});
	}

	@SuppressLint("DefaultLocale")
	private void onTimeEntriesLoaded (List<TeamworkTimeEntry> entries) {
		// Calculate the working time based on month day count
		TeamworkPerson account = SessionManager.getInstance ().loadAccountInfo ();
		float dailyWorkingHours = Float.parseFloat (account.getLengthOfDay ());
		int monthDayCount = Calendar.getInstance ().getActualMaximum (Calendar.DAY_OF_MONTH);
		float averageHoursInMonth = dailyWorkingHours * monthDayCount;

		// Sum every time entry
		float totalHourCount = 0;

		for (TeamworkTimeEntry entry : entries) {
			int hours = Integer.parseInt (entry.getHours ());
			int minutes = Integer.parseInt (entry.getMinutes ());

			totalHourCount += hours + minutes / 60f;
		}

		// Populate UI
		// If the current registered time count doesn't overlap the average hours in month
		if (totalHourCount <= averageHoursInMonth) {
			pbHoursPercent.setMax ((int) averageHoursInMonth);
			pbHoursPercent.setProgress ((int) totalHourCount);
			tvHoursReport.setText (String.format ("%d de %d horas registradas", (int) totalHourCount, (int) averageHoursInMonth));

			// Normal background
			pbHoursPercent.setProgressDrawable (getResources ().getDrawable (R.drawable.selector_progressbar));
		} else {
			// We got some extra hours

			int extraHours = (int) (totalHourCount - averageHoursInMonth);
			int percentage = 100 - (int) ((extraHours / totalHourCount) * 100f);

			pbHoursPercent.setMax (100);
			pbHoursPercent.setProgress (percentage);
			tvHoursReport.setText (String.format ("%d horas registradas (%d horas extras)", (int) totalHourCount, extraHours));

			// Overlap background
			pbHoursPercent.setProgressDrawable (getResources ().getDrawable (R.drawable.selector_overlap_progressbar));
		}

		startEntryAnimation ();

		servicesLoaded = true;

		setupClock ();
		animateClockView ();

		if (this.pendingStartClock) {
			this.onBtnPlayPauseClick (btnPlayPause);
			this.pendingStartClock = false;
		}
	}

	private void startEntryAnimation () {
		lnProgress.animate ()
				  .translationY (0)
				  .setInterpolator (new DecelerateInterpolator ())
				  .start ();

		tvAdjustAlarms.setVisibility (View.VISIBLE);
		tvAdjustAlarms.animate ()
					  .alpha (1)
					  .setInterpolator (new DecelerateInterpolator ())
					  .start ();
	}


}
