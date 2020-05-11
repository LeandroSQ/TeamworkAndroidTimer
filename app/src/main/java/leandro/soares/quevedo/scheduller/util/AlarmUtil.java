package leandro.soares.quevedo.scheduller.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.List;

import leandro.soares.quevedo.scheduller.background.AlarmEventReceiver;
import leandro.soares.quevedo.scheduller.helper.HolidaysHelper;
import leandro.soares.quevedo.scheduller.model.calendar.CalendarHolidayDate;

public final class AlarmUtil {

	private final static boolean DEBUG = false;

	public static void scheduleAlarm (Context context, String message, int hours, int minutes) {
		// Mount the time and date
		Calendar calendar = skipHolidays (context, getNextWorkingDay (Calendar.getInstance ()));
		calendar.set (Calendar.HOUR_OF_DAY, hours);
		calendar.set (Calendar.MINUTE, minutes);
		calendar.set (Calendar.SECOND, 0);
		calendar.set (Calendar.MILLISECOND, 0);

		// Check if the time has past and schedule to tomorrow
		Calendar now = Calendar.getInstance ();
		if (now.get (Calendar.HOUR_OF_DAY) > hours || (now.get (Calendar.HOUR_OF_DAY) == hours && now.get (Calendar.MINUTE) > minutes)) {
			calendar.add (Calendar.DATE, 1);// Next day
		}

		// Request the alarm manager service
		AlarmManager alarmManager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
		// Create a new alarm
		Intent intent = new Intent (context, AlarmEventReceiver.class).putExtra ("text", message);
		PendingIntent pendingIntent = PendingIntent.getBroadcast (context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// Schedule it
		if (DEBUG) {
			alarmManager.set (AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime () + 5000, pendingIntent);
		} else {
			alarmManager.set (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (), pendingIntent);
		}
	}

	public static void scheduleAlarmToNextDay (Context context, String message, int hours, int minutes) {
		// Mount the time and date
		Calendar calendar = Calendar.getInstance ();
		// Skip one day
		calendar.add (Calendar.DATE, 1);
		// If necessary, advance to the next working day
		calendar = skipHolidays (context, getNextWorkingDay (calendar));

		calendar.set (Calendar.HOUR_OF_DAY, hours);
		calendar.set (Calendar.MINUTE, minutes);
		calendar.set (Calendar.SECOND, 0);
		calendar.set (Calendar.MILLISECOND, 0);

		// Request the alarm manager service
		AlarmManager alarmManager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
		// Create a new alarm
		Intent intent = new Intent (context, AlarmEventReceiver.class).putExtra ("text", message);
		PendingIntent pendingIntent = PendingIntent.getBroadcast (context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// Schedule it
		if (DEBUG) {
			alarmManager.set (AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime () + 5000, pendingIntent);
		} else {
			alarmManager.set (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (), pendingIntent);
		}
	}

	public static void cancelAlarmSchedule (Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
		Intent intent = new Intent (context, AlarmEventReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast (context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmManager.cancel (pendingIntent);
	}

	private static Calendar getNextWorkingDay (Calendar calendar) {
		int weekDay = calendar.get (Calendar.DAY_OF_WEEK);

		// Not a working day
		if (weekDay == Calendar.SUNDAY) {
			// Next day, monday
			calendar.add (Calendar.DATE, 1);
		} else if (weekDay == Calendar.SATURDAY) {
			// Next two days, monday
			calendar.add (Calendar.DATE, 2);
		}

		return calendar;
	}

	private static Calendar skipHolidays (Context context, Calendar calendar) {
		HolidaysHelper holidaysHelper = new HolidaysHelper (context);
		List<CalendarHolidayDate> holidays = holidaysHelper.getNotWorkedHolidayDateList ();

		for (CalendarHolidayDate holiday : holidays) {
			Calendar holidayDate = Calendar.getInstance ();
			holidayDate.setTime (holiday.getFormattedDate ());

			if (calendar.getTimeInMillis () == holidayDate.getTimeInMillis ()) {
				// Next day
				calendar.add (Calendar.DATE, 1);

				// Next working day, if needed
				return getNextWorkingDay (calendar);
			}
		}

		// No holiday on this date, simple return as is
		return calendar;
	}

}
