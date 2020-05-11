package leandro.soares.quevedo.scheduller.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import leandro.soares.quevedo.scheduller.helper.PushNotificationHelper;
import leandro.soares.quevedo.scheduller.model.AlarmInfo;
import leandro.soares.quevedo.scheduller.util.AlarmUtil;
import leandro.soares.quevedo.scheduller.util.SessionManager;

public class AlarmEventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive (Context context, Intent intent) {
		Log.d ("scheduller", "Alarm!");
		// Extract info from intent
		String message = intent.getStringExtra ("text");

		// Tries to reschedule the alarm, to the next day
		rescheduleAlarm (context, message);

		// Show notification
		new PushNotificationHelper (context).showAlarmNotification (message);
	}

	private void rescheduleAlarm (Context context, String message) {
		// Check if we got an alarm enabled
		AlarmInfo alarmInfo = SessionManager.getInstance ().loadAlarmInfo ();

		if (alarmInfo != null) {
			// Parse alarm info time into hours and minutes
			String time = alarmInfo.getStartHours ();
			String []timeParts = time.split (":");
			int hours = Integer.parseInt (timeParts[0]);
			int minutes = Integer.parseInt (timeParts[1]);
			// Reschedule to the next day
			AlarmUtil.scheduleAlarmToNextDay (context, message, hours, minutes);
		} else {
			Log.e ("scheduller", "Could not reschedule the alarm");
		}
	}

}
