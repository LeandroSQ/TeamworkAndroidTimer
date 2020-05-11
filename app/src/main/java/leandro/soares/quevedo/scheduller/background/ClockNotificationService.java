package leandro.soares.quevedo.scheduller.background;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import leandro.soares.quevedo.scheduller.helper.PushNotificationHelper;
import leandro.soares.quevedo.scheduller.model.ClockInfo;
import leandro.soares.quevedo.scheduller.util.ClockUtil;
import leandro.soares.quevedo.scheduller.util.SessionManager;
import leandro.soares.quevedo.scheduller.view.activity.home.HomeActivity;

public class ClockNotificationService extends Service implements Runnable {

	public final static String SERVICE_IDENTIFIER = "leandro.soares.quevedo.scheduller.background.ClockNotificationService.Receiver";
	private static volatile boolean backgroundServiceRunning;

	private PushNotificationHelper notificationHelper;
	private Thread timerThread;
	private BroadcastReceiver broadcastReceiver;

	private static ClockNotificationService instance;

	@Override
	public void onCreate () {
		super.onCreate ();

		instance = this;
	}

	@Nullable
	@Override
	public IBinder onBind (Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		this.notificationHelper = new PushNotificationHelper (this);

		this.timerThread = new Thread (this);
		this.timerThread.start ();

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy () {
		super.onDestroy ();

		backgroundServiceRunning = false;

		this.timerThread.interrupt ();
		this.timerThread = null;

		this.notificationHelper.dismissNotification ();
	}

	private void startTimer () {
		if (this.timerThread == null) {
			this.timerThread = new Thread (this);
			this.timerThread.start ();
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void run () {
		if (backgroundServiceRunning) {
			return;
		} else {
			backgroundServiceRunning = true;
		}

		while (this.timerThread != null && this.timerThread.isAlive () && !this.timerThread.isInterrupted ()) {
			try {
				ClockInfo clockInfo = SessionManager.getInstance ().loadClockInfo ();
 				if (clockInfo == null) continue;

				int seconds = Math.round (clockInfo.getPassedSeconds ());

				int minutes = seconds / 60;
				seconds -= minutes * 60;
				int hours = minutes / 60;
				minutes -= hours * 60;

				notificationHelper.showNotification (String.format ("%02d:%02d:%02d", hours, minutes, seconds), ClockUtil.isClockRunning ());
				Thread.sleep (1000);
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}

	public static class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive (Context context, Intent intent) {
			switch (intent.getStringExtra ("action")) {
				case "start": {
					instance.startTimer ();
					break;
				}
				case "pause": {

					break;
				}
				case "dismiss": {
					if (instance.timerThread != null) {
						instance.timerThread.interrupt ();
						instance.timerThread = null;
					}

					instance.notificationHelper.dismissNotification ();
					break;
				}
				case "playPause": {
					ClockUtil.playPauseClock ();

					LocalBroadcastManager.getInstance (context).sendBroadcast (new Intent (HomeActivity.class.getName ()).putExtra ("action", "playPause"));
					break;
				}
				case "stop": {
					// Flag clock as stopped
					ClockUtil.setClockRunning (false);

					// Pull up the notification bar
					Intent it = new Intent (Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
					context.sendBroadcast (it);

					// Start activity on top
					Intent i = new Intent (context, HomeActivity.class);
					i.putExtra ("action", "stop");
					i.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity (i);
					break;
				}
				case "changeTask": {
					// Flag clock as stopped
					ClockUtil.setClockRunning (false);

					// Pull up the notification bar
					Intent it = new Intent (Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
					context.sendBroadcast (it);

					// Start activity on top
					Intent i = new Intent (context, HomeActivity.class);
					i.putExtra ("action", "changeTask");
					i.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity (i);
					break;
				}
			}
		}
	}
}
