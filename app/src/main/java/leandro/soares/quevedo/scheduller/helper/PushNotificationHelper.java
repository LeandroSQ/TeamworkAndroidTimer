package leandro.soares.quevedo.scheduller.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import java.util.List;
import java.util.Objects;

import leandro.soares.quevedo.scheduller.R;
import leandro.soares.quevedo.scheduller.background.ClockNotificationService;
import leandro.soares.quevedo.scheduller.view.activity.home.HomeActivity;

public class PushNotificationHelper {

	private final static int NOTIFICATION_ID = 1380;
	public final static String TIMER_NOTIFICATION_CHANNEL = "timer_channel";
	private final static int NOTIFICATION_ALARM_ID = 1381;
	public final static String ALARM_NOTIFICATION_CHANNEL = "alarm_channel";

	private Context context;
	private NotificationManager notificationManager;
	private Notification notification;

	public PushNotificationHelper (Context context) {
		this.context = context;
		this.notificationManager = (NotificationManager) context.getSystemService (Context.NOTIFICATION_SERVICE);

		setupNotificationChannels ();
	}

	private void setupNotificationChannels () {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !checkNotificationChannels ()) {
			NotificationChannel timerChannel = new NotificationChannel (TIMER_NOTIFICATION_CHANNEL, "Cronômetro", NotificationManager.IMPORTANCE_HIGH);
			timerChannel.setShowBadge (false);

			NotificationChannel alarmChannel = new NotificationChannel (ALARM_NOTIFICATION_CHANNEL, "Alarme de expediente", NotificationManager.IMPORTANCE_DEFAULT);
			alarmChannel.setShowBadge (true);

			notificationManager.createNotificationChannel (timerChannel);
			notificationManager.createNotificationChannel (alarmChannel);
		}
	}

	@TargetApi(Build.VERSION_CODES.O)
	private boolean checkNotificationChannels () {
		boolean timerNotificationChannelExists = false;
		boolean alarmNotificationChannelExists = false;

		List<NotificationChannel> list = notificationManager.getNotificationChannels ();
		for (NotificationChannel channel : list) {
			if (Objects.equals (channel.getId (), TIMER_NOTIFICATION_CHANNEL)) {
				timerNotificationChannelExists = true;
			} else if (Objects.equals (channel.getId (), ALARM_NOTIFICATION_CHANNEL)) {
				alarmNotificationChannelExists = true;
			}
		}

		return timerNotificationChannelExists && alarmNotificationChannelExists;
	}

	public void showNotification (String text, boolean isClockRunning) {
		Intent resultIntent = new Intent (context, HomeActivity.class);
		TaskStackBuilder taskStackBuilder = TaskStackBuilder.create (context);
		taskStackBuilder.addParentStack (HomeActivity.class);
		taskStackBuilder.addNextIntent (resultIntent);

		Notification.Builder builder = new Notification.Builder (context)
				.setContentTitle ("Cronômetro digital business")
				.setContentText (text)
				.setSmallIcon (R.drawable.ic_register_hours)
				.setPriority (Notification.PRIORITY_MAX)
				.setCategory (Notification.CATEGORY_ALARM)
				.setColor (context.getResources ().getColor (R.color.yellow))
				.setContentIntent (taskStackBuilder.getPendingIntent (0, PendingIntent.FLAG_UPDATE_CURRENT))
				.setOngoing (true)
				.setOnlyAlertOnce (true);



		// In Oreo and above, set the channel
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			builder.setChannelId (TIMER_NOTIFICATION_CHANNEL);
			builder.setColorized (true);
		}


		// Actions
		//Intent changeTaskIntent = new Intent (ClockNotificationService.SERVICE_IDENTIFIER);
		Intent changeTaskIntent = new Intent (context, ClockNotificationService.Receiver.class);
		changeTaskIntent.putExtra ("action", "changeTask");
		PendingIntent changeTaskPendingIntent = PendingIntent.getBroadcast (context, 1337, changeTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.addAction (R.drawable.ic_switch_task, "Mudar", changeTaskPendingIntent);

		Intent playPauseIntent = new Intent (context, ClockNotificationService.Receiver.class);
		playPauseIntent.putExtra ("action", "playPause");
		PendingIntent playPausePendingIntent = PendingIntent.getBroadcast (context, 1338, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.addAction (isClockRunning ? R.drawable.ic_pause : R.drawable.ic_play, isClockRunning ? "Pausar" : "Iniciar", playPausePendingIntent);

		Intent stopIntent = new Intent (context, ClockNotificationService.Receiver.class);
		stopIntent.putExtra ("action", "stop");
		PendingIntent stopPendingIntent = PendingIntent.getBroadcast (context, 1339, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.addAction (R.drawable.ic_stop, "Parar", stopPendingIntent);

		this.notification = builder.build ();
		this.notification.flags = Notification.FLAG_ONGOING_EVENT;

		this.notificationManager.notify (NOTIFICATION_ID, this.notification);
	}

	public void dismissNotification () {
		this.notificationManager.cancel (NOTIFICATION_ID);
	}

	public void showAlarmNotification (String text) {
		Intent resultIntent = new Intent (context, HomeActivity.class);
		TaskStackBuilder taskStackBuilder = TaskStackBuilder.create (context);
		taskStackBuilder.addParentStack (HomeActivity.class);
		taskStackBuilder.addNextIntent (resultIntent);

		Notification.Builder builder = new Notification.Builder (context)
				.setContentTitle ("Digital business")
				.setContentText (text)
				.setSmallIcon (R.drawable.ic_register_hours)
				.setPriority (Notification.PRIORITY_MAX)
				.setCategory (Notification.CATEGORY_ALARM)
				.setColor (context.getResources ().getColor (R.color.yellow))
				.setContentIntent (taskStackBuilder.getPendingIntent (NOTIFICATION_ALARM_ID, PendingIntent.FLAG_UPDATE_CURRENT));

		// In Oreo and above, set the channel
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			builder.setChannelId (ALARM_NOTIFICATION_CHANNEL);
		}

		Notification notification = builder.build ();

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		this.notificationManager.notify (NOTIFICATION_ALARM_ID, notification);
	}
}
